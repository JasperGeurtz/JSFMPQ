package org.jasperge.mpq;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.jasperge.sfmpq.SFMPQ.*;

public class MPQEditor {
    final SFMPQWrapper sfmpq = new SFMPQWrapper();
    private final Pointer archive;

    /**
     * fails if doesn't exist
     */
    public MPQEditor(String filePath) {
        // last param ignored anyway
        archive = sfmpq.openArchiveForUpdate(filePath, MOAU_OPEN_EXISTING, 262144);
    }

    /**
     * Add file to Archive: WILL REPLACE!
     */
    public boolean addFile(String sourceFileName, String destFileName) {
        return sfmpq.addFileToArchive(archive, sourceFileName, destFileName, MAFA_REPLACE_EXISTING);
    }

    /**
     * Extract file, if destFileName == null, it will take the name of the sourceFile
     * but with all `/` & `\\` replaced with `_`
     */
    public boolean extractFile(String sourceFileName, String destFileName) throws IOException {
        if (destFileName == null) {
            destFileName = sourceFileName.replace('\\', '_').replace('/','_');
        }
        byte[] buffer = extractFileBuffer(sourceFileName);
        if (buffer == null) {
            return false;
        }
        try (FileOutputStream fos = new FileOutputStream(destFileName)) {
            fos.write(buffer);
        }
        return true;
    }

    public boolean extractAll(String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            boolean ret = dir.mkdirs();
            if (!ret) {
                return false;
            }
        }
        else if (!dir.isDirectory()) {
            return false;
        }
        boolean all = true;
        for (MPQFile file : getFiles()) {
            boolean ret = extractFile(file.name, outputDir + "\\" + file.name
                    .replace("\\", "_")
                    .replace("/", "_"));
            if (!ret) {
                System.err.println("failed to extract: " + file.name);
            }
            all &= ret;
        }
        return all;
    }

    public byte[] extractFileBuffer(String sourceFileName) {
        Pointer filePtr = sfmpq.openFileEx(archive, sourceFileName, 0);
        if (filePtr == null) {
            return null;
        }
        int fileSize = sfmpq.getFileSize(filePtr, null);
        if (fileSize < 0) {
            return null;
        }
        byte[] buffer = new byte[fileSize];
        if (fileSize > 0) {
            boolean ret = sfmpq.readFile(filePtr, buffer, fileSize, new IntByReference(), 0);
            if (!ret) {
                return null;
            }
        }
        sfmpq.closeFile(filePtr);
        return buffer;
    }

    public List<MPQFile> getFiles() {
        return sfmpq.listFiles(archive).stream()
                .map(MPQFile::new)
                .collect(Collectors.toList());
    }

    public boolean close() {
        return sfmpq.closeUpdatedArchive(archive) == 1;
    }

}
