package org.jasperge.tbl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class TBLConstants {


    static Set<Byte> reserved = new HashSet<>(Arrays.asList(
            (byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6, (byte)7, (byte)8,
            (byte)9, (byte)10, (byte)11, (byte)12, (byte)14, (byte)15, (byte)16, (byte)17,
            (byte)18, (byte)19, (byte)20, (byte)21, (byte)22, (byte)23, (byte)24, (byte)25,
            (byte)26, (byte)27, (byte)28, (byte)29, (byte)30, (byte)31, (byte)35, (byte)60,
            (byte) 62));

    static String[] refTable = ("#----------------------------------------------------\n" +
            "# Misc.\n" +
            "#    <0> = End Substring\n" +
            "#    <9> = Tab\n" +
            "#   <10> = Newline\n" +
            "#   <18> = Right Align\n" +
            "#   <19> = Center Align\n" +
            "#   <27> = Escape Key\n" +
            "#   <35> = #\n" +
            "#   <60> = <\n" +
            "#   <62> = >\n" +
            "#\n" +
            "# Menu Screen Colors\n" +
            "#    <1> = Cyan\n" +
            "#    <2> = Cyan\n" +
            "#    <3> = Green\n" +
            "#    <4> = Light Green\n" +
            "#    <5> = Grey*\n" +
            "#    <6> = White\n" +
            "#    <7> = Red\n" +
            "#    <8> = Black*\n" +
            "#   <11> = Invisible*\n" +
            "#   <12> = Truncate\n" +
            "#   <14> = Black\n" +
            "#   <15> = Black\n" +
            "#   <16> = Black\n" +
            "#   <17> = Black\n" +
            "#   <20> = Invisible*\n" +
            "#   <21> = Black\n" +
            "#   <22> = Black\n" +
            "#   <23> = Black\n" +
            "#   <24> = Black\n" +
            "#   <25> = Black\n" +
            "#   <26> = Black/Cyan?\n" +
            "#   <27> = Black\n" +
            "#   <28> = Black\n" +
            "#\n" +
            "# In-game Colors\n" +
            "#    <1> = Cyan\n" +
            "#    <2> = Cyan\n" +
            "#    <3> = Yellow\n" +
            "#    <4> = White\n" +
            "#    <5> = Grey*\n" +
            "#    <6> = Red\n" +
            "#    <7> = Green\n" +
            "#    <8> = Red (Player 1)\n" +
            "#   <11> = Invisible*\n" +
            "#   <12> = Truncate\n" +
            "#   <14> = Blue (Player 2)\n" +
            "#   <15> = Teal (Player 3)\n" +
            "#   <16> = Purple (Player 4)\n" +
            "#   <17> = Orange (Player 5)\n" +
            "#   <20> = Invisible*\n" +
            "#   <21> = Brown (Player 6)\n" +
            "#   <22> = White (Player 7)\n" +
            "#   <23> = Yellow (Player 8)\n" +
            "#   <24> = Green (Player 9)\n" +
            "#   <25> = Brighter Yellow (Player 10)\n" +
            "#   <26> = Cyan (Player 12)\n" +
            "#   <27> = Pinkish (Player 11)\n" +
            "#   <28> = Dark Cyan\n" +
            "#   <29> = Greygreen\n" +
            "#   <30> = Bluegrey\n" +
            "#   <31> = Turquiose\n" +
            "#\n" +
            "# Hotkey Types\n" +
            "#    <0> = Label Only, no Requirements\n" +
            "#    <1> = Minerals, Gas, Supply (Unit/Building)\n" +
            "#    <2> = Upgrade Research\n" +
            "#    <3> = Spell\n" +
            "#    <4> = Technology Research\n" +
            "#    <5> = Minerals, Gas (Guardian/Devourer Aspect)\n" +
            "#\n" +
            "# * Starcraft will ignore all color tags after this.\n" +
            "#----------------------------------------------------\n").split("\n");
}
