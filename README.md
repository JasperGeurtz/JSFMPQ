# JSFMPQ

Java library to 
 - read and modify StarCraft:Broodwar **MPQ**'s (archiving file format used by Blizzard games)
 - extract files contained in those MPQ's
 - decompile & compile **TBL** files (Starcraft text string)

(Uses [SFmpqapi ](https://sfsrealm.hopto.org/dwnload.html#SFmpqapi) internally to manipulate the MPQ's)


### MPQ
This library offers 3 layers of abstraction to manipulate MPQ's:

 - `MPQEditor`: highest level, offers easy and simple manipulation of MPQ archives (enough for most use cases)
 - `SFMPQ`: lowest level, pure C-style calls, only use if you know what you are doing
 - `SFMPQWrapper`: middle level, still pretty lowlevel, but has a more intuitive interface for Java programmers
 
### TBL
  - `TBLTools.decompile` tbl→txt
  - `TBLTools.compile` txt→tbl
 
 
### Installation
 - Add to your project tools via JitPack: https://jitpack.io/#JasperGeurtz/JSFMPQ
 - Make sure `SFmpq.dll` or `SFmpq64.dll` are on your path/same directory (until I figure out a way to do this more elegantly)
 - Make sure `Listfile.txt` is in the same directory (until I figure out a way to do this more elegantly)

### Example Usage

Modifying hotkeys for StarCraft:Broodwar
 ```Java
class CustomHotkeys {
    public static void main(String[] args) {
        String patchMPQ = new File("patch_rt.mpq").getAbsolutePath();
        MPQEditor editor = new MPQEditor(patchMPQ);

        String hotkeysFileName = "rez\\stat_txt.tbl";
        assert editor.getFiles().stream() //make sure the file is present
                .anyMatch(n -> n.name.equals(keysFileName));


        ByteBuffer keysBuf = editor.extractToBuffer(keysFileName);

    }
}
```


 
 ### Command-Line Usage
 ```bash
# TODO
```