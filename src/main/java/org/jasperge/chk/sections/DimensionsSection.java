package org.jasperge.chk.sections;

import org.jasperge.chk.CHKSection;

public class DimensionsSection extends CHKSection {
    public DimensionsSection(CHKSection section) {
        super(section);
    }

    public int getWidth() {
        return toShort(data, 0);
    }

    public int getHeight() {
        return toShort(data, 2);
    }
}
