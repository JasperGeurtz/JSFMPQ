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
 - Alternatively compile yourself: `mvn package`

### Example Usage

Modifying hotkeys for StarCraft:Broodwar
 ```Java
public class CustomHotkeys {
    public static void main(String[] args) {
        Path patchMPQ = Paths.get("src/test/resources/patch_rt.mpq");

        // Open MPQ file
        try (MPQEditor editor = new MPQEditor(patchMPQ)) {
            String hotkeysFilename = "rez\\stat_txt.tbl";
            assert editor.hasFile(hotkeysFilename);

            // Extract hotkey file
            byte[] keyBytes = editor.extractFileBuffer(hotkeysFilename);

            // change zergling hotkey from `z` to `m`
            List<String> strings = TBLTools.decompile(keyBytes);
            int idx = strings.indexOf("z<1>Morph to <3>Z<1>erglings<0>");
            strings.set(idx, "m<1>Morph to Zerglings<0><3>M<1>");
            byte[] newKeyBytes = TBLTools.compile(strings);

            // update the hotkey file
            editor.addFileFromBuffer(newKeyBytes, "rez\\stat_txt.tbl");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

### Todo
 - command line application (on request)
 - support more SC:BW formats? (on request)
