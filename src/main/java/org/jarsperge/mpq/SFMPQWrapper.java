package org.jarsperge.mpq;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import org.jarsperge.sfmpq.*;

import java.lang.reflect.InvocationTargetException;

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

    public MPQARCHIVE openArchive(String lpFileName, int dwPriority, int dwFlag) {

        PointerByReference ptr = new PointerByReference();
        System.out.println(ptr.getValue());

        boolean ret = sfmpq.SFileOpenArchive(lpFileName, dwPriority, dwFlag, ptr);
        if (!ret) {
            System.err.println("openArchive Error: " + Native.getLastError());
        }
        System.out.println(ptr.getValue());
        MPQARCHIVE a = new MPQARCHIVE.ByReference(ptr.getValue());
        System.out.println(a.lpFileName);
        //System.out.println(abr.lpFileName);
        return a;
    }

}
