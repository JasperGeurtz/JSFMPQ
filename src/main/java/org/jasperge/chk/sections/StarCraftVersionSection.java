package org.jasperge.chk.sections;

import org.jasperge.chk.CHKSection;
import org.jasperge.chk.enums.StarCraftVersion;

import static org.jasperge.chk.enums.StarCraftVersion.*;

public class StarCraftVersionSection extends CHKSection {
    public StarCraftVersionSection(CHKSection sec) {
        super(sec);
    }

    public StarCraftVersion getVersion() {
        int v = getRawVersion();
        switch (v) {
            case 59: return StarCraft;
            case 63: return Hybrid;
            case 205: return BroodWar;
        }
        return Unknown;
    }

    public int getRawVersion() {
        return toShort(data, 0);
    }
}
