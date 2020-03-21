package org.jasperge.mpq;

import com.sun.jna.Pointer;
import org.jasperge.sfmpq.FILELISTENTRY;
import org.jasperge.sfmpq.MPQARCHIVE;
import org.jasperge.sfmpq.SFMPQVERSION;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.jasperge.sfmpq.SFMPQ.*;

public class SFMPQTest {
    String archivePath = new File("src/test/resources/patch_rt.mpq").getAbsolutePath();
    String filePath = new File("src/test/resources/stat_txt.tbl").getAbsolutePath();
    SFMPQWrapper sfmpq = new SFMPQWrapper();

    @Test
    public void testAppending() {
        Assert.assertEquals(2.0, sfmpq.getVersionFloat(), 0.0001);
        Assert.assertEquals("ShadowFlare MPQ API Library v1.08", sfmpq.getVersionString());

        SFMPQVERSION v = sfmpq.getVersionStruct();
        Assert.assertArrayEquals(new short[]{1, 0 , 8, 1}, new short[]{v.Major, v.Minor, v.Revision, v.Subrevision});

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

        Assert.assertEquals(new String(archive.szFileName).trim(), archivePath);

        //System.out.println(a);
        List<FILELISTENTRY> files = sfmpq.listFiles(archivePtr);

        Assert.assertTrue(files.stream().map(m -> (new String(m.szFileName)).trim())
                .anyMatch(n -> n.equals("rez\\stat_txt.tbl")));

        Assert.assertEquals(89, files.size());

        Assert.assertTrue(sfmpq.closeArchive(archivePtr));
    }


}
