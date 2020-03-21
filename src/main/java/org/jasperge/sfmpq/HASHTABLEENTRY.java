package org.jasperge.sfmpq;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class HASHTABLEENTRY extends Structure {
    public int dwNameHashA; // First name hash of file
    public int dwNameHashB; // Second name hash of file
    public int lcLocale; // Locale ID of file
    public int dwBlockTableIndex; // Index to the block table entry for the file

    protected List<String> getFieldOrder() {
        return Arrays.asList("dwNameHashA", "dwNameHashB", "lcLocale", "dwBlockTableIndex");
    }

    public static class ByReference extends HASHTABLEENTRY implements Structure.ByReference {}
}
