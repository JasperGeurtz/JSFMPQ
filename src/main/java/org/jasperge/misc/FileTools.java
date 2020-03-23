package org.jasperge.misc;

import org.jasperge.sfmpq.SFMPQ;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class FileTools {
    private FileTools() {} // Utility class

    private static String tmpdir = System.getProperty("java.io.tmpdir");

    public static File copyToTemp(String fileName) {
        File temp = new File(tmpdir, fileName);
        if (!temp.exists()) {
            try (InputStream is = SFMPQ.class.getClassLoader().getResourceAsStream(fileName)) {
                Files.copy(is, temp.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return temp;
    }
}
