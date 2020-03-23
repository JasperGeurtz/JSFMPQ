package org.jasperge.mpq;

import com.sun.jna.Native;

import java.io.IOException;

public class MPQException extends IOException {
    MPQException(String message) {
        super(message);
    }

    MPQException() {
        super("\"" + Thread.currentThread().getStackTrace()[2].getMethodName() + "\" failed. Error code: " + Native.getLastError());
    }
}
