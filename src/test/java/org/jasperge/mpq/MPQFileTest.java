package org.jasperge.mpq;

import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class MPQFileTest {


    @Test
    public void addToMPQTest() {
        File mpq = new File("src/test/resources/patch_rt.mpq");
        File tbl = new File("src/test/resources/stat_txt.tbl");

        MPQEditor mpqArchive = new MPQEditor(mpq.getAbsolutePath());

        assert mpqArchive.addFile(tbl.getAbsolutePath(), "rez\\stat_txt.tbl");
        assert mpqArchive.close();
    }

    @Test
    public void extractMPQTest() {
        File mpq = new File("src/test/resources/patch_rt.mpq");

        MPQEditor mpqArchive = new MPQEditor(mpq.getAbsolutePath());

        List<MPQFile> files = mpqArchive.getFiles();
        System.out.println(files.stream().map(f -> f.name).sorted().collect(Collectors.joining("\n")));
        assert mpqArchive.close();
    }

}
