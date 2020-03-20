package org.jarsperge.sfmpq;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.PointerByReference;

/**
 * https://www.eshayne.com/jnaex/index.html?example=15
 * https://sfsrealm.hopto.org/inside_mopaq/chapter4.htm
 */
public interface SFMPQ extends Library {
    static SFMPQ instanciate() {
        String dllName = System.getProperty("sun.arch.data.model").equals("64") ? "SFmpq64" : "SFmpq";
        return Native.load(dllName, SFMPQ.class);
    }

    String MpqGetVersionString(); //LPCSTR SFMPQAPI WINAPI MpqGetVersionString();

    float MpqGetVersion(); //float  SFMPQAPI WINAPI MpqGetVersion();

    /**
     * Displays an about page in a web browser (this has only been tested in Internet Explorer). This is only for the dll version of SFmpq
     */
    void AboutSFMpq(); //void SFMPQAPI WINAPI AboutSFMpq();

    String SFMpqGetVersionString(); //LPCSTR SFMPQAPI WINAPI SFMpqGetVersionString();

    SFMPQVERSION.ByValue SFMpqGetVersion(); // SFMPQVERSION SFMPQAPI WINAPI SFMpqGetVersion();

    /**
     * Returns 0 if the dll version is equal to the version your program was compiled
     * with, 1 if the dll is newer, -1 if the dll is older.
     */
    long SFMpqCompareVersion(); //long SFMPQAPI __inline SFMpqCompareVersion();

    // General error codes
    int MPQ_ERROR_MPQ_INVALID = 0x85200065;
    int MPQ_ERROR_FILE_NOT_FOUND = 0x85200066;
    int MPQ_ERROR_DISK_FULL = 0x85200068; //Physical write file to MPQ failed
    int MPQ_ERROR_HASH_TABLE_FULL = 0x85200069;
    int MPQ_ERROR_ALREADY_EXISTS = 0x8520006A;
    int MPQ_ERROR_BAD_OPEN_MODE = 0x8520006C; //When MOAU_READ_ONLY is used without MOAU_OPEN_EXISTING

    int MPQ_ERROR_COMPACT_ERROR = 0x85300001;

    // MpqOpenArchiveForUpdate flags
    int MOAU_CREATE_NEW = 0x00; //If archive does not exist, it will be created. If it exists, the function will fail
    int MOAU_CREATE_ALWAYS = 0x08; //Will always create a new archive
    int MOAU_OPEN_EXISTING = 0x04; //If archive exists, it will be opened. If it does not exist, the function will fail
    int MOAU_OPEN_ALWAYS = 0x20; //If archive exists, it will be opened. If it does not exist, it will be created
    int MOAU_READ_ONLY = 0x10; //Must be used with MOAU_OPEN_EXISTING. Archive will be opened without write access
    int MOAU_MAINTAIN_ATTRIBUTES = 0x02; //Will be used in a future version to create the (attributes) file
    int MOAU_MAINTAIN_LISTFILE = 0x01; //Creates and maintains a list of files in archive when they are added, replaced, or deleted

    // MpqOpenArchiveForUpdateEx constants
    int DEFAULT_BLOCK_SIZE = 3; // 512 << number = block size
    int USE_DEFAULT_BLOCK_SIZE = 0xFFFFFFFF; // Use default block size that is defined internally

    // MpqAddFileToArchive flags
    int MAFA_EXISTS = 0x80000000; //This flag will be added if not present
    int MAFA_UNKNOWN40000000 = 0x40000000; //Unknown flag
    int MAFA_SINGLEBLOCK = 0x01000000; //File is stored as a single unit rather than being split by the block size
    int MAFA_MODCRYPTKEY = 0x00020000; //Used with MAFA_ENCRYPT. Uses an encryption key based on file position and size
    int MAFA_ENCRYPT = 0x00010000; //Encrypts the file. The file is still accessible when using this, so the use of this has depreciated
    int MAFA_COMPRESS = 0x00000200; //File is to be compressed when added. This is used for most of the compression methods
    int MAFA_COMPRESS2 = 0x00000100; //File is compressed with standard compression only (was used in Diablo 1)
    int MAFA_REPLACE_EXISTING = 0x00000001; //If file already exists, it will be replaced

    // MpqAddFileToArchiveEx compression flags
    int MAFA_COMPRESS_STANDARD = 0x08; //Standard PKWare DCL compression
    int MAFA_COMPRESS_DEFLATE = 0x02; //ZLib's deflate compression
    int MAFA_COMPRESS_BZIP2 = 0x10; //bzip2 compression
    int MAFA_COMPRESS_WAVE = 0x81; //Stereo wave compression
    int MAFA_COMPRESS_WAVE2 = 0x41; //Mono wave compression

    // Flags for individual compression types used for wave compression
    int MAFA_COMPRESS_WAVECOMP1 = 0x80; //Main compressor for stereo wave compression
    int MAFA_COMPRESS_WAVECOMP2 = 0x40; //Main compressor for mono wave compression
    int MAFA_COMPRESS_WAVECOMP3 = 0x01; //Secondary compressor for wave compression

    // ZLib deflate compression level constants (used with MpqAddFileToArchiveEx and MpqAddFileFromBufferEx)
    int Z_NO_COMPRESSION = 0;
    int Z_BEST_SPEED = 1;
    int Z_BEST_COMPRESSION = 9;
    int Z_DEFAULT_COMPRESSION = (-1); //Default level is 6 with current ZLib version

    // MpqAddWaveToArchive quality flags
    int MAWA_QUALITY_HIGH = 1; //Higher compression, lower quality
    int MAWA_QUALITY_MEDIUM = 0; //Medium compression, medium quality
    int MAWA_QUALITY_LOW = 2; //Lower compression, higher quality

    // SFileGetFileInfo flags
    int SFILE_INFO_BLOCK_SIZE = 0x01; //Block size in MPQ
    int SFILE_INFO_HASH_TABLE_SIZE = 0x02; //Hash table size in MPQ
    int SFILE_INFO_NUM_FILES = 0x03; //Number of files in MPQ
    int SFILE_INFO_TYPE = 0x04; //Is MPQHANDLE a file or an MPQ?
    int SFILE_INFO_SIZE = 0x05; //Size of MPQ or uncompressed file
    int SFILE_INFO_COMPRESSED_SIZE = 0x06; //Size of compressed file
    int SFILE_INFO_FLAGS = 0x07; //File flags (compressed, etc.), file attributes if a file not in an archive
    int SFILE_INFO_PARENT = 0x08; //Handle of MPQ that file is in
    int SFILE_INFO_POSITION = 0x09; //Position of file pointer in files
    int SFILE_INFO_LOCALEID = 0x0A; //Locale ID of file in MPQ
    int SFILE_INFO_PRIORITY = 0x0B; //Priority of open MPQ
    int SFILE_INFO_HASH_INDEX = 0x0C; //Hash table index of file in MPQ
    int SFILE_INFO_BLOCK_INDEX = 0x0D; //Block table index of file in MPQ

    // Return values of SFileGetFileInfo when SFILE_INFO_TYPE flag is used
    int SFILE_TYPE_MPQ = 0x01;
    int SFILE_TYPE_FILE = 0x02;

    // SFileListFiles flags
    int SFILE_LIST_MEMORY_LIST = 0x01; // Specifies that lpFilelists is a file list from memory, rather than being a list of file lists
    int SFILE_LIST_ONLY_KNOWN = 0x02; // Only list files that the function finds a name for
    int SFILE_LIST_ONLY_UNKNOWN = 0x04; // Only list files that the function does not find a name for
    int SFILE_LIST_FLAG_UNKNOWN = 0x08; // Use without SFILE_LIST_ONLY_KNOWN or SFILE_LIST_FLAG_UNKNOWN to list all files, but will set dwFileExists to 3 if file's name is not found

    // SFileOpenArchive flags
    int SFILE_OPEN_HARD_DISK_FILE = 0x0000; //Open archive without regard to the drive type it resides on
    int SFILE_OPEN_CD_ROM_FILE = 0x0001; //Open the archive only if it is on a CD-ROM
    int SFILE_OPEN_ALLOW_WRITE = 0x8000; //Open file with write access

    // SFileOpenFileEx search scopes
    int SFILE_SEARCH_CURRENT_ONLY = 0x00; //Used with SFileOpenFileEx; only the archive with the handle specified will be searched for the file
    int SFILE_SEARCH_ALL_OPEN = 0x01; //SFileOpenFileEx will look through all open archives for the file. This flag also allows files outside the archive to be used

    // Storm functions implemented by this library
    boolean SFileOpenArchive(String lpFileName, int dwPriority, int dwFlags, PointerByReference hMPQ);

    boolean SFileCloseArchive(Pointer hMPQ);

    boolean SFileOpenFileAsArchive(Pointer hSourceMPQ, Pointer lpFileName, int dwPriority, int dwFlags, Pointer hMPQ);

    boolean SFileGetArchiveName(Pointer hMPQ, Pointer lpBuffer, int dwBufferLength);

    boolean SFileOpenFile(String lpFileName, PointerByReference hFile);

    boolean SFileOpenFileEx(Pointer hMPQ, Pointer lpFileName, int dwSearchScope, Pointer hFile);

    boolean SFileCloseFile(Pointer hFile);

    int SFileGetFileSize(Pointer hFile, Pointer lpFileSizeHigh);

    boolean SFileGetFileArchive(Pointer hFile, Pointer hMPQ);

    boolean SFileGetFileName(Pointer hFile, Pointer lpBuffer, int dwBufferLength);

    int SFileSetFilePointer(Pointer hFile, long lDistanceToMove, Pointer lplDistanceToMoveHigh, int dwMoveMethod);

    boolean SFileReadFile(Pointer hFile, Pointer lpBuffer, int nNumberOfBytesToRead, Pointer lpNumberOfBytesRead, int lpOverlapped); //NOTE: set overLapped to 0

    int SFileSetLocale(int nNewLocale);

    boolean SFileGetBasePath(Pointer lpBuffer, int dwBufferLength);

    boolean SFileSetBasePath(Pointer lpNewBasePath);

    // Extra storm-related functions
    int SFileGetFileInfo(Pointer hFile, int dwInfoType);

    boolean SFileSetArchivePriority(Pointer hMPQ, int dwPriority);

    int SFileFindMpqHeader(Pointer hFile);

    boolean SFileListFiles(Pointer hMPQ, Pointer lpFileLists, Pointer lpListBuffer, int dwFlags);

    // Archive editing functions implemented by this library
    Pointer MpqOpenArchiveForUpdate(String lpFileName, int dwFlags, int dwMaximumFilesInArchive);

    int MpqCloseUpdatedArchive(Pointer hMPQ, int dwUnknown2);

    boolean MpqAddFileToArchive(Pointer hMPQ, String lpSourceFileName, String lpDestFileName, int dwFlags);

    boolean MpqAddWaveToArchive(Pointer hMPQ, String lpSourceFileName, String lpDestFileName, int dwFlags, int dwQuality);

    boolean MpqRenameFile(Pointer hMPQ, String lpcOldFileName, String lpcNewFileName);

    boolean MpqDeleteFile(Pointer hMPQ, String lpFileName);

    boolean MpqCompactArchive(Pointer hMPQ);

    // Extra archive editing functions
    Pointer MpqOpenArchiveForUpdateEx(Pointer lpFileName, int dwFlags, int dwMaximumFilesInArchive, int dwBlockSize);

    boolean MpqAddFileToArchiveEx(Pointer hMPQ, Pointer lpSourceFileName, Pointer lpDestFileName, int dwFlags, int dwCompressionType, int dwCompressLevel);

    boolean MpqAddFileFromBufferEx(Pointer hMPQ, Pointer lpBuffer, int dwLength, Pointer lpFileName, int dwFlags, int dwCompressionType, int dwCompressLevel);

    boolean MpqAddFileFromBuffer(Pointer hMPQ, Pointer lpBuffer, int dwLength, Pointer lpFileName, int dwFlags);

    boolean MpqAddWaveFromBuffer(Pointer hMPQ, Pointer lpBuffer, int dwLength, Pointer lpFileName, int dwFlags, int dwQuality);

    boolean MpqRenameAndSetFileLocale(Pointer hMPQ, Pointer lpcOldFileName, Pointer lpcNewFileName, int nOldLocale, int nNewLocale);

    boolean MpqDeleteFileWithLocale(Pointer hMPQ, Pointer lpFileName, int nLocale);

    boolean MpqSetFileLocale(Pointer hMPQ, Pointer lpFileName, int nOldLocale, int nNewLocale);
}
