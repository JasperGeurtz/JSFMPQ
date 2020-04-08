package org.jasperge.chk;

import org.jasperge.mpq.MPQEditor;
import org.jasperge.mpq.MPQException;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SCXFileTest {
    Path benzene = new File("src/test/resources/(2)Benzene.scx").toPath();
    Path empire = new File("src/test/resources/(4)Empire of the Sun.scm").toPath();


    public void printMapdata(Path mapPath) throws MPQException {
        try (MPQEditor mpqEditor = new MPQEditor(mapPath)) {
            System.out.println(mpqEditor.getFiles().stream().map(t -> t.name).collect(Collectors.joining(", ")));
            byte[] scenario = mpqEditor.extractFileBuffer("staredit\\scenario.chk");

            CHKFile map = new CHKFile(scenario);

            System.out.println("starcraft version: " + map.getStarCraftVersion());
            System.out.println("player types: " + Arrays.toString(map.getPlayerTypes()));
            System.out.println("tileset: " + map.getTileSet());
            System.out.println("dimensions: " + map.getWidth() + "x" + map.getHeight());
            System.out.println("player races: " + Arrays.toString(map.playerRaces()));
            System.out.println("terrain: \n" + Arrays.stream(map.getTerrain()).map(Arrays::toString).collect(Collectors.joining("\n")));

        }
    }

    @Test
    public void openBenzenTest() throws MPQException {
        printMapdata(benzene);
    }

    @Test //TODO empire crashes>
    public void openEmpireTest() throws MPQException {
        try (MPQEditor mpqEditor = new MPQEditor(empire)) {
            System.out.println(mpqEditor.getFiles());
        }
    }
}
