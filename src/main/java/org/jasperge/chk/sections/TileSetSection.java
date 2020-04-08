package org.jasperge.chk.sections;

import org.jasperge.chk.CHKSection;
import org.jasperge.chk.enums.TileSet;

public class TileSetSection extends CHKSection {
    private static final byte MASK = 0b111;
    public TileSetSection(CHKSection section) {
        super(section);
    }

    public TileSet getTileSet() {
        int u16 = toShort(data, 0);
        return TileSet.withID(u16 & MASK);
    }
}
