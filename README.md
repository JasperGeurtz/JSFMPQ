# JSFMPQ


Java library to 
 - read and modify StarCraft:Broodwar MPQ's (might work for other Blizzard games but not guaranteed).
 - 

(Uses [SFmpqapi ](https://sfsrealm.hopto.org/dwnload.html#SFmpqapi))


This library offers 3 layers of abstraction:

 - `MPQEditor`: highest level, offers easy and simple manipulation of MPQ archives
 - `SFMPQ`: lowest level, pure C-style calls, only use if you know what you are doing
 - `SFMPQWrapper`: middle level, still pretty lowlevel, but has a more intuitive interface for Java programmers
 
 
 ### Example: custom hotkeys

 ```Java
class CustomHotkeys {
    public static void main(String[] args) {
        String patchMPQ = new File("patch_rt.mpq").getAbsolutePath();
        MPQEditor editor = new MPQEditor(patchMPQ);

        String keysFileName = "rez\\stat_txt.tbl";
        assert editor.getFiles().stream() //make sure the file is present
                .anyMatch(n -> n.name.equals(keysFileName));


        ByteBuffer keysBuf = editor.extractToBuffer(keysFileName);

    }
}
```