package org.jasperge.mpq;

import com.sun.jna.*;
import com.sun.jna.ptr.*;
import org.jasperge.sfmpq.*;
import java.util.*;
import java.util.stream.*;

import static org.jasperge.sfmpq.SFMPQ.*;

public class SFMPQWrapper {
    public final static SFMPQ sfmpq = SFMPQ.getInstance(); //only 1 instance

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
            throw new MPQException();
        }
        return ptr.getValue();
    }

    public void closeArchive(Pointer archive) throws MPQException {
        if (!sfmpq.SFileCloseArchive(archive)) {
            throw new MPQException();
        }
    }

    public Pointer openArchiveForUpdate(String fileName, int flags, int maximumFilesInArchive) throws MPQException {
        Pointer ptr = sfmpq.MpqOpenArchiveForUpdate(fileName, flags, maximumFilesInArchive);

        if (ptr == null || Pointer.nativeValue(ptr) == 0xffffffff) {
            throw new MPQException();
        }
        return ptr;
    }

    public void closeUpdatedArchive(Pointer archive) throws MPQException{
        if (sfmpq.MpqCloseUpdatedArchive(archive, 0) == 0) {
            throw new MPQException();
        }
    }

    public List<FILELISTENTRY> listFiles(Pointer archive, String filelist) throws MPQException{
        int n = sfmpq.SFileGetFileInfo(archive, SFILE_INFO_HASH_TABLE_SIZE);
        if (n == 0) {
            return new ArrayList<>();
        }
        FILELISTENTRY[] fileListBuffer = new FILELISTENTRY[n];

        if (!sfmpq.SFileListFiles(archive, filelist, fileListBuffer, 0)) {
            throw new MPQException();
        }
        return Arrays.stream(fileListBuffer)
                .filter(f -> f.dwFileExists != 0)
                .collect(Collectors.toList());
    }

    public void addFileToArchive(Pointer archive, String sourceFilename, String destFilename, int flags) throws MPQException {
        if (!sfmpq.MpqAddFileToArchive(archive, sourceFilename, destFilename, flags)) {
            throw new MPQException();
        }
    }


    public void addFileFromBuffer(Pointer archive, byte[] bytes, int fileSize, String destFilename, int flags) throws MPQException {
        if (!sfmpq.MpqAddFileFromBuffer(archive, bytes, fileSize, destFilename, flags)) {
            throw new MPQException();
        }
    }

    public Pointer openFileEx(Pointer archive, String fileName, int flags) throws MPQException {
        PointerByReference ptr = new PointerByReference();
         if (!sfmpq.SFileOpenFileEx(archive, fileName, flags, ptr)) {
            throw new MPQException();
        }
        return ptr.getValue();
    }

    public void closeFile(Pointer file) throws MPQException {
        if (!sfmpq.SFileCloseFile(file)) {
            throw new MPQException();
        }
    }

    public int getFileSize(Pointer file, PointerByReference fileSizeHigh) throws MPQException{
        int size = sfmpq.SFileGetFileSize(file, fileSizeHigh);
        if (size < 0) {
            throw new MPQException();
        }
        return size;
    }

    public void readFile(Pointer file, byte[] buffer, int fileSize, IntByReference bytesRead, int overlapped) throws MPQException {
        if (!sfmpq.SFileReadFile(file, buffer, fileSize, bytesRead, overlapped)) {
            throw new MPQException();
        }
    }

    public void deleteFile(Pointer archive, String sourceFilename) throws MPQException {
        if (!sfmpq.MpqDeleteFile(archive, sourceFilename)) {
            throw new MPQException();
        }
    }

    public void addWave(Pointer archive, String sourceFilename, String destFilename, int flags, int quality) throws MPQException {
        if (!sfmpq.MpqAddWaveToArchive(archive, sourceFilename, destFilename, flags, quality)) {
            throw new MPQException();
        }
    }

    public void addWaveFromBuffer(Pointer archive, byte[] bytes, int length, String destFilename, int flags, int quality) throws MPQException {
        if (!sfmpq.MpqAddWaveFromBuffer(archive, bytes, length, destFilename, flags, quality)) {
            throw new MPQException();
        }
    }

    public void renameFile(Pointer archive, String filenameBefore, String filenameAfter) throws MPQException {
        if (!sfmpq.MpqRenameFile(archive, filenameBefore, filenameAfter)) {
            throw new MPQException();
        }
    }

    public void compact(Pointer archive) throws MPQException {
        if (!sfmpq.MpqCompactArchive(archive)) {
            throw new MPQException();
        }
    }
}
