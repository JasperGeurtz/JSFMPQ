package org.jasperge.chk.sections;

import org.jasperge.chk.CHKSection;
import org.jasperge.chk.enums.PlayerRace;

public class PlayerRaceSection extends CHKSection {
    public PlayerRaceSection(CHKSection section) {
        super(section);
    }

    public PlayerRace[] getPlayerRaces() {
        PlayerRace[] playerRaces = new PlayerRace[12];
        for (int i=0; i < 12; i++) {
            playerRaces[i] = PlayerRace.withID(data[i]);
        }
        return playerRaces;
    }
}
