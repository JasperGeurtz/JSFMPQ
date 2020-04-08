package org.jasperge.chk.enums;

import java.util.Arrays;

public enum  PlayerType {
    Inactive(0),
    Computer_game(1),
    Occupied_by_Human_Player(2),
    Rescue_Passive(3),
    Unused(4),
    Computer(5),
    Human_Open_Slot(6),
    Neutral(7),
    Closed_slot(8);

    public final int id;

    PlayerType(int id) {
        this.id = id;
    }

    public static PlayerType withID(int id) {
        return PlayerType.values()[id];
    }
}
