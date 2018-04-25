# Program Architecture

## Package Structure

![Package Diagram](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/PackageDiagram.jpg?raw=true)

ROM tools is divided into 6 main packages, 3 of which are inside backend package.

Package | Contains | Purpose
-- | -- | --
com.djrapitops.rom.**game** |Data classes that represent games handled by the program |Allow easy manipulation and display of metadata that can be linked to files
com.djrapitops.rom.**backend.database** |Persistent storage of game data to a SQLite database |Sufficient abstraction of java.sql utilities so that data storage can be effortlessly expanded
com.djrapitops.rom.**backend.cache** |Database fetch result storage in memory |Cache is maintained so that heavy fetch operations do not need to be performed if the database data has not changed.
com.djrapitops.rom.**backend.processes** |Main program execution logic |Allows easy calls from frontend to perform specific tasks
com.djrapitops.rom.**frontend** |Contains frontend code not related to javafx and javafx implementation of the frontend. |Abstract frontend implementation in addition with State allows swapping frontend in the future if necessary.
com.djrapitops.rom.**util** |Misc. non-domain specific utility classes |The code in util package can be re-used because of the small size of the classes.

### Classes

Last updated: 24.04.2018

![Class Diagram](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/ClassDiagram2.jpg?raw=true)

Color | Clarification
-- | --
Green|Backend Class
Purple|Database Backend Class
Blue|Frontend Class
Yellow|Data Class
Grey|Interface
White|Misc

## Program Logic

### Launch

Execution logic for starting the program is contained inside `Backend`-class. In order to start the program `backend.open(Frontend)` needs to be called.

Backend then calls `MainProcesses` that performs rest of the launch in a separate thread.

### Asyncronous Execution

Any task that performs IO, such as reading from the GameBackend or files, is performed asyncronously.

This is done with Java's `CompletableFuture.supplyAsync`-method, that gives the task of executing code to an `ExecutorService`. This way the UI thread is not taxed with heavy operations - so that the program does not stall while performing operations.

Different tasks in the program are divided into different methods, so that they can be called in succession by chaining the results CompletableFuture gives. 

If you're familiar with JavaScript CompletableFuture is similar to Promises.

## User Interface

![UI 1.0](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/UI-1.0.png?raw=true)

The user interface contains 4 different views. Games, Tools, Settings and *Filters*  

Usage of each view is detailed in the [Using the Program](https://github.com/Rsl1122/ROM-tools/wiki/Using-the-Program)-guide

## Persistent Storage

### games.db

games.db is a SQLite Database that is used to store information about added games, their files and metadata. It will be created next to the program in the same folder where it was launched from.

### ROM files

The program allows adding different emulation files for processing. The files are kept where they were added from, but if the added file is an archive (such as a .zip-file) the contents are extracted to a folder next to the program in a new folder.

## Program Execution

### Fetching Games from the GameBackend

All games are fetched from the GameBackend when the program is launched.  
To do this `Operations.ALL_GAMES.get()` is called. `Operations` stores `Operation<T>` objects that use the `DAO<T>` objects provided to them when they are constucted. 

![Seq Diagram](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/SeqDia-FetchGames.png?raw=true)

This approach allows adding a new operation without needing to touch any code that implements a GameBackend.
