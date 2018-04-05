# ROM-tools

Toolkit for managing ROMs for different emulators.  

Goal of this program is to aid in managing games for emulated systems, such as Atari 2600, Sega Megadrive, PSX, NES, etc.  

Managing downloaded games can be a lot of unnecessary work - Extracting zips, removing duplicates, unpacking .emc files into .bin and .cue files

## Documentation

[Requirements Analysis](documentation/req.md)

[Hours used](documentation/hours.md)

### Using the software

**Build Rom-tools.jar** to Rom-tools/target
```
mvn install
```

**Run the program** (This will be improved later)
```
mvn compile exec:java -Dexec.mainClass=com.djrapitops.rom.Main
```