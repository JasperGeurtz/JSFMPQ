package org.jasperge.chk;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * http://www.staredit.net/wiki/index.php/Scenario.chk
 */
public class CHKSection {
    protected static Charset charSet = StandardCharsets.ISO_8859_1;

    protected String stringName;
    protected byte[] byteName;
    protected int size;
    protected int location;
    protected byte[] data = null;


    protected int toShort(byte[] data, int i) {
        return (data[i] & 0xFF) | ((data[i + 1] & 0xFF) << 8);
    }
    protected int toInt(byte[] data, int i) {
        return (data[i] & 0xFF) | ((data[i + 1] & 0xFF) << 8) | ((data[i + 2] & 0xFF) << 16) | ((data[i + 3] & 0xFF) << 24);
    }

    public CHKSection(CHKSection section) {
        this.stringName = section.stringName;
        this.byteName = section.byteName;
        this.size =section.size;
        this.location = section.location;
        this.data = section.data;
    }


    public CHKSection(byte[] bytes, int n) {
        location = n;
        byteName = Arrays.copyOfRange(bytes, n, n + 4);
        int strSize = (int)IntStream.range(0, 4).filter(i -> bytes[i] != 0).count();
        byte[] strBytes = new byte[strSize];
        for (int i=0, j=0; i < 4; i++) {
            if (byteName[i] != 0) {
                strBytes[j] = byteName[i];
                j++;
            }
        }
        stringName = new String(strBytes, charSet);
        size = toInt(bytes, n + 4);
        if (size > 0) {
            data = Arrays.copyOfRange(bytes, n + 4 + 4, n + 4 + 4 + size);
        }
    }

    public String getStringName() {
        return stringName;
    }

    public byte[] getByteName() {
        return byteName;
    }

    public byte[] getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    public int getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "CHKSection{" +
                "stringName='" + stringName + '\'' +
                ", byteName=" + Arrays.toString(byteName) +
                ", size=" + size +
                '}';
    }

}
