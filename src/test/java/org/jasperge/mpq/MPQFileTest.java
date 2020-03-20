package org.jasperge.mpq;

import org.junit.Test;

import java.io.File;

public class MPQFileTest {


    @Test
    public void testMPQFile() {
        File f = new File("src/test/resources/patch_rt.mpq");
        assert f.exists();
    }

}
