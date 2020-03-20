package org.jasperge.sfmpq;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class MPQHEADER extends Structure {
    public int dwMPQID; //"MPQ\x1A" for mpq's, "BN3\x1A" for bncache.dat
    public int dwHeaderSize; // Size of this header
    public int dwMPQSize; //The size of the mpq archive
    public short wUnused0C; // Seems to always be 0
    public short wBlockSize; // Size of blocks in files equals 512 << wBlockSize
    public int dwHashTableOffset; // Offset to hash table
    public int dwBlockTableOffset; // Offset to block table
    public int dwHashTableSize; // Number of entries in hash table
    public int dwBlockTableSize; // Number of entries in block table

    protected List<String> getFieldOrder() {
        return Arrays.asList("dwMPQID", "dwHeaderSize", "dwMPQSize", "wUnused0C", "wBlockSize", "dwHashTableOffset",
                "dwBlockTableOffset", "dwHashTableSize", "dwBlockTableSize");
    }

    public static class ByReference extends MPQHEADER implements Structure.ByReference {}
    public static class ByValue extends MPQHEADER implements Structure.ByValue {}
}
