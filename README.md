# Lizzy project template

This is a project template for a greenfield Java project. Its chatbot is named _Lizzy_. Given below are instructions on how to use it.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/lizzy/Lizzy.java` file, right-click it, and choose `Run Lizzy.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see something like the below as the output:
   ```
       __    _
      / /   (_)_______  __  __
     / /   / /_  /_  / / / / /
    / /___/ / / /_/ /_/ /_/ /
   /_____/_/ /___/___/\__, /
                     /____/
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Creating and running the executable JAR

With JDK 25 selected, create Lizzy's self-contained executable JAR with:

```sh
./gradlew clean shadowJar
```

Gradle writes the fat JAR to `build/libs/lizzy.jar`. Copy that file to an empty folder and run it from that folder:

```sh
java -jar "lizzy.jar"
```

The JAR stores Lizzy's task data relative to the folder from which it is run, so keeping the JAR in its own folder keeps its data separate from the project files.
