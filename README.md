# ![ROM Tools](https://github.com/Rsl1122/ROM-tools/blob/master/Rom-tools/src/main/resources/Logo-text.png?raw=true)

[![Build Status](https://travis-ci.org/Rsl1122/ROM-tools.svg?branch=master)](https://travis-ci.org/Rsl1122/ROM-tools)
[![Quality Gate](https://sonarcloud.io/api/badges/gate?key=com.djrapitops%3Arom-tools)](https://sonarcloud.io/dashboard?id=com.djrapitops%3Arom-tools)

Toolkit for managing ROMs for different emulators.  

Goal of this program is to aid in managing games for emulated systems, such as Atari 2600, Sega Megadrive, PSX, NES, etc.  

Managing downloaded games can be a lot of unnecessary work - Extracting zips, removing duplicates, unpacking .emc files into .bin and .cue files

## Documentation

[How to use the Program](https://github.com/Rsl1122/ROM-tools/wiki/Using-the-Program)

[Requirements Analysis](documentation/req.md)

[Architecture](documentation/architecture.md)

[Testing document](documentation/tests.md)

[Hours used](documentation/hours.md)

### Course Required Tools

[Latest Release](https://github.com/Rsl1122/ROM-tools/releases)

#### Build Rom-tools.jar to Rom-tools/target
```
mvn package
```

#### Run the program
Double click the jar to run the program or use the following command:
```
java -jar Rom-tools-1.0-SNAPSHOT.jar
```

#### Checkstyle

While you can run checkstyle via maven, You can also see checkstyle result at the end of [Travis Build log](https://travis-ci.org/Rsl1122/ROM-tools), if no errors are present you can see the following:
```
[INFO] --- maven-checkstyle-plugin:2.17:checkstyle (default-cli) @ rom-tools ---
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

#### Jacoco Test Coverage Report

While you can run jacoco coverage report via maven, SonarCloud uses Jacoco for it's test coverage, so [Test coverage can be viewed on SonarCloud](https://sonarcloud.io/component_measures?id=com.djrapitops%3Arom-tools&metric=coverage&view=tree)

#### Javadoc

Javadoc can be generated with the command and then viewed in target/site/apidocs/index.html
```
mvn javadoc:javadoc
```
