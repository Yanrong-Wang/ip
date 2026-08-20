# UI Test Plan

Run this plan with Java 25 active:

```sh
sdk use java 25.0.3.fx-zulu
python3 .codex/skills/test-ui/scripts/run_ui_tests.py test/ui-test-plan.md
```

The command in each case compiles the current sources into a temporary, ignored directory, then starts a fresh Lizzy session. Expected output is exact except for line-ending normalization.

## Test cases

### Exit politely
- Aim: Verify that Lizzy starts, recognises `bye`, and prints its farewell.
- Command:
  ```sh
  javac -d _temp/ui-test-classes src/main/java/*.java && java -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  bye
  ```
- Expected output:
  ```text
  ____________________________________________________________
      __    _
     / /   (_)_______  __  __
    / /   / /_  /_  / / / / /
   / /___/ / / /_/ /_/ /_/ /
  /_____/_/ /___/___/\__, /
                    /____/
  Hello! I'm Lizzy.
  What brings you here today?
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Add and list a task
- Aim: Verify that a task can be added and shown as incomplete in the list.
- Command:
  ```sh
  javac -d _temp/ui-test-classes src/main/java/*.java && java -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  read book
  list
  bye
  ```
- Expected output:
  ```text
  ____________________________________________________________
      __    _
     / /   (_)_______  __  __
    / /   / /_  /_  / / / / /
   / /___/ / / /_/ /_/ /_/ /
  /_____/_/ /___/___/\__, /
                    /____/
  Hello! I'm Lizzy.
  What brings you here today?
  ____________________________________________________________
  ____________________________________________________________
  added: read book
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[ ] read book
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Mark and unmark a task
- Aim: Verify that completion status changes are reflected in Lizzy's responses and task list.
- Command:
  ```sh
  javac -d _temp/ui-test-classes src/main/java/*.java && java -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  submit assignment
  mark 1
  unmark 1
  list
  bye
  ```
- Expected output:
  ```text
  ____________________________________________________________
      __    _
     / /   (_)_______  __  __
    / /   / /_  /_  / / / / /
   / /___/ / / /_/ /_/ /_/ /
  /_____/_/ /___/___/\__, /
                    /____/
  Hello! I'm Lizzy.
  What brings you here today?
  ____________________________________________________________
  ____________________________________________________________
  added: submit assignment
  ____________________________________________________________
  ____________________________________________________________
  Very good! That is one matter settled:
    [X] submit assignment
  ____________________________________________________________
  ____________________________________________________________
  Ah, it seems this matter is not quite settled:
    [ ] submit assignment
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[ ] submit assignment
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```
