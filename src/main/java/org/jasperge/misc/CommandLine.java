package org.jasperge.misc;

import org.jasperge.mpq.MPQEditor;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;

public class CommandLine {

    static void mpq(Iterator<String> it) {
        if (it.hasNext()) {
            String name = it.next();
            try (MPQEditor e = new MPQEditor(Paths.get(name))) {
                System.out.println(e);

            } catch (Exception e) {
                System.err.println("[-] error loading mpq with name: " + name);
                e.printStackTrace();
            }
        }
        else {
            System.err.println("[-] missing MPQ name: ");
        }
    }


    public static void main(String[] args) {
        System.out.println(Arrays.toString(args));
        Iterator<String> it = Arrays.asList(args).iterator();

        while (it.hasNext()) {
            String s = it.next();
            switch (s) {
                case "mpq":
                    mpq(it);
                    break;
                default:
                    System.err.println("[-] invalid command: " + s);
                    return;
            }
        }

    }
}
