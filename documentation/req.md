# Requirement Analysis

## Intended use of the software

By using the software, users can organize their emulation libraries and make adding more titles easier.

## User Interface Sketch

![UI Sketch](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/UI%20Sketch.jpg)

## Functionality

- User can add games, directly or as zip archives
  - Files stay where they are added from (Unless zipped, then they're added to extracted folder)
  - Games with multiple files (Such as PSX games) are added as a single entity
- User can select games
  - Games can be searched by name
  - Visible games can be limited with Console filters
- User can perform operations on the selected files
  - Move to a single folder
  - Move to subfolders based on console, names of folders can be changed in settings.

## Further development

- Duplicates
  - Duplicates are detected when filenames are similar (eg Game [!] [PAL].f  Game [E] [3232].f
  - Duplicates can be removed by the user
- Game metadata is fetched from available game databases
- More support for more emulation files excluding MAME
  - PSX
    - Converting .ecm to .bin and .cue files (Will require executing C code, as original unecm script is in C)
    - Generating missing .cue files for .bin files, without generating multiples for [Disk 1] [Disk 2]
