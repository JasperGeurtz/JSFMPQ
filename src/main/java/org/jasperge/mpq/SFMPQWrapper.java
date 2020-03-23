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

    public Pointer openArchive(String fileName, int priority, int flags) throws MPQException{
        PointerByReference ptr = new PointerByReference();

        if (!sfmpq.SFileOpenArchive(fileName, priority, flags, ptr)) {
            throw new MPQException("openArchive Error: " + Native.getLastError());
        }
        return ptr.getValue();
    }

    public void closeArchive(Pointer archive) throws MPQException {
        if (!sfmpq.SFileCloseArchive(archive)) {
            throw new MPQException("closeArchive Error: " + Native.getLastError());
        }
    }

    public Pointer openFile(String fileName) throws MPQException {
        PointerByReference ptr = new PointerByReference();

        if (!sfmpq.SFileOpenFile(fileName, ptr)) {
            throw new MPQException("openFile Error: " + Native.getLastError());
        }
        return ptr.getValue();
    }

    public Pointer openArchiveForUpdate(String fileName, int flags, int maximumFilesInArchive) throws MPQException {
        Pointer ptr = sfmpq.MpqOpenArchiveForUpdate(fileName, flags, maximumFilesInArchive);

        if (Pointer.nativeValue(ptr) == 0xffffffff) {
            throw new MPQException("openArchiveForUpdate Error: " + Native.getLastError());
        }
        return ptr;
    }

    public void closeUpdatedArchive(Pointer archive) throws MPQException{
        if (sfmpq.MpqCloseUpdatedArchive(archive, 0) == 0) {
            throw new MPQException("closeUpdatedArchive Error: " + Native.getLastError());
        }
    }

    public List<FILELISTENTRY> listFiles(Pointer archive) throws MPQException{
        int n = sfmpq.SFileGetFileInfo(archive, SFILE_INFO_HASH_TABLE_SIZE);
        if (n == 0) {
            return new ArrayList<>();
        }
        FILELISTENTRY[] fileListBuffer = new FILELISTENTRY[n];

        if (!sfmpq.SFileListFiles(archive, listFile, fileListBuffer, 0)) {
            throw new MPQException("listFiles Error: " + Native.getLastError());
        }
        return Arrays.stream(fileListBuffer)
                .filter(f -> f.dwFileExists != 0)
                .collect(Collectors.toList());
    }

    public void addFileToArchive(Pointer archive, String sourceFileName, String destFileName, int flags) throws MPQException {
        if (!sfmpq.MpqAddFileToArchive(archive, sourceFileName, destFileName, flags)) {
            throw new MPQException("addFileToArchive Error: " + Native.getLastError());
        }
    }


    public void addFileFromBuffer(Pointer archive, byte[] bytes, int fileSize, String destFileName, int flags) throws MPQException {
        if (!sfmpq.MpqAddFileFromBuffer(archive, bytes, fileSize, destFileName, flags)) {
            throw new MPQException("AddFileFromBuffer Error: " + Native.getLastError());
        }
    }

    public Pointer openFileEx(Pointer archive, String fileName, int flags) throws MPQException {
        PointerByReference ptr = new PointerByReference();
         if (!sfmpq.SFileOpenFileEx(archive, fileName, flags, ptr)) {
            throw new MPQException("openFileEx Error: " + Native.getLastError());
        }
        return ptr.getValue();
    }

    public void closeFile(Pointer file) throws MPQException {
        if (!sfmpq.SFileCloseFile(file)) {
            throw new MPQException("closeFile Error: " + Native.getLastError());
        }
    }

    public int getFileSize(Pointer file, PointerByReference fileSizeHigh) throws MPQException{
        int size = sfmpq.SFileGetFileSize(file, fileSizeHigh);
        if (size < 0) {
            throw new MPQException("getFileSize Error: " + Native.getLastError() + " | invalid filesize: " + size);
        }
        return size;
    }

    public void readFile(Pointer file, byte[] buffer, int fileSize, IntByReference bytesRead, int overlapped) throws MPQException {
        if (!sfmpq.SFileReadFile(file, buffer, fileSize, bytesRead, overlapped)) {
            throw new MPQException("readFile Error: " + Native.getLastError());
        }
    }
}
