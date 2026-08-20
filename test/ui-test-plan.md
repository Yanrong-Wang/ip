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

### Add and list a todo
- Aim: Verify that a todo receives the T type marker and can be listed as incomplete.
- Command:
  ```sh
  javac -d _temp/ui-test-classes src/main/java/*.java && java -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  todo read book
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
  Another matter to keep track of:
    [T][ ] read book
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][ ] read book
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Add, update, and list dated tasks
- Aim: Verify that deadlines and events retain their date/time text and task type markers after status updates.
- Command:
  ```sh
  javac -d _temp/ui-test-classes src/main/java/*.java && java -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  deadline submit report /by Sunday
  event project meeting /from Mon 2pm /to 4pm
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
  A deadline, then. We'd better not keep it waiting.
    [D][ ] submit report (by: Sunday)
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I've added it to your list:
    [E][ ] project meeting (from: Mon 2pm to: 4pm)
  Now you have 2 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Very good! That is one matter settled:
    [D][X] submit report (by: Sunday)
  ____________________________________________________________
  ____________________________________________________________
  Ah, it seems this matter is not quite settled:
    [D][ ] submit report (by: Sunday)
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[D][ ] submit report (by: Sunday)
  2.[E][ ] project meeting (from: Mon 2pm to: 4pm)
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```
