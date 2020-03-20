package org.jasperge.sfmpq;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class BLOCKTABLEENTRY extends Structure {
    public int dwFileOffset; // Offset to file
    public int dwCompressedSize; // Compressed size of file
    public int dwFullSize; // Uncompressed size of file
    public int dwFlags; // Flags for file

    protected List<String> getFieldOrder() {
        return Arrays.asList("dwFileOffset", "dwCompressedSize", "dwFullSize", "dwFlags");
    }

    public static class ByReference extends BLOCKTABLEENTRY implements Structure.ByReference {}
}
