package org.jasperge.mpq;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.jasperge.sfmpq.FILELISTENTRY;
import org.jasperge.sfmpq.SFMPQ;
import org.jasperge.sfmpq.SFMPQVERSION;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.jasperge.sfmpq.SFMPQ.SFILE_INFO_HASH_TABLE_SIZE;

public class SFMPQWrapper {
    private static SFMPQ sfmpq = SFMPQ.getInstance(); //only 1 instance

    public String listFile = new File("Listfile.txt").getAbsolutePath();

    public String getVersionString() {
        return sfmpq.MpqGetVersionString();
    }

    public float getVersionFloat() {
        return sfmpq.MpqGetVersion();
    }

    public SFMPQVERSION getVersionStruct() {
        return sfmpq.SFMpqGetVersion();
    }

    public Pointer openArchive(String lpFileName, int dwPriority, int dwFlag) {
        PointerByReference ptr = new PointerByReference();

        boolean ret = sfmpq.SFileOpenArchive(lpFileName, dwPriority, dwFlag, ptr);
        if (!ret) {
            System.err.println("openArchive Error: " + Native.getLastError());
        }
        return ptr.getValue();
    }

    public boolean closeArchive(Pointer archive) {
        return sfmpq.SFileCloseArchive(archive);
    }

    public Pointer openFile(String fileName) {
        PointerByReference ptr = new PointerByReference();

        boolean ret = sfmpq.SFileOpenFile(fileName, ptr);
        if (!ret) {
            System.err.println("openFile Error: " + Native.getLastError());
        }
        return ptr.getValue();
    }

    public Pointer openArchiveForUpdate(String lpFileName, int dwFlags, int dwMaximumFilesInArchive) {
        Pointer p = sfmpq.MpqOpenArchiveForUpdate(lpFileName, dwFlags, dwMaximumFilesInArchive);

        if (Pointer.nativeValue(p) == 0xffffffff) {
            System.err.println("openArchiveForUpdate Error: " + Native.getLastError());
            p = null;
        }
        return p;
    }

    public int closeUpdatedArchive(Pointer archive) {
        return sfmpq.MpqCloseUpdatedArchive(archive, 0);
    }

    public List<FILELISTENTRY> listFiles(Pointer archive) {
        int n = sfmpq.SFileGetFileInfo(archive, SFILE_INFO_HASH_TABLE_SIZE);
        if (n == 0) {
            return new ArrayList<>();
        }
        FILELISTENTRY[] fileListBuffer = new FILELISTENTRY[n];

        boolean ret = sfmpq.SFileListFiles(archive, listFile, fileListBuffer, 0);
        if (!ret) {
            System.err.println("listFiles Error: " + Native.getLastError());
            return null;
        }
        return Arrays.stream(fileListBuffer)
                .filter(f -> f.dwFileExists != 0)
                .collect(Collectors.toList());
    }

    public boolean addFileToArchive(Pointer archive, String sourceFileName, String destFileName, int dwFlags) {
        boolean ret = sfmpq.MpqAddFileToArchive(archive, sourceFileName, destFileName, dwFlags);
        if (!ret) {
            System.err.println("addFileToArchive Error: " + Native.getLastError());
        }
        return ret;
    }
}
