package org.jasperge.mpq;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class MPQFileTest {
    Path mpq = new File("src/test/resources/patch_rt.mpq").toPath();
    File tbl = new File("src/test/resources/stat_txt.tbl");
    String hotkeyFileName = "rez\\stat_txt.tbl";

    @Test
    public void addToMPQTest() throws MPQException {
        try (MPQEditor mpqEditor = new MPQEditor(mpq)) {
            mpqEditor.addFile(tbl.getAbsolutePath(), hotkeyFileName).
        }
    }

    @Test
    public void extractMPQTest() throws IOException {
        try (MPQEditor mpqEditor = new MPQEditor(mpq)) {
            List<MPQFile> files = mpqEditor.getFiles();
            System.out.println(files.stream().map(f -> f.name).sorted().collect(Collectors.joining("\n")));

            mpqEditor.extractFile(hotkeyFileName, null);
        }
    }

    @Test
    public void extractAllMPQTest() throws IOException {
        MPQEditor mpqEditor = new MPQEditor(mpq);
        mpqEditor.extractAll("patch_rt");
        mpqEditor.close();
    }

}
