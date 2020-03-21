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
    public void testAppending() {
        assertEquals(2.0, sfmpq.getVersionFloat(), 0.0001);
        assertEquals("ShadowFlare MPQ API Library v1.08", sfmpq.getVersionString());

        SFMPQVERSION v = sfmpq.getVersionStruct();
        assertArrayEquals(new short[]{1, 0 , 8, 1}, new short[]{v.Major, v.Minor, v.Revision, v.Subrevision});

        Pointer archive = sfmpq.openArchiveForUpdate(archivePath, MOAU_OPEN_EXISTING, 262144);
        System.out.println(archive);
        boolean ret = sfmpq.addFileToArchive(archive, filePath, "rez\\stat_txt.tbl", MAFA_REPLACE_EXISTING);
        System.out.println(ret);
        int closed = sfmpq.closeUpdatedArchive(archive);
        System.out.println(closed);
    }

    @Test
    public void testArchive() {
        Pointer archivePtr = sfmpq.openArchive(archivePath, 0, SFILE_OPEN_HARD_DISK_FILE);
        MPQARCHIVE archive = new MPQARCHIVE.ByReference(archivePtr);

        assertEquals(new String(archive.szFileName).trim(), archivePath);

        //System.out.println(a);
        List<FILELISTENTRY> files = sfmpq.listFiles(archivePtr);

        assertTrue(files.stream().map(m -> (new String(m.szFileName)).trim())
                .anyMatch(n -> n.equals(hotkeyFileName)));

        assertEquals(89, files.size());

        assertTrue(sfmpq.closeArchive(archivePtr));
    }

    @Test
    public void testExtract() {
        Pointer archivePtr = sfmpq.openArchive(archivePath, 0, SFILE_OPEN_HARD_DISK_FILE);

        try {
            Pointer filePtr = sfmpq.openFileEx(archivePtr, hotkeyFileName, 0);
            MPQFILE file = new MPQFILE.ByReference(filePtr);
            assertEquals(hotkeyFileName, file.lpFileName);
            assertEquals(hotkeyFileName, new String(file.szFileName).trim());

            int fileSize = sfmpq.getFileSize(filePtr, null);

            byte[] buffer = new byte[fileSize];
            if (fileSize > 0) {
                boolean ret = sfmpq.readFile(filePtr, buffer, fileSize, new IntByReference(), 0);
            }
//            try (FileOutputStream fos = new FileOutputStream(hotkeyFileName.replace("\\", "_"))) {
//                if (fileSize != 0) {
//                    fos.write(buffer);
//                }
//            }
            assertTrue(sfmpq.closeFile(filePtr));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(sfmpq.closeArchive(archivePtr));
    }


}
