package org.jasperge.mpq;

import com.sun.jna.Pointer;
import org.jasperge.sfmpq.SFMPQVERSION;
import org.junit.*;

import java.io.File;

import static org.jasperge.sfmpq.SFMPQ.MAFA_REPLACE_EXISTING;
import static org.jasperge.sfmpq.SFMPQ.MOAU_OPEN_EXISTING;

public class SFMPQTest {
    @Test
    public void testAllMethods() {
        SFMPQWrapper sfmpq = new SFMPQWrapper();

        Assert.assertEquals(2.0, sfmpq.getVersionFloat(), 0.0001);
        Assert.assertEquals("ShadowFlare MPQ API Library v1.08", sfmpq.getVersionString());

        SFMPQVERSION v = sfmpq.getVersionStruct();
        Assert.assertArrayEquals(new short[]{1, 0 , 8, 1}, new short[]{v.Major, v.Minor, v.Revision, v.Subrevision});

        File f = new File("src/test/resources/patch_rt.mpq");
        String archivePath = f.getAbsolutePath();
        f = new File("src/test/resources/stat_txt.tbl");
        String filePath = f.getAbsolutePath();
        System.out.println(archivePath + ": " + f.exists());
        System.out.println(filePath + ": " + f.exists());

        Pointer archive = sfmpq.openArchiveForUpdate(archivePath, MOAU_OPEN_EXISTING, 262144);
        System.out.println(archive);
        boolean ret = sfmpq.addFileToArchive(archive, filePath, "rez\\stat_txt.tbl", MAFA_REPLACE_EXISTING);
        System.out.println(ret);
        int closed = sfmpq.closeUpdatedArchive(archive);
        System.out.println(closed);
    }


}
