package org.jasperge.mpq;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

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
     * Add file to Archive: WILL REPLACE!
     */
    public MPQEditor addFile(String sourceFileName, String destFileName) throws MPQException {
        sfmpq.addFileToArchive(archive, sourceFileName, destFileName, MAFA_REPLACE_EXISTING);
        return this;
    }

    /**
     * Add file using bytes instead of path.
     */
    public MPQEditor addFileFromBuffer(byte[] bytes, String destFileName) throws MPQException {
        sfmpq.addFileFromBuffer(archive, bytes, bytes.length, destFileName, MAFA_REPLACE_EXISTING);
        return this;
    }

    /**
     * Extract file, if destFileName == null, it will take the name of the sourceFile
     * but with all `/` & `\\` replaced with `_`
     */
    public MPQEditor extractFile(String sourceFileName, Path destFileName) throws IOException {
        if (destFileName == null) {
            destFileName = Paths.get(sourceFileName.replace('\\', '_').replace('/','_'));
        }
        byte[] buffer = extractFileBuffer(sourceFileName);
        if (buffer == null) {
            throw new MPQException("extractFileBuffer");
        }
        try (FileOutputStream fos = new FileOutputStream(destFileName.toString())) {
            fos.write(buffer);
        }
        return this;
    }

    public MPQEditor extractAll(String outputDir) throws IOException {
        File dir = new File(outputDir);
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
            extractFile(file.name, Paths.get(outputDir + "\\" + file.name
                    .replace("\\", "_")
                    .replace("/", "_")));
        }
        return this;
    }

    public byte[] extractFileBuffer(String sourceFileName) throws MPQException {
        Pointer filePtr = sfmpq.openFileEx(archive, sourceFileName, 0);
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

    public void close() throws MPQException {
        sfmpq.closeUpdatedArchive(archive);
    }

    public boolean hasFile(String filename) throws MPQException {
        try {
            Pointer ptr = sfmpq.openFileEx(archive, filename, 0);
            sfmpq.closeFile(ptr);
            return true;
        } catch (MPQException e) {
            return false;
        }
    }
}
