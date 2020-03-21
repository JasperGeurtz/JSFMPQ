package org.jasperge.tbl;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//https://sfsrealm.hopto.org/tblspecs.html
public class TBLTools {
    private static Charset charSet = StandardCharsets.ISO_8859_1;

    private TBLTools() {} // Utility class


    private static int toUnsignedShort(byte a, byte b) {
        return ((b & 0xFF) << 8) | (a & 0xFF);
    }

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
        for (byte b : bytes) {
            ext += (b == (byte)'<') ? 1 : 0;
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
        System.out.println(new String(out, charSet));
        return out;
    }

    public static List<String> decompile(Path filePath) throws IOException {
        return decompile(Files.readAllBytes(filePath));
    }

    public static List<String> decompile(byte[] data) {
        List<String> arr = new ArrayList<>(Arrays.asList(TBLConstants.refTable));
        int n = toUnsignedShort(data[0], data[1]);
        System.out.println("length: " + n);
        int[] offsets = new int[n+1];
        for (int i=0; i < n; i++) {
            offsets[i] = toUnsignedShort(data[2 + 2*i], data[2 + 2*i + 1]);
        }
        System.out.println("offsets: " + Arrays.toString(offsets));
        offsets[n] = data.length;
        for (int i=0; i < n; i++) {
            String string = decompileString(data, offsets[i], offsets[i+1] - offsets[i]);
            arr.add(string);
        }
        return arr;
    }

    public static boolean decompileToDisk(Path inputFilePath, Path outputFilePath) throws IOException {
        List<String> decompiled = decompile(inputFilePath);
        try (FileWriter fw = new FileWriter(outputFilePath.toString())) {
            for (String str : decompiled) {
                fw.write(str + System.lineSeparator());
            }
        }
        return false;
    }

    public static byte[] compile(List<String> textString) {
        List<byte[]> strings = textString.stream()
                .filter(s -> !s.trim().startsWith("#"))
                .map(TBLTools::compileString)
                .collect(Collectors.toList());
        System.out.println("length: " + strings.size());

        return null;
    }


    public static byte[] compile(Path filePath) throws IOException {
        return compile(Files.readAllLines(filePath, charSet));
    }

    public static boolean compileToDisk(Path inputFilePath, Path outputFilePath) {
        return false;
    }
}
