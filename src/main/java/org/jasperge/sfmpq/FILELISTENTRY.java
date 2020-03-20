package org.jasperge.sfmpq;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class FILELISTENTRY extends Structure {
    public int dwFileExists; // Nonzero if this entry is used
    public int lcLocale; // Locale ID of file
    public int dwCompressedSize; // Compressed size of file
    public int dwFullSize; // Uncompressed size of file
    public int dwFlags; // Flags for file
    public char[] szFileName = new char[260];

    protected List<String> getFieldOrder() {
        return Arrays.asList("dwFileExists", "lcLocale", "dwCompressedSize", "dwFullSize", "dwFlags", "szFileName");
    }

    public static class ByReference extends FILELISTENTRY implements Structure.ByReference {}
}
