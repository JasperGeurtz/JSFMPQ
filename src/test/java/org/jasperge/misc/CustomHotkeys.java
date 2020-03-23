package org.jasperge.misc;

import org.jasperge.mpq.MPQEditor;
import org.jasperge.tbl.TBLTools;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class CustomHotkeys {
    public static void main(String[] args) {
        Path patchMPQ = Paths.get("src/test/resources/patch_rt.mpq");

        // Open MPQ file
        try (MPQEditor editor = new MPQEditor(patchMPQ)) {
            String hotkeysFilename = "rez\\stat_txt.tbl";
            assert editor.hasFile(hotkeysFilename);

            // Extract hotkey file
            byte[] keyBytes = editor.extractFileBuffer(hotkeysFilename);

            // change zergling hotkey from `z` to `m`
            List<String> strings = TBLTools.decompile(keyBytes);
            int idx = strings.indexOf("z<1>Morph to <3>Z<1>erglings<0>");
            strings.set(idx, "m<1>Morph to Zerglings<0><3>M<1>");
            byte[] newKeyBytes = TBLTools.compile(strings);

            // update the hotkey file
            editor.addFileFromBuffer(newKeyBytes, "rez\\stat_txt.tbl");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
