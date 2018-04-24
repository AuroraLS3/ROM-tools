# Program Architecture

## Class Diagram

[Commit 3314b1ca7c9136e7b8c81851c527109304071872](https://github.com/Rsl1122/ROM-tools/tree/3314b1ca7c9136e7b8c81851c527109304071872)

![Class Diagram](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/ClassDiagram2.jpg?raw=true)

Color | Clarification
-- | --
Green|Backend Class
Purple|Database Backend Class
Blue|Frontend Class
Yellow|Data Class
Grey|Interface
White|Misc

## Sequence Diagrams

### Fetching Games from the GameBackend

![Seq Diagram](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/SeqDia-FetchGames.png?raw=true)

This approach allows adding a new operation without needing to touch any code that implements a GameBackend.
