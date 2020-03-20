package org.jasperge.mpq;

import com.sun.jna.Pointer;
import org.jasperge.sfmpq.MPQARCHIVE;
import org.jasperge.sfmpq.SFMPQVERSION;
import org.junit.*;

import java.io.File;

import static org.jasperge.sfmpq.SFMPQ.*;

public class SFMPQTest {
    String archivePath = new File("src/test/resources/patch_rt.mpq").getAbsolutePath();
    String filePath = new File("src/test/resources/stat_txt.tbl").getAbsolutePath();

    @Test
    public void testAppending() {
        SFMPQWrapper sfmpq = new SFMPQWrapper();

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
        SFMPQWrapper sfmpq = new SFMPQWrapper();
        Pointer archive = sfmpq.openArchive(archivePath, 0, 0);

        MPQARCHIVE a = new MPQARCHIVE.ByReference(archive);

        Assert.assertEquals(new String(a.szFileName).trim(), archivePath);

        System.out.println(a);
    }


}
