package org.jasperge.mpq;

import org.junit.Test;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
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
            mpqEditor.addFile(tbl.getAbsolutePath(), hotkeyFileName);
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
        try ( MPQEditor mpqEditor = new MPQEditor(mpq);) {
            mpqEditor.extractAll(Paths.get("patch_rt"), false);
        }
    }

    @Test
    public void deleteFileMPQTest() throws MPQException {
        String fileName = "arr\\flingy.dat";
        try (MPQEditor e = new MPQEditor(mpq)) {
            assertTrue(e.hasFile(fileName));

            byte[] file = e.extractFileBuffer(fileName);
            System.out.println(Arrays.toString(file));
            e.deleteFile(fileName);
            e.addFileFromBuffer(file, fileName);
            assertTrue(e.hasFile(fileName));
        }
    }
}
