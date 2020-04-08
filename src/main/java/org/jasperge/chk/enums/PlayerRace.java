package org.jasperge.chk.enums;

public enum PlayerRace {
    Zerg(0),
    Terran(1),
    Protoss(2),
    Invalid_Independent(3),
    Invalid_Neutral(4),
    User_Selectable(5),
    Random(6),
    Inactive(7);
    public final int id;

    PlayerRace(int id) {
        this.id = id;
    }

    public static PlayerRace withID(int id) {
        return PlayerRace.values()[id];
    }
}
