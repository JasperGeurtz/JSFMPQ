package org.jasperge.mpq;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.jasperge.sfmpq.*;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.jasperge.sfmpq.SFMPQ.*;
import static org.junit.Assert.*;

public class SFMPQTest {
    String archivePath = new File("src/test/resources/patch_rt.mpq").getAbsolutePath();
    String filePath = new File("src/test/resources/stat_txt.tbl").getAbsolutePath();
    String hotkeyFileName = "rez\\stat_txt.tbl";
    SFMPQWrapper sfmpq = new SFMPQWrapper();

    @Test
    public void testExceptionName() {
        try {
            sfmpq.openArchive("nonsense", 0, 0);
        } catch (MPQException e) {
            assertTrue(e.toString().contains("openArchive"));
        }
    }

//    @Test
//    public void testAppending() throws MPQException {
//        assertEquals(2.0, sfmpq.getVersionFloat(), 0.0001);
//        assertEquals("ShadowFlare MPQ API Library v1.08", sfmpq.getVersionString());
//
//        SFMPQVERSION v = sfmpq.getVersionStruct();
//        assertArrayEquals(new short[]{1, 0 , 8, 1}, new short[]{v.Major, v.Minor, v.Revision, v.Subrevision});
//
//        Pointer archive = sfmpq.openArchiveForUpdate(archivePath, MOAU_OPEN_EXISTING, 262144);
//        System.out.println(archive);
//        sfmpq.addFileToArchive(archive, filePath, "rez\\stat_txt.tbl", MAFA_REPLACE_EXISTING);
//        sfmpq.closeUpdatedArchive(archive);
//    }

    @Test
    public void testArchive() throws MPQException {
        Pointer archivePtr = sfmpq.openArchive(archivePath, 0, SFILE_OPEN_HARD_DISK_FILE);
        MPQARCHIVE archive = new MPQARCHIVE.ByReference(archivePtr);

        assertEquals(new String(archive.szFileName).trim(), archivePath);

        //System.out.println(a);
        List<FILELISTENTRY> files = sfmpq.listFiles(archivePtr, null);

        assertTrue(files.stream().map(m -> (new String(m.szFileName)).trim())
                .anyMatch(n -> n.equals(hotkeyFileName)));

        assertEquals(89, files.size());

        sfmpq.closeArchive(archivePtr);
    }

//    @Test
//    public void testExtract() throws MPQException {
//        Pointer archivePtr = sfmpq.openArchive(archivePath, 0, SFILE_OPEN_HARD_DISK_FILE);
//
//        try {
//            Pointer filePtr = sfmpq.openFileEx(archivePtr, hotkeyFileName, 0);
//            MPQFILE file = new MPQFILE.ByReference(filePtr);
//            assertEquals(hotkeyFileName, file.lpFileName);
//            assertEquals(hotkeyFileName, new String(file.szFileName).trim());
//
//            int fileSize = sfmpq.getFileSize(filePtr, null);
//
//            byte[] buffer = new byte[fileSize];
//            if (fileSize > 0) {
//                sfmpq.readFile(filePtr, buffer, fileSize, new IntByReference(), 0);
//            }
//            sfmpq.closeFile(filePtr);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        sfmpq.closeArchive(archivePtr);
//    }
}
