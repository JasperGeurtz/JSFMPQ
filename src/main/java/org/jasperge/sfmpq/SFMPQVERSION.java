package org.jasperge.sfmpq;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

public class SFMPQVERSION extends Structure {
    public short Major;
    public short Minor;
    public short Revision;
    public short Subrevision;

    protected List<String> getFieldOrder() {
        return Arrays.asList("Major", "Minor", "Revision", "Subrevision");
    }

    public static class ByValue extends SFMPQVERSION implements Structure.ByValue {}
}
