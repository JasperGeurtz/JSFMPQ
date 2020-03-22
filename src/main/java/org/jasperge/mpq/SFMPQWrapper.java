package org.jasperge.mpq;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import org.jasperge.sfmpq.FILELISTENTRY;
import org.jasperge.sfmpq.SFMPQ;
import org.jasperge.sfmpq.SFMPQVERSION;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.jasperge.sfmpq.SFMPQ.*;

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

    public Pointer openArchive(String fileName, int priority, int flags) {
        PointerByReference ptr = new PointerByReference();

        if (!sfmpq.SFileOpenArchive(fileName, priority, flags, ptr)) {
            System.err.println("openArchive Error: " + Native.getLastError());
            return null;
        }
        return ptr.getValue();
    }

    public boolean closeArchive(Pointer archive) {
        return sfmpq.SFileCloseArchive(archive);
    }

    public Pointer openFile(String fileName) {
        PointerByReference ptr = new PointerByReference();

        if (!sfmpq.SFileOpenFile(fileName, ptr)) {
            System.err.println("openFile Error: " + Native.getLastError());
            return null;
        }
        return ptr.getValue();
    }

    public Pointer openArchiveForUpdate(String fileName, int flags, int maximumFilesInArchive) {
        Pointer ptr = sfmpq.MpqOpenArchiveForUpdate(fileName, flags, maximumFilesInArchive);

        if (Pointer.nativeValue(ptr) == 0xffffffff) {
            System.err.println("openArchiveForUpdate Error: " + Native.getLastError());
            return null;
        }
        return ptr;
    }

    public int closeUpdatedArchive(Pointer archive) {
        int ret = sfmpq.MpqCloseUpdatedArchive(archive, 0);
        if (ret == 0) {
            System.err.println("closeUpdatedArchive Error: " + Native.getLastError());
        }
        return ret;
    }

    public List<FILELISTENTRY> listFiles(Pointer archive) {
        int n = sfmpq.SFileGetFileInfo(archive, SFILE_INFO_HASH_TABLE_SIZE);
        if (n == 0) {
            return new ArrayList<>();
        }
        FILELISTENTRY[] fileListBuffer = new FILELISTENTRY[n];

        if (!sfmpq.SFileListFiles(archive, listFile, fileListBuffer, 0)) {
            System.err.println("listFiles Error: " + Native.getLastError());
            return null;
        }
        return Arrays.stream(fileListBuffer)
                .filter(f -> f.dwFileExists != 0)
                .collect(Collectors.toList());
    }

    public boolean addFileToArchive(Pointer archive, String sourceFileName, String destFileName, int flags) {
        boolean ret = sfmpq.MpqAddFileToArchive(archive, sourceFileName, destFileName, flags);
        if (!ret) {
            System.err.println("addFileToArchive Error: " + Native.getLastError());
        }
        return ret;
    }

    public Pointer openFileEx(Pointer archive, String fileName, int flags) {
        PointerByReference ptr = new PointerByReference();
        if (!sfmpq.SFileOpenFileEx(archive, fileName, flags, ptr)) {
            System.err.println("openFileEx Error: " + Native.getLastError());
            return null;
        }
        return ptr.getValue();
    }

    public boolean closeFile(Pointer file) {
        boolean ret = sfmpq.SFileCloseFile(file);
        if (!ret) {
            System.err.println("closeFile Error: " + Native.getLastError());
        }
        return ret;
    }

    public int getFileSize(Pointer file, PointerByReference fileSizeHigh) {
        return sfmpq.SFileGetFileSize(file, fileSizeHigh);
    }

    public boolean readFile(Pointer file, byte[] buffer, int fileSize, IntByReference bytesRead, int overlapped) {
        boolean ret = sfmpq.SFileReadFile(file, buffer, fileSize, bytesRead, overlapped);
        if (!ret) {
            System.err.println("readFile Error: " + Native.getLastError());
        }
        return ret;
    }
}
