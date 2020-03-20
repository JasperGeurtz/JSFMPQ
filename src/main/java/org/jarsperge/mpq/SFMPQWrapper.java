package org.jarsperge.mpq;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import org.jarsperge.sfmpq.SFMPQ;
import org.jarsperge.sfmpq.SFMPQVERSION;

public class SFMPQWrapper {
    public final SFMPQ sfmpq = SFMPQ.instanciate();

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
        if (p == null) {
            System.err.println("openArchiveForUpdate Error: " + Native.getLastError());
        }
        return p;
    }

    public int closeUpdatedArchive(Pointer archive) {
        return sfmpq.MpqCloseUpdatedArchive(archive, 0);
    }


    public boolean addFileToArchive(Pointer archive, String sourceFileName, String destFileName, int dwFlags) {
        boolean ret = sfmpq.MpqAddFileToArchive(archive, sourceFileName, destFileName, dwFlags);
        if (!ret) {
            System.err.println("addFileToArchive Error: " + Native.getLastError());
        }
        return ret;
    }
}