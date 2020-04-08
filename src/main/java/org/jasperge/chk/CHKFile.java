package org.jasperge.chk;

import org.jasperge.chk.enums.PlayerRace;
import org.jasperge.chk.enums.PlayerType;
import org.jasperge.chk.enums.StarCraftVersion;
import org.jasperge.chk.enums.TileSet;
import org.jasperge.chk.sections.*;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *      u8 for an unsigned byte
 *     u16 for an unsigned short (2-byte integer)
 *     u32 for an unsigned int (4-byte integer)
 *     uT[a] for an array of datatype T (8, 16, 32) with a number of elements
 *     uT[a][b] for a two-dimensional array of datatype T, where each element of T[a] will be an array of size b
 */
public class CHKFile {
    static final String SEC_VERSION = "VER ";
    static final String SEC_PLAYERTYPES = "OWNR";
    static final String SEC_TILESET = "ERA ";
    static final String SEC_DIMENSIONS = "DIM ";
    static final String SEC_PLAYERRACES = "SIDE";
    static final String SEC_TERRAIN = "MTXM";

    byte[] bytes;

    protected Map<String, CHKSection> sections = new TreeMap<>();

    public CHKFile(byte[] bytes) {
        this.bytes = bytes;
        int n = 0;
        while (n < bytes.length) {
            CHKSection sec = new CHKSection(bytes, n);
            switch (sec.stringName) {
                case SEC_VERSION:
                    sec = new StarCraftVersionSection(sec);
                    break;
                case SEC_PLAYERTYPES:
                    sec = new PlayerTypesSection(sec);
                    break;
                case SEC_TILESET:
                    sec = new TileSetSection(sec);
                    break;
                case SEC_DIMENSIONS:
                    sec = new DimensionsSection(sec);
                    break;
                case SEC_PLAYERRACES:
                    sec = new PlayerRaceSection(sec);
                    break;
                case SEC_TERRAIN:
                    sec = new TerrainSection(sec);
                    break;
            }
            sections.put(sec.stringName, sec);
            n += 4 + 4 + (Math.max(sec.size, 0));
        }
    }

    public Map<String, CHKSection> getSections() {
        return new HashMap<>(sections);
    }

    StarCraftVersion getStarCraftVersion() {
        if (sections.containsKey(SEC_VERSION)) {
            return ((StarCraftVersionSection)sections.get(SEC_VERSION)).getVersion();
        }
        throw new IllegalStateException("Map doesn't have a `" + SEC_VERSION + "` section");
    }

    PlayerType[] getPlayerTypes() {
        if (sections.containsKey(SEC_PLAYERTYPES)) {
            return ((PlayerTypesSection)sections.get(SEC_PLAYERTYPES)).getPlayerTypes();
        }
        throw new IllegalStateException("Map doesn't have a `" + SEC_PLAYERTYPES + "` section");
    }

    TileSet getTileSet() {
        if (sections.containsKey(SEC_TILESET)) {
            return ((TileSetSection)sections.get(SEC_TILESET)).getTileSet();
        }
        throw new IllegalStateException("Map doesn't have a `" + SEC_TILESET + "` section");
    }

    int getWidth() {
        if (sections.containsKey(SEC_DIMENSIONS)) {
            return ((DimensionsSection)sections.get(SEC_DIMENSIONS)).getWidth();
        }
        throw new IllegalStateException("Map doesn't have a `" + SEC_DIMENSIONS + "` section");
    }

    int getHeight() {
        if (sections.containsKey(SEC_DIMENSIONS)) {
            return ((DimensionsSection)sections.get(SEC_DIMENSIONS)).getHeight();
        }
        throw new IllegalStateException("Map doesn't have a `" + SEC_DIMENSIONS + "` section");
    }

    PlayerRace[] playerRaces() {
        if (sections.containsKey(SEC_PLAYERRACES)) {
            return ((PlayerRaceSection)sections.get(SEC_PLAYERRACES)).getPlayerRaces();
        }
        throw new IllegalStateException("Map doesn't have a `" + SEC_PLAYERRACES + "` section");
    }

    int[][] getTerrain() {
        if (sections.containsKey(SEC_TERRAIN)) {
            int width = getWidth();
            int height = getHeight();
            return ((TerrainSection)sections.get(SEC_TERRAIN)).terrain2D(width, height);
        }
        throw new IllegalStateException("Map doesn't have a `" + SEC_TERRAIN + "` section");
    }
}
