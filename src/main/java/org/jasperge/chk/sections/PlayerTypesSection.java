package org.jasperge.chk.sections;

import org.jasperge.chk.CHKSection;
import org.jasperge.chk.enums.PlayerType;

import java.util.Arrays;

public class PlayerTypesSection extends CHKSection {
    public PlayerTypesSection(CHKSection sec) {
        super(sec);
    }

    public PlayerType[] getPlayerTypes() {
        PlayerType[] playerTypes = new PlayerType[12];
        for (int i=0; i < 12; i++) {
            playerTypes[i] = PlayerType.withID(data[i]);
        }
        return playerTypes;
    }
}
