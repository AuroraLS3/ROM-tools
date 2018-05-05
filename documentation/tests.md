# Testing Document

Program is being tested with automated unit and integration tests with JUnit 4 by using [Travis](https://travis-ci.org/Rsl1122/ROM-tools) as Continuous Integration provider.

Quality of the code is monitored using the Maven Checkstyle plugin and by using [SonarCloud](https://sonarcloud.io/dashboard?id=com.djrapitops%3Arom-tools), that provides insights on possible mistakes.

## Unit and Integration tests

Since some functionality depends on changing the state of the frontend many tests initialize a fake frontend and a fake backend so that the state can be accessed.

Where possible, smaller utility components have been tested first individually and then as a part of a larger complex. 

Since some of the program functionality depends on running code asyncronously tests use [Awaitility](https://github.com/awaitility/awaitility) testing framework for concurrent testing. This was recommended by SonarCloud.

Largest integration test is opening the Backend with a fake frontend, with an empty game database saved to a temporary folder. This ensures that any fresh installation will function.

### Coverage

05.05.2018 17:35 +2 GMT

Source | Line Coverage | Branch Coverage
-- | -- | --
Jacoco report | 92% | 75%
Sonar | 89.4% | 75.9%

Currently some of the functionality related to parsing files into games is not tested as that is likely to change in the near future when metadata is fetched from the internet.

## Manual testing

Game parsing and UI functionality has been tested manually by adding ~2600 Atari 2600 games as a zip file and as extracted folder on Ubuntu 16.04 and Windows 10 machines.

## Remaining quality issues

Currently multi-file games (Like PSX games) are not parsed into a single Game and remain seen as separate entities by the program.
