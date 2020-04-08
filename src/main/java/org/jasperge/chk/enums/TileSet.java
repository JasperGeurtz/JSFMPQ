package org.jasperge.chk.enums;

public enum  TileSet {
    Badlands(0),
    Space_Platform(1),
    Installation(2),
    Ashworld(3),
    Jungle(4),
    Desert(5),
    Arctic(6),
    Twilight(7);

    public final int id;
    TileSet(int id) {
        this.id = id;
    }

    public static TileSet withID(int id) {
        return TileSet.values()[id];
    }
}
