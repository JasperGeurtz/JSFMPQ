package org.jasperge.mpq;

import org.junit.Test;

import java.io.File;

public class MPQFileTest {


    @Test
    public void testMPQFile() {
        File mpq = new File("src/test/resources/patch_rt.mpq");
        File tbl = new File("src/test/resources/stat_txt.tbl");

        MPQEditor mpqArchive = new MPQEditor(mpq.getAbsolutePath());

        mpqArchive.addFile(tbl.getAbsolutePath(), "rez\\stat_txt.tbl");
        mpqArchive.close();
    }

}
