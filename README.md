# Lizzy

Lizzy is a Java 25 desktop task manager for todos, deadlines, events, and
tasks with flexible completion periods. Its JavaFX conversation interface
offers concise command guidance, persistent storage, schedule lookup, and a
warm, lightly witty personality.

See the [Lizzy User Guide](docs/README.md) for installation and every user
command.

## Development setup

1. Install JDK 25 and open this repository in IntelliJ IDEA.
2. Set the project SDK to JDK 25 and the language level to **SDK default**.
3. Run `./gradlew run` to start the graphical interface.

The console interface remains available through `lizzy.Lizzy#main`.

## Build and test

Create the cross-platform fat JAR with:

```sh
./gradlew clean shadowJar
```

The output is `build/libs/lizzy.jar`. Run the full automated checks with:

```sh
./gradlew check
```

## Credits and reuse

- This repository began from the
  [SE-EDU Duke starter project](https://github.com/se-edu/duke) and follows
  its JavaFX tutorial structure. The original contributors remain listed in
  [CONTRIBUTORS.md](CONTRIBUTORS.md).
- The bundled
  [Source Serif 4](https://github.com/adobe-fonts/source-serif) font is
  Copyright 2014–2023 Adobe and is distributed under the SIL Open Font
  License 1.1; its license is included at
  `src/main/resources/fonts/LICENSE.md`.
- The Lizzy and Jane line-art portraits are original AI-assisted artwork
  created for this project using OpenAI image generation.
