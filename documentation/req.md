# Requirement Analysis

## Intended use of the software

By using the software, users can organize their emulation libraries and make adding more titles easier.

## Users

This program will only have one user role because it is a desktop application for managing files.

## User Interface Sketch

![UI Sketch](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/UI%20Sketch.jpg)

## Basic functionality

- User can add games, directly or as zip archives
  - Files stay where they are added from (Unless zipped, then they're added to extracted folder)
- User can select games and perform operations on the selected files
  - Move to a single folder
  - Move to subfolders based on console, names of folders can be changed in settings.
  
## Further development

- Duplicates
  - Duplicates are detected when filenames are similar (eg Game [!] [PAL].f  Game [E] [3232].f
  - Duplicates can be removed by the user
- Game metadata is fetched from available game databases
- Improved selection
  - Filters such as Console
  - Metadata selection
- Support for more emulation files excluding MAME
  - PSX
    - Converting .emc to .bin and .cue files
    - Generating missing .cue files for .bin files, without generating multiples for [Disk 1] [Disk 2]
