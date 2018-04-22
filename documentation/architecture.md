# Program Architecture

## Class Diagram

[Commit 1f195b7d448d4901833f47e8683d458b95e2b3bd](https://github.com/Rsl1122/ROM-tools/tree/1f195b7d448d4901833f47e8683d458b95e2b3bd)

![Class Diagram](https://github.com/Rsl1122/ROM-tools/blob/master/documentation/ClassDiagram.jpg?raw=true)

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
