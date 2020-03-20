package org.jarsperge.sfmpq;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

/**
 * Archive handles may be typecasted to this struct so you can access
 * some of the archive's properties and the decrypted hash table and
 * block table directly.  This struct is based on Storm's internal
 * struct for archive handles.
 */
public class MPQARCHIVE extends Structure {
    // Arranged according to priority with lowest priority first
    public MPQARCHIVE.ByReference lpNextArc; //0// Pointer to the next MPQARCHIVE struct. Pointer to addresses of first and last archives if last archive
    public MPQARCHIVE.ByReference lpPrevArc; //4// Pointer to the previous MPQARCHIVE struct. 0xEAFC5E23 if first archive
    public char[] szFileName = new char[260]; //8// Filename of the archive

    //[TYPE=HANDLE]
    public Pointer hFile; //10C// The archive's file handle

    public int dwFlags1; //110// Some flags, bit 0 seems to be set when opening an archive from a hard drive if bit 1 in the flags for SFileOpenArchive is set, bit 1 (0 based) seems to be set when opening an archive from a CD
    public int dwPriority; //114// Priority of the archive set when calling SFileOpenArchive
    public MPQFILE.ByReference lpLastReadFile; //118// Pointer to the last read file's MPQFILE struct. This is cleared when finished reading a block
    public int dwUnk; //11C// Seems to always be 0
    public int dwBlockSize; //120// Size of file blocks in bytes

    //[TYPE=BYTE *]
    public Pointer lpLastReadBlock; //124// Pointer to the read buffer for archive. This is cleared when finished reading a block

    public int dwBufferSize; //128// Size of the read buffer for archive. This is cleared when finished reading a block
    public int dwMPQStart; //12C// The starting offset of the archive
    public int dwMPQEnd; //130// The ending offset of the archive
    public MPQHEADER.ByReference lpMPQHeader; //134// Pointer to the archive header
    public BLOCKTABLEENTRY.ByReference lpBlockTable; //138// Pointer to the start of the block table
    public HASHTABLEENTRY.ByReference lpHashTable; //13C// Pointer to the start of the hash table
    public int dwReadOffset; //140// Offset to the data for a file
    public int dwRefCount; //144// Count of references to this open archive.  This is incremented for each file opened from the archive, and decremented for each file closed
    // Extra struct members used by SFmpq
    public MPQHEADER.ByValue MpqHeader;
    public int dwFlags; //The only flags that should be changed are MOAU_MAINTAIN_LISTFILE and MOAU_MAINTAIN_ATTRIBUTES, changing any others can have unpredictable effects
    public String lpFileName;
    public int dwExtraFlags;

    protected List<String> getFieldOrder() {
        return Arrays.asList("lpNextArc", "lpPrevArc", "szFileName", "hFile", "dwFlags1", "dwPriority",
                "lpLastReadFile", "dwUnk", "dwBlockSize", "lpLastReadBlock", "dwBufferSize", "dwMPQStart",
                "dwMPQEnd", "lpMPQHeader", "lpBlockTable", "lpHashTable", "dwReadOffset", "dwRefCount", "MpqHeader",
                "dwFlags", "lpFileName", "dwExtraFlags");
    }

    public MPQARCHIVE(Pointer ptr) {
        super(ptr);
    }

    public static class ByReference extends MPQARCHIVE implements Structure.ByReference {
        public ByReference(Pointer ptr) {
            super(ptr);
        }
    }
}
