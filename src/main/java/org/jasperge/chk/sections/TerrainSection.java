package org.jasperge.chk.sections;

import org.jasperge.chk.CHKSection;

import java.util.Arrays;

public class TerrainSection extends CHKSection {
    public TerrainSection(CHKSection section) {
        super(section);
    }


    public int[] terrain1D(int terrainLength)  {
        int[] arr = new int[terrainLength];
        for (int i=0; i < size / 2; i += 1) {
            arr[i] = toShort(data, i*2);
        }
        return arr;
    }

    public int[][] terrain2D(int width, int height) {
        int[] array = terrain1D(width * height);
        int[][] array2D = new int[height][width];
        for (int row=0; row < height; row++) {
            System.arraycopy(array, width * row, array2D[row], 0, width);
        }
        return array2D;
    }
}
