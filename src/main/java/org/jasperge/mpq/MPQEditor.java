package org.jasperge.mpq;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.jasperge.sfmpq.SFMPQ.*;

public class MPQEditor implements AutoCloseable{
    final SFMPQWrapper sfmpq = new SFMPQWrapper();
    private final Pointer archive;

    /**
     * fails if doesn't exist
     */
    public MPQEditor(Path filePath) throws MPQException {
        // last param ignored anyway
        archive = sfmpq.openArchiveForUpdate(filePath.toString(), MOAU_OPEN_EXISTING, 262144);
    }

    /**
     * Add file to archive: WILL REPLACE and compress!
     */
    public void addFile(String sourceFilename, String destFilename) throws MPQException {
        sfmpq.addFileToArchive(archive, sourceFilename, destFilename, MAFA_REPLACE_EXISTING | MAFA_COMPRESS);
    }

    /**
     * Add WAV file to archive: WILL REPLACE!
     */
    public void addWave(String sourceFilename, String destFilename, WaveQuality quality) throws MPQException {
        sfmpq.addWave(archive, sourceFilename, destFilename, MAFA_REPLACE_EXISTING , quality.value);
    }

    /**
     * Add file using bytes instead of path.
     */
    public void addFileFromBuffer(byte[] bytes, String destFilename) throws MPQException {
        sfmpq.addFileFromBuffer(archive, bytes, bytes.length, destFilename, MAFA_REPLACE_EXISTING);
    }

    /**
     * Add WAV file using bytes instead of path.
     */
    public void addWaveFromBuffer(byte[] bytes, String destFilename, WaveQuality quality) throws MPQException {
        sfmpq.addWaveFromBuffer(archive, bytes, bytes.length, destFilename, MAFA_REPLACE_EXISTING, quality.value);
    }

    /**
     * Delete file from the archive
     */
    public void deleteFile(String sourceFilename) throws MPQException {
        sfmpq.deleteFile(archive, sourceFilename);
    }

    /**
     * Rename file in the archive
     */
    public void renameFile(String filenameBefore, String filenameAfter) throws MPQException {
        sfmpq.renameFile(archive, filenameBefore, filenameAfter);
    }

    /**
     * Extract file, if destFilename == null, it will take the name of the sourceFile
     * but with all `/` & `\\` replaced with `_`
     */
    public void extractFile(String sourceFilename, Path destFilename) throws IOException {
        if (destFilename == null) {
            destFilename = Paths.get(sourceFilename.replace('\\', '_').replace('/','_'));
        }
        byte[] buffer = extractFileBuffer(sourceFilename);
        try (FileOutputStream fos = new FileOutputStream(destFilename.toString())) {
            fos.write(buffer);
        }
    }

    public void extractAll(Path outputDir, boolean stopOnError) throws IOException {
        File dir = outputDir.toFile();
        if (!dir.exists()) {
            boolean ret = dir.mkdirs();
            if (!ret) {
                throw new MPQException("extractAll Error: couldn't create directory: " + outputDir);
            }
        }
        else if (!dir.isDirectory()) {
            throw new MPQException("extractAll Error: " + outputDir + " is not a directory");
        }
        for (MPQFile file : getFiles()) {
            try {
                extractFile(file.name, Paths.get(outputDir + "\\" + file.name
                        .replace("\\", "_")
                        .replace("/", "_")));
            } catch (MPQException exc) {
                if (stopOnError) {
                    throw exc;
                }
            }
        }
    }

    public byte[] extractFileBuffer(String sourceFilename) throws MPQException {
        Pointer filePtr = sfmpq.openFileEx(archive, sourceFilename, 0);
        int fileSize = sfmpq.getFileSize(filePtr, null);
        byte[] buffer = new byte[fileSize];
        if (fileSize > 0) {
            sfmpq.readFile(filePtr, buffer, fileSize, new IntByReference(), 0);
        }
        sfmpq.closeFile(filePtr);
        return buffer;
    }

    public List<MPQFile> getFiles() throws MPQException {
        return sfmpq.listFiles(archive).stream()
                .map(MPQFile::new)
                .collect(Collectors.toList());
    }

    public boolean hasFile(String filename) {
        // use raw SFMPQ directly
        PointerByReference ptr = new PointerByReference();
        boolean ret = SFMPQWrapper.sfmpq.SFileOpenFileEx(archive, filename, 0, ptr);
        if (ret) { //close if succesfully opened
            SFMPQWrapper.sfmpq.SFileCloseFile(ptr.getValue());
        }
        return ret;
    }

    public void compact() throws MPQException {
        sfmpq.compact(archive);
    }

    public void close() throws MPQException {
        try {
            compact();
        } catch (MPQException e) {
            e.printStackTrace();
        }
        sfmpq.closeUpdatedArchive(archive);
    }
}
