package org.jasperge.sfmpq;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class MPQFILE extends Structure {
        public MPQFILE.ByReference lpNextFile; //0// Pointer to the next MPQFILE struct. Pointer to addresses of first and last files if last file
        public MPQFILE.ByReference lpPrevFile; //4// Pointer to the previous MPQFILE struct. 0xEAFC5E13 if first file
        public char[] szFileName = new char[260]; //8// Filename of the file

        // [TYPE=HANDLE]
        public Pointer hFile; //10C// Always INVALID_HANDLE_VALUE for files in MPQ archives. For files not in MPQ archives, this is the file handle for the file and the rest of this struct is filled with zeros except for dwRefCount

        public MPQARCHIVE.ByReference lpParentArc; //110// Pointer to the MPQARCHIVE struct of the archive in which the file is contained
        public BLOCKTABLEENTRY.ByReference lpBlockEntry; //114// Pointer to the file's block table entry
        public int dwCryptKey; //118// Decryption key for the file
        public int dwFilePointer; //11C// Position of file pointer in the file
        public int dwUnk; //120// Seems to always be 0
        public int dwBlockCount; //124// Number of blocks in file

        //[TYPE=DWORD *]
        public Pointer lpdwBlockOffsets; //128// Offsets to blocks in file. There are 1 more of these than the number of blocks. The values for this are set after the first read

        public int dwReadStarted; //12C// Set to 1 after first read
        public boolean bStreaming; //130// 1 when streaming a WAVE

        //[TYPE=BYTE *]
        public Pointer lpLastReadBlock; //134// Pointer to the read buffer for file. This starts at the position specified in the last SFileSetFilePointer call. This is cleared when SFileSetFilePointer is called or when finished reading the block

        public int dwBytesRead; //138// Total bytes read from the current block in the open file. This is cleared when SFileSetFilePointer is called or when finished reading the block
        public int dwBufferSize; //13C// Size of the read buffer for file. This is cleared when SFileSetFilePointer is called or when finished reading the block
        public int dwRefCount; //140// Count of references to this open file
        // Extra struct members used by SFmpq
        public HASHTABLEENTRY.ByReference lpHashEntry;
        public String lpFileName;

        protected List<String> getFieldOrder() {
                return Arrays.asList("lpNextFile", "lpPrevFile", "szFileName", "hFile", "lpParentArc", "lpBlockEntry",
                        "dwCryptKey", "dwFilePointer", "dwUnk", "dwBlockCount", "lpdwBlockOffsets", "dwReadStarted",
                        "bStreaming", "lpLastReadBlock", "dwBytesRead", "dwBufferSize", "dwRefCount", "lpHashEntry",
                        "lpFileName");
        }

        public static class ByReference extends MPQFILE implements Structure.ByReference {}
}
