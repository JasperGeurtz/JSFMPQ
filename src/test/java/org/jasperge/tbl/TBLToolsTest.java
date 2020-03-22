package org.jasperge.tbl;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

public class TBLToolsTest {

    @Test
    public void testDecompIntoComp() throws IOException {
        Path tbl = new File("src/test/resources/stat_txt.tbl").toPath();
        Path str = new File("src/test/resources/strings.txt").toPath();

        List<String> str0 = Files.readAllLines(str, TBLTools.charSet);
        List<String> str1 = TBLTools.decompile(tbl);
        byte[] bytesBefore = TBLTools.compile(str1);
        List<String> str2 = TBLTools.decompile(bytesBefore);
        byte[] bytesAfter = TBLTools.compile(str2);

        assertArrayEquals(str0.toArray(), str1.toArray());
        assertArrayEquals(str1.toArray(), str2.toArray());
        assertArrayEquals(bytesBefore, bytesAfter);
    }
}
