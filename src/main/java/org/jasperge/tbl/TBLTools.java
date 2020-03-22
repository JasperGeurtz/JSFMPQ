package org.jasperge.tbl;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

//https://sfsrealm.hopto.org/tblspecs.html
public class TBLTools {
    static Charset charSet = StandardCharsets.ISO_8859_1;

    private TBLTools() {} // Utility class

    private static String decompileString(byte[] buffer, int offset, int size) {
        int ext = 0;
        for (int i=0; i < size; i++) {
            byte b = buffer[offset + i];
            if (TBLConstants.reserved.contains(b)) {
                ext += b < 10 ? 2 : 3;
            }
        }
        byte[] bytes = new byte[size + ext];
        int j=0;
        for (int i = 0; i < size; i++) {
            byte b = buffer[offset + i];
            if (TBLConstants.reserved.contains(b)) {
                 bytes[i+j] = (byte)'<';
                 j += 1;
                 for (byte c : ("" + (int) b).getBytes()) {
                     bytes[i+j] = c;
                     j += 1;
                 }
                 bytes[i+j] = (byte)'>';
            }
            else {
                bytes[i+j] = b;
            }
        }
        return new String(bytes, charSet);
    }

    private static byte[] compileString(String str) {
        byte[] bytes = str.getBytes();
        int ext = 0;
        for (int i=0; i < bytes.length; i++) {
            ext += (bytes[i] == (byte)'<') ? ((bytes[i+2] == (byte)'>') ? 2 : 3) : 0;
        }
        byte[] out = new byte[bytes.length - ext];
        int j = 0;
        for (int i=0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (b == (byte)'<') {
                if (bytes[i+2] != (byte)'>') {
                    out[i - j] = Byte.parseByte((char)bytes[i+1] + "" + (char)bytes[i+2]);
                    j += 3;
                    i += 3;
                } else {
                    out[i - j] = Byte.parseByte((char)bytes[i+1] + "");
                    j += 2;
                    i += 2;
                }
            }
            else {
                out[i-j] = b;
            }
        }
        return out;
    }

    public static List<String> decompile(Path filePath) throws IOException {
        return decompile(Files.readAllBytes(filePath), false);
    }

    public static List<String> decompile(Path filePath, boolean withExplanationText) throws IOException {
        return decompile(Files.readAllBytes(filePath), withExplanationText);
    }

    public static List<String> decompile(byte[] data) {
        return decompile(data, false);
    }

    public static List<String> decompile(byte[] data, boolean withExplanationText) {
        List<String> arr = new ArrayList<>();
        if (withExplanationText) {
            arr.addAll(Arrays.asList(TBLConstants.refTable));
        }
        int n = (data[0] & 0xFF) | ((data[1] & 0xFF) << 8); // Unsigned Short (int)
        int[] offsets = new int[n+1];
        for (int i=0; i < n; i++) {
            offsets[i] = (data[2 + 2*i] & 0xFF) | ((data[2 + 2*i + 1] & 0xFF) << 8);
        }
        offsets[n] = data.length;
        for (int i=0; i < n; i++) {
            arr.add(decompileString(data, offsets[i], offsets[i+1] - offsets[i]));
        }
        return arr;
    }

    public static void decompileToDisk(Path inputFilePath, Path outputFilePath) throws IOException {
        decompileToDisk(inputFilePath, outputFilePath, false);
    }

    public static void decompileToDisk(Path inputFilePath, Path outputFilePath, boolean withExplanationText) throws IOException {
        List<String> decompiled = decompile(inputFilePath, withExplanationText);
        try (FileWriter fw = new FileWriter(outputFilePath.toString())) {
            for (String str : decompiled) {
                fw.write(str + System.lineSeparator());
            }
        }
    }

    public static byte[] compile(List<String> textString) {
        byte[][] strings = textString.stream()
                .filter(s -> !s.trim().startsWith("#"))
                .map(TBLTools::compileString)
                .toArray(byte[][]::new);

        if (strings.length == 0) {
            return null;
        }
        int n = strings.length;
        int[] offsets = new int[n];
        offsets[0] = 2 + 2 * n;
        int totalSize = offsets[0];
        for (int i=1; i < n; i++) {
            int len = strings[i-1].length;
            offsets[i] = offsets[i-1] + len;
            totalSize += len;
        }
        totalSize += strings[n-1].length;

        byte[] data = new byte[totalSize];
        data[0] = (byte)(n & 0xFF);
        data[1] = (byte)((n >> 8) & 0xFF);
        for (int i=0; i < n; i++) {
            data[2+i*2] = (byte)(offsets[i] & 0xFF);
            data[2+i*2 + 1] = (byte)((offsets[i] >> 8) & 0xFF);
            System.arraycopy(strings[i], 0, data, offsets[i], strings[i].length);
        }
        return data;
    }

    public static byte[] compile(Path filePath) throws IOException {
        return compile(Files.readAllLines(filePath, charSet));
    }

    public static void compileToDisk(Path inputFilePath, Path outputFilePath) throws IOException {
        byte[] bytes = compile(inputFilePath);
        try (FileOutputStream fos = new FileOutputStream(outputFilePath.toString())) {
            fos.write(bytes);
        }
    }
}
