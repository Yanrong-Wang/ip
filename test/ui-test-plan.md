# UI Test Plan

Run this plan with Java 25 active:

```sh
sdk use java 25.0.3.fx-zulu
python3 .codex/skills/test-ui/scripts/run_ui_tests.py test/ui-test-plan.md
```

The command in each case compiles the current sources into a temporary, ignored directory, then starts a fresh Lizzy session. Expected output is exact except for line-ending normalization.

The invalid-input cases below define the expected validation behaviour: Lizzy should explain the problem, continue accepting commands, and leave the existing task list unchanged.

## Test cases

### Exit politely
- Aim: Verify that Lizzy starts, recognises `bye`, and prints its farewell.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
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
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
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
  Here comes another matter to keep track of:
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
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
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

### List an empty task list
- Aim: Verify that listing before any task is added is safe and does not display phantom tasks.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
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
  Here are the tasks in your list:
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Preserve task state after an invalid task number
- Aim: Verify that a valid status update is retained while an intervening out-of-range task number is rejected without changing the task list.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  todo revise notes
  mark 1
  mark 2
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
  Here comes another matter to keep track of:
    [T][ ] revise notes
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Very good! That is one matter settled:
    [T][X] revise notes
  ____________________________________________________________
  ____________________________________________________________
  That task seems to exist only in your imagination.
  Choose a task number from 1 to 1.
  ____________________________________________________________
  ____________________________________________________________
  Ah, it seems this matter is not quite settled:
    [T][ ] revise notes
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][ ] revise notes
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Preserve valid tasks around malformed dated commands
- Aim: Verify that malformed deadline and event commands are rejected and do not consume task-list positions between valid commands.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  todo review lecture
  deadline submit assignment
  todo prepare slides
  event consultation /from Tue 2pm
  deadline submit assignment /by Friday
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
  Here comes another matter to keep track of:
    [T][ ] review lecture
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Something seems to be missing from this deadline.
  Use: deadline <description> /by <date>.
  ____________________________________________________________
  ____________________________________________________________
  Here comes another matter to keep track of:
    [T][ ] prepare slides
  Now you have 2 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  This event appears to be missing part of its arrangement.
  Use: event <description> /from <start> /to <end>.
  ____________________________________________________________
  ____________________________________________________________
  A deadline, then. We'd better not keep it waiting.
    [D][ ] submit assignment (by: Friday)
  Now you have 3 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][ ] review lecture
  2.[T][ ] prepare slides
  3.[D][ ] submit assignment (by: Friday)
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Reject malformed commands while retaining valid task state
- Aim: Verify that blank, unknown, malformed task-creation, malformed status, and extra-argument commands are all rejected without changing valid tasks added before or after them.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text

  todo read book
  blah
  todo
  deadline submit report
  deadline submit report /by Friday
  event meeting /from Monday
  event meeting /from Monday /to Tuesday
  mark first
  mark 4
  unmark 2
  list later
  bye later
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
  Silence may be elegant, but it gives me very little to work with.
  Try: todo <description>, list, or another command.
  ____________________________________________________________
  ____________________________________________________________
  Here comes another matter to keep track of:
    [T][ ] read book
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  I'm afraid "blah" is quite beyond my acquaintance.
  Try todo, deadline, event, list, mark, unmark, delete, or bye.
  ____________________________________________________________
  ____________________________________________________________
  A task with nothing to do is hardly a task at all.
  Use: todo <description>.
  ____________________________________________________________
  ____________________________________________________________
  Something seems to be missing from this deadline.
  Use: deadline <description> /by <date>.
  ____________________________________________________________
  ____________________________________________________________
  A deadline, then. We'd better not keep it waiting.
    [D][ ] submit report (by: Friday)
  Now you have 2 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  This event appears to be missing part of its arrangement.
  Use: event <description> /from <start> /to <end>.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I've added it to your list:
    [E][ ] meeting (from: Monday to: Tuesday)
  Now you have 3 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  I'm afraid that will not quite do; I need a proper task number.
  Use: mark <task number>.
  ____________________________________________________________
  ____________________________________________________________
  That task seems to exist only in your imagination.
  Choose a task number from 1 to 3.
  ____________________________________________________________
  ____________________________________________________________
  Ah, it seems this matter is not quite settled:
    [D][ ] submit report (by: Friday)
  ____________________________________________________________
  ____________________________________________________________
  A list requires no further instruction.
  Use: list.
  ____________________________________________________________
  ____________________________________________________________
  One farewell at a time, if you please.
  Use: bye.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][ ] read book
  2.[D][ ] submit report (by: Friday)
  3.[E][ ] meeting (from: Monday to: Tuesday)
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Reject status updates on an empty list
- Aim: Verify that missing task numbers, empty-list updates, and out-of-range updates do not prevent a later valid status update.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  mark 1
  unmark 1
  todo write report
  unmark
  unmark 0
  mark 1
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
  There is very little to mark when the list is entirely empty.
  Add a task first.
  ____________________________________________________________
  ____________________________________________________________
  There is very little to unmark when the list is entirely empty.
  Add a task first.
  ____________________________________________________________
  ____________________________________________________________
  Here comes another matter to keep track of:
    [T][ ] write report
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  I'm afraid that will not quite do; I need a proper task number.
  Use: unmark <task number>.
  ____________________________________________________________
  ____________________________________________________________
  That task seems to exist only in your imagination.
  Choose a task number from 1 to 1.
  ____________________________________________________________
  ____________________________________________________________
  Very good! That is one matter settled:
    [T][X] write report
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][X] write report
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Delete tasks and retain the reindexed list
- Aim: Verify that delete removes the requested task, preserves the remaining task state, reindexes later tasks, and rejects invalid deletion requests.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  delete 1
  todo read book
  deadline submit report /by Friday
  event project meeting /from Aug 6th 2pm /to 4pm
  mark 3
  delete 2
  list
  delete 3
  delete first
  delete 1
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
  There is very little to delete when the list is entirely empty.
  Add a task first.
  ____________________________________________________________
  ____________________________________________________________
  Here comes another matter to keep track of:
    [T][ ] read book
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  A deadline, then. We'd better not keep it waiting.
    [D][ ] submit report (by: Friday)
  Now you have 2 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I've added it to your list:
    [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
  Now you have 3 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Very good! That is one matter settled:
    [E][X] project meeting (from: Aug 6th 2pm to: 4pm)
  ____________________________________________________________
  ____________________________________________________________
  That matter is off the list:
    [D][ ] submit report (by: Friday)
  You now have 2 tasks on your list.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][ ] read book
  2.[E][X] project meeting (from: Aug 6th 2pm to: 4pm)
  ____________________________________________________________
  ____________________________________________________________
  That task seems to exist only in your imagination.
  Choose a task number from 1 to 2.
  ____________________________________________________________
  ____________________________________________________________
  I'm afraid that will not quite do; I need a proper task number.
  Use: delete <task number>.
  ____________________________________________________________
  ____________________________________________________________
  That matter is off the list:
    [T][ ] read book
  You now have 1 tasks on your list.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[E][X] project meeting (from: Aug 6th 2pm to: 4pm)
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Accept flexible whitespace in dated commands
- Aim: Verify that extra spaces around command arguments and date markers are accepted, while deletion still leaves a correctly reindexed list.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  todo   read book
  deadline   submit report    /by    Friday
  event   project meeting   /from    Monday 2pm   /to    4pm
  delete 2
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
  Here comes another matter to keep track of:
    [T][ ] read book
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  A deadline, then. We'd better not keep it waiting.
    [D][ ] submit report (by: Friday)
  Now you have 2 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I've added it to your list:
    [E][ ] project meeting (from: Monday 2pm to: 4pm)
  Now you have 3 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  That matter is off the list:
    [D][ ] submit report (by: Friday)
  You now have 2 tasks on your list.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][ ] read book
  2.[E][ ] project meeting (from: Monday 2pm to: 4pm)
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Save task changes to disk
- Aim: Verify that adding and marking a task rewrites the data file with its latest state.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy && sed -n '1,$p' _temp/ui-test-data/current.txt
  ```
- Inputs:
  ```text
  todo read book
  mark 1
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
  Here comes another matter to keep track of:
    [T][ ] read book
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Very good! That is one matter settled:
    [T][X] read book
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  T | 1 | cmVhZCBib29r
  ```

### Load saved tasks after restarting
- Aim: Verify that todos, deadlines, events, and completion state survive a restart.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && printf 'todo read book\ndeadline return book /by June 6th\nevent project meeting /from Aug 6th 2pm /to 4pm\nmark 1\nbye\n' | java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy >/dev/null && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
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
  Here are the tasks in your list:
  1.[T][X] read book
  2.[D][ ] return book (by: June 6th)
  3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Create a missing data folder
- Aim: Verify that the first task change creates both a missing parent folder and its data file.
- Command:
  ```sh
  rm -rf _temp/ui-test-data/missing-parent && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/missing-parent/nested/lizzy.txt -cp _temp/ui-test-classes Lizzy && test -f _temp/ui-test-data/missing-parent/nested/lizzy.txt && printf 'Data file created.\n'
  ```
- Inputs:
  ```text
  todo first run
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
  Here comes another matter to keep track of:
    [T][ ] first run
  Now you have 1 tasks in the list.
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  Data file created.
  ```

### Recover from malformed saved data
- Aim: Verify that Lizzy explains an invalid storage record and continues with an empty task list.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && printf 'not valid\n' > _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
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
  I couldn't understand the saved task data at line 1.
  Starting with an empty task list for this session.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```

### Continue after a save failure
- Aim: Verify that a file-system error is explained without ending the session or losing the in-memory task.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/not-a-folder && printf 'block' > _temp/ui-test-data/not-a-folder && javac -d _temp/ui-test-classes src/main/java/*.java && java -Dlizzy.data.path=_temp/ui-test-data/not-a-folder/lizzy.txt -cp _temp/ui-test-classes Lizzy
  ```
- Inputs:
  ```text
  todo keep working
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
  I updated your task list, but couldn't save it to _temp/ui-test-data/not-a-folder/lizzy.txt.
  ____________________________________________________________
  ____________________________________________________________
  Here are the tasks in your list:
  1.[T][ ] keep working
  ____________________________________________________________
  ____________________________________________________________
  Bye! I hope our next conversation will be just as agreeable.
  ____________________________________________________________
  ```
