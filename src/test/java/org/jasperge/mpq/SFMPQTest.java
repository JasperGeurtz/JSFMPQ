package org.jasperge.mpq;

import org.jarsperge.mpq.*;
import org.jarsperge.sfmpq.MPQARCHIVE;
import org.jarsperge.sfmpq.SFMPQVERSION;
import org.junit.*;

import java.io.File;

public class SFMPQTest {
    @Test
    public void testAllMethods() {
        SFMPQWrapper sfmpq = new SFMPQWrapper();

        Assert.assertEquals(2.0, sfmpq.getVersionFloat(), 0.0001);
        Assert.assertEquals("ShadowFlare MPQ API Library v1.08", sfmpq.getVersionString());

        SFMPQVERSION v = sfmpq.getVersionStruct();
        Assert.assertArrayEquals(new short[]{1, 0 , 8, 1}, new short[]{v.Major, v.Minor, v.Revision, v.Subrevision});

        File f = new File("src/test/resources/patch_rt.mpq");
        String path = f.getAbsolutePath();
        System.out.println(path + ": " + f.exists());
        MPQARCHIVE archive = sfmpq.openArchive(path, 0, 0);
        //System.out.println(archive.lpFileName);

        //System.out.println(archive.lpFileName);
    }


}
