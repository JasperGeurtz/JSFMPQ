package org.jasperge.mpq;

import org.jasperge.tbl.TBLTools;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TBLToolsTest {

    @Test
    public void testDecompIntoComp() throws IOException {
        Path tbl = new File("src/test/resources/stat_txt.tbl").toPath();
        Path str = new File("src/test/resources/strings.txt").toPath();

        byte[] bytesBefore = Files.readAllBytes(str);
        String str0 = new String(bytesBefore, StandardCharsets.ISO_8859_1);

        List<String> str1 = TBLTools.decompile(tbl);

        //assertEquals(str0, str1);

        byte[] bytesAfter = TBLTools.compile(str1);

        //System.out.println(String.join("\n", decompile(tbl.getPath())));
    }
}
