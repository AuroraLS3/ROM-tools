# Testing Document

Program is being tested with automated unit and integration tests with JUnit 4 by using [Travis](https://travis-ci.org/Rsl1122/ROM-tools) as Continuous Integration provider.

Quality of the code is monitored using the Maven Checkstyle plugin and by using [SonarCloud](https://sonarcloud.io/dashboard?id=com.djrapitops%3Arom-tools), that provides insights on possible mistakes.

## Unit and Integration tests

Since some functionality depends on changing the state of the frontend many tests initialize a fake frontend and a fake backend so that the state can be accessed.

Where possible, smaller utility components have been tested first individually and then as a part of a larger complex. 

Since some of the program functionality depends on running code asyncronously tests use [Awaitility](https://github.com/awaitility/awaitility) testing framework for concurrent testing. This was recommended by SonarCloud.

Largest integration test is opening the Backend with a fake frontend, with an empty game database saved to a temporary folder. This ensures that any fresh installation will function.

### Coverage

07.05.2018 10:58 +2 GMT

Source | Line Coverage | Branch Coverage
-- | -- | --
Jacoco report | 93% | 82%
Sonar | 91.8% | 83.0%

Currently some of the functionality related to parsing files into games is not tested as that is likely to change in the near future when metadata is fetched from the internet.

## Manual testing

Game parsing and UI functionality has been tested manually by adding ~2000 Atari 2600 games as a zip file and as extracted folder on Ubuntu 16.04 and Windows 10 machines. 
Additional testing was performed by adding 300 files from my RetroPie collection that were parsed into 234 games (Correct amount).

## Remaining quality issues

- Due to the extremely large amount of roms available not all combinations of files can be tested, and some might not function well with the program. 
- Some file types are used by multiple different console emulators (such as .bin, .cue, .iso) and the console can not be identified without use of a third party game metadata source.
- Some .bin files take a long time because MD5 hashing takes longer on large files.
- Warnings are not displayed to the user
