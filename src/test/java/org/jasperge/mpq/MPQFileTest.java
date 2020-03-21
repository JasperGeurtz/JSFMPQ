package org.jasperge.mpq;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class MPQFileTest {
    File mpq = new File("src/test/resources/patch_rt.mpq");
    File tbl = new File("src/test/resources/stat_txt.tbl");
    String hotkeyFileName = "rez\\stat_txt.tbl";

    @Test
    public void addToMPQTest() {
        MPQEditor mpqEditor = new MPQEditor(mpq.getAbsolutePath());

        assertTrue(mpqEditor.addFile(tbl.getAbsolutePath(), hotkeyFileName));
        assertTrue(mpqEditor.close());
    }

    @Test
    public void extractMPQTest() throws IOException {
        MPQEditor mpqEditor = new MPQEditor(mpq.getAbsolutePath());
        List<MPQFile> files = mpqEditor.getFiles();
        System.out.println(files.stream().map(f -> f.name).sorted().collect(Collectors.joining("\n")));

        assertTrue(mpqEditor.extractFile(hotkeyFileName, null));

        assertTrue(mpqEditor.close());
    }

    @Test
    public void extractAllMPQTest() throws IOException {
        MPQEditor mpqEditor = new MPQEditor(mpq.getAbsolutePath());
        assertTrue(mpqEditor.extractAll("patch_rt"));
        assertTrue(mpqEditor.close());
    }

}
