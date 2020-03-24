package org.jasperge.sfmpq;

import com.sun.jna.Native;
import org.jasperge.misc.FileTools;

import java.io.File;

class SFMPQInstance {
    static SFMPQ get() {
        String dllName = System.getProperty("sun.arch.data.model").equals("64") ? "SFmpq64" : "SFmpq";
        File libLocation = FileTools.copyToTemp(dllName + ".dll");
        System.load(libLocation.getAbsolutePath());
        return Native.load(dllName, SFMPQ.class);
    }
}
