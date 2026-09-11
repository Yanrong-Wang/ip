# UI Test Plan

Run this plan with Java 25 active:

```sh
sdk use java 25.0.3.fx-zulu
python3 .codex/skills/test-ui/scripts/run_ui_tests.py test/ui-test-plan.md
```

The command in each case compiles the console sources into a temporary, ignored directory, then starts a fresh
Lizzy session. JavaFX classes are excluded because these cases verify the console interface without a JavaFX
module path. Expected output is exact except for line-ending normalization.

The invalid-input cases below define the expected validation behaviour: Lizzy should explain the problem, continue accepting commands, and leave the existing task list unchanged.

## Test cases

### Exit politely
- Aim: Verify that Lizzy starts, recognises `bye`, and prints its farewell.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Add and list a todo
- Aim: Verify that a todo receives the T type marker and can be listed as incomplete.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] read book
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][ ] read book
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Add, update, and list dated tasks
- Aim: Verify that deadlines and events retain their date/time text and task type markers after status updates.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  deadline submit report /by 2026-08-30
  event project meeting /from 2026-08-31 /to 2026-09-01
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] submit report (by: Aug 30 2026)
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I have reserved it a place:
    [E][ ] project meeting (from: Aug 31 2026 to: Sep 1 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Excellent. One less claim upon your attention:
    [D][X] submit report (by: Aug 30 2026)
  ____________________________________________________________
  ____________________________________________________________
  No harm done—this matter wants our attention again:
    [D][ ] submit report (by: Aug 30 2026)
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[D][ ] submit report (by: Aug 30 2026)
  2.[E][ ] project meeting (from: Aug 31 2026 to: Sep 1 2026)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### List an empty task list
- Aim: Verify that listing before any task is added is safe and does not display phantom tasks.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Your list is perfectly untroubled—there is nothing on it.
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Preserve task state after an invalid task number
- Aim: Verify that a valid status update is retained while an intervening out-of-range task number is rejected without changing the task list.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] revise notes
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  Excellent. One less claim upon your attention:
    [T][X] revise notes
  ____________________________________________________________
  ____________________________________________________________
  I cannot find that task in the present list.
  Choose a task number from 1 to 1.
  ____________________________________________________________
  ____________________________________________________________
  No harm done—this matter wants our attention again:
    [T][ ] revise notes
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][ ] revise notes
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Preserve valid tasks around malformed dated commands
- Aim: Verify that malformed deadline and event commands are rejected and do not consume task-list positions between valid commands.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  todo review lecture
  deadline submit assignment
  todo prepare slides
  event consultation /from Tue 2pm
  deadline submit assignment /by 2026-08-28
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] review lecture
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  This deadline is missing either its duty or its date.
  Use: deadline <description> /by <yyyy-MM-dd>.
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] prepare slides
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  This engagement needs a description, a beginning, and an end.
  Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] submit assignment (by: Aug 28 2026)
  You now have 3 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][ ] review lecture
  2.[T][ ] prepare slides
  3.[D][ ] submit assignment (by: Aug 28 2026)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Reject malformed commands while retaining valid task state
- Aim: Verify that blank, unknown, malformed task-creation, malformed status, and extra-argument commands are all rejected without changing valid tasks added before or after them.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text

  todo read book
  blah
  todo
  deadline submit report
  deadline submit report /by 2026-08-28
  event meeting /from Monday
  event meeting /from 2026-08-29 /to 2026-08-30
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  A thoughtful pause, but I still need a command.
  Try: todo <description>, list, or another command.
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] read book
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  I do not believe I know the command "blah"—yet.
  Try: todo, deadline, event, within, list, find, on, mark, unmark, delete, or bye.
  ____________________________________________________________
  ____________________________________________________________
  Even the smallest task needs a description.
  Use: todo <description>.
  ____________________________________________________________
  ____________________________________________________________
  This deadline is missing either its duty or its date.
  Use: deadline <description> /by <yyyy-MM-dd>.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] submit report (by: Aug 28 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  This engagement needs a description, a beginning, and an end.
  Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I have reserved it a place:
    [E][ ] meeting (from: Aug 29 2026 to: Aug 30 2026)
  You now have 3 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Numbers are wonderfully unambiguous; I shall need one here.
  Use: mark <task number>.
  ____________________________________________________________
  ____________________________________________________________
  I cannot find that task in the present list.
  Choose a task number from 1 to 3.
  ____________________________________________________________
  ____________________________________________________________
  No harm done—this matter wants our attention again:
    [D][ ] submit report (by: Aug 28 2026)
  ____________________________________________________________
  ____________________________________________________________
  The list needs no embellishment.
  Use: list.
  ____________________________________________________________
  ____________________________________________________________
  One farewell is quite sufficient, thank you.
  Use: bye.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][ ] read book
  2.[D][ ] submit report (by: Aug 28 2026)
  3.[E][ ] meeting (from: Aug 29 2026 to: Aug 30 2026)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Reject status updates on an empty list
- Aim: Verify that missing task numbers, empty-list updates, and out-of-range updates do not prevent a later valid status update.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  There is nothing to mark while your list is empty.
  Add a task first.
  ____________________________________________________________
  ____________________________________________________________
  There is nothing to unmark while your list is empty.
  Add a task first.
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] write report
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  Numbers are wonderfully unambiguous; I shall need one here.
  Use: unmark <task number>.
  ____________________________________________________________
  ____________________________________________________________
  I cannot find that task in the present list.
  Choose a task number from 1 to 1.
  ____________________________________________________________
  ____________________________________________________________
  Excellent. One less claim upon your attention:
    [T][X] write report
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][X] write report
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Delete tasks and retain the reindexed list
- Aim: Verify that delete removes the requested task, preserves the remaining task state, reindexes later tasks, and rejects invalid deletion requests.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  delete 1
  todo read book
  deadline submit report /by 2026-08-28
  event project meeting /from 2026-08-06 /to 2026-08-06
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  There is nothing to delete while your list is empty.
  Add a task first.
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] read book
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] submit report (by: Aug 28 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I have reserved it a place:
    [E][ ] project meeting (from: Aug 6 2026 to: Aug 6 2026)
  You now have 3 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Excellent. One less claim upon your attention:
    [E][X] project meeting (from: Aug 6 2026 to: Aug 6 2026)
  ____________________________________________________________
  ____________________________________________________________
  Consider that matter dismissed:
    [D][ ] submit report (by: Aug 28 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][ ] read book
  2.[E][X] project meeting (from: Aug 6 2026 to: Aug 6 2026)
  ____________________________________________________________
  ____________________________________________________________
  I cannot find that task in the present list.
  Choose a task number from 1 to 2.
  ____________________________________________________________
  ____________________________________________________________
  Numbers are wonderfully unambiguous; I shall need one here.
  Use: delete <task number>.
  ____________________________________________________________
  ____________________________________________________________
  Consider that matter dismissed:
    [T][ ] read book
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[E][X] project meeting (from: Aug 6 2026 to: Aug 6 2026)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Accept flexible whitespace in dated commands
- Aim: Verify that extra spaces around command arguments and date markers are accepted, while deletion still leaves a correctly reindexed list.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  todo   read book
  deadline   submit report    /by    2026-08-28
  event   project meeting   /from    2026-08-31   /to    2026-09-01
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] read book
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] submit report (by: Aug 28 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I have reserved it a place:
    [E][ ] project meeting (from: Aug 31 2026 to: Sep 1 2026)
  You now have 3 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Consider that matter dismissed:
    [D][ ] submit report (by: Aug 28 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][ ] read book
  2.[E][ ] project meeting (from: Aug 31 2026 to: Sep 1 2026)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Save task changes to disk
- Aim: Verify that adding and marking a task rewrites the data file with its latest state.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy && sed -n '1,$p' _temp/ui-test-data/current.txt
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] read book
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  Excellent. One less claim upon your attention:
    [T][X] read book
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  T | 1 | cmVhZCBib29r
  ```

### Load saved tasks after restarting
- Aim: Verify that todos, deadlines, events, and completion state survive a restart.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && printf 'todo read book\ndeadline return book /by 2026-06-06\nevent project meeting /from 2026-08-06 /to 2026-08-06\nmark 1\nbye\n' | java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy >/dev/null && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][X] read book
  2.[D][ ] return book (by: Jun 6 2026)
  3.[E][ ] project meeting (from: Aug 6 2026 to: Aug 6 2026)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Create a missing data folder
- Aim: Verify that the first task change creates both a missing parent folder and its data file.
- Command:
  ```sh
  rm -rf _temp/ui-test-data/missing-parent && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/missing-parent/nested/lizzy.txt -cp _temp/ui-test-classes lizzy.Lizzy && test -f _temp/ui-test-data/missing-parent/nested/lizzy.txt && printf 'Data file created.\n'
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] first run
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  Data file created.
  ```

### Recover from malformed saved data
- Aim: Verify that Lizzy explains an invalid storage record and continues with an empty task list.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && printf 'not valid\n' > _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  I couldn't understand the saved task data at line 1.
  Starting with an empty task list for this session.
  ____________________________________________________________
  ____________________________________________________________
  Your list is perfectly untroubled—there is nothing on it.
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Continue after a save failure
- Aim: Verify that a file-system error is explained without ending the session or losing the in-memory task.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/not-a-folder && printf 'block' > _temp/ui-test-data/not-a-folder && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/not-a-folder/lizzy.txt -cp _temp/ui-test-classes lizzy.Lizzy
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  I updated your task list, but couldn't save it to _temp/ui-test-data/not-a-folder/lizzy.txt.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[T][ ] keep working
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Parse and format calendar dates
- Aim: Verify strict ISO date parsing, calendar validation, event ordering, and readable output formatting.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  deadline return book /by 2/12/2019
  deadline leap mistake /by 2019-02-29
  event backwards /from 2019-12-03 /to 2019-12-02
  deadline return book /by 2019-12-02
  event workshop /from 2019-12-02 /to 2019-12-03
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  That date has rather defeated me.
  Use dates in yyyy-MM-dd format, for example 2019-10-15.
  ____________________________________________________________
  ____________________________________________________________
  That date has rather defeated me.
  Use dates in yyyy-MM-dd format, for example 2019-10-15.
  ____________________________________________________________
  ____________________________________________________________
  Even the liveliest engagement cannot end before it begins.
  Use an end date on or after the start date.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] return book (by: Dec 2 2019)
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I have reserved it a place:
    [E][ ] workshop (from: Dec 2 2019 to: Dec 3 2019)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[D][ ] return book (by: Dec 2 2019)
  2.[E][ ] workshop (from: Dec 2 2019 to: Dec 3 2019)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Find dated tasks on a date
- Aim: Verify date-based lookup for deadlines and inclusive event ranges, while excluding todos and retaining original task numbers.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  todo buy milk
  deadline submit report /by 2026-09-01
  event conference /from 2026-09-01 /to 2026-09-03
  deadline pay bill /by 2026-09-04
  on
  on 09/01/2026
  on 2026-09-01
  on 2026-09-02
  on 2026-09-05
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] buy milk
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] submit report (by: Sep 1 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  An engagement! I have reserved it a place:
    [E][ ] conference (from: Sep 1 2026 to: Sep 3 2026)
  You now have 3 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] pay bill (by: Sep 4 2026)
  You now have 4 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Tell me which date you wish to inspect.
  Use: on <yyyy-MM-dd>.
  ____________________________________________________________
  ____________________________________________________________
  That date has rather defeated me.
  Use dates in yyyy-MM-dd format, for example 2019-10-15.
  ____________________________________________________________
  ____________________________________________________________
  Here are the matters requiring attention on Sep 1 2026:
  2.[D][ ] submit report (by: Sep 1 2026)
  3.[E][ ] conference (from: Sep 1 2026 to: Sep 3 2026)
  ____________________________________________________________
  ____________________________________________________________
  Here are the matters requiring attention on Sep 2 2026:
  3.[E][ ] conference (from: Sep 1 2026 to: Sep 3 2026)
  ____________________________________________________________
  ____________________________________________________________
  That date makes no demands upon you: Sep 5 2026.
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Add and view a task within a date period
- Aim: Verify that a period task accepts inclusive dates, appears on each date in its period, is saved in the list, and rejects a reversed period.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  within collect certificate /from 2026-09-10 /to 2026-09-12
  within invalid period /from 2026-09-12 /to 2026-09-10
  on 2026-09-10
  on 2026-09-11
  on 2026-09-13
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  A useful interval; I have noted it:
    [W][ ] collect certificate (within: Sep 10 2026 to: Sep 12 2026)
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  A useful interval must end no earlier than it begins.
  Use an end date on or after the start date.
  ____________________________________________________________
  ____________________________________________________________
  Here are the matters requiring attention on Sep 10 2026:
  1.[W][ ] collect certificate (within: Sep 10 2026 to: Sep 12 2026)
  ____________________________________________________________
  ____________________________________________________________
  Here are the matters requiring attention on Sep 11 2026:
  1.[W][ ] collect certificate (within: Sep 10 2026 to: Sep 12 2026)
  ____________________________________________________________
  ____________________________________________________________
  That date makes no demands upon you: Sep 13 2026.
  ____________________________________________________________
  ____________________________________________________________
  Here is your present list:
  1.[W][ ] collect certificate (within: Sep 10 2026 to: Sep 12 2026)
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```

### Find tasks by keyword
- Aim: Verify that description search finds matching task types in original list order, retains task numbers and completion state, and handles no or missing keywords.
- Command:
  ```sh
  mkdir -p _temp/ui-test-data && rm -f _temp/ui-test-data/current.txt && javac -d _temp/ui-test-classes $(find src/main/java -name '*.java' ! -path '*/gui/*') && java -Dlizzy.data.path=_temp/ui-test-data/current.txt -cp _temp/ui-test-classes lizzy.Lizzy
  ```
- Inputs:
  ```text
  todo read book
  deadline return book /by 2026-09-06
  todo buy milk
  mark 1
  mark 2
  find book
  find pen
  find
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
  Good day! I'm Elizabeth Bennet—Lizzy, if you please.
  Tell me, what shall we set in order?
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] read book
  You now have 1 task in your list.
  ____________________________________________________________
  ____________________________________________________________
  A deadline. Let us give it its proper attention:
    [D][ ] return book (by: Sep 6 2026)
  You now have 2 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Very well—one more matter worth remembering:
    [T][ ] buy milk
  You now have 3 tasks in your list.
  ____________________________________________________________
  ____________________________________________________________
  Excellent. One less claim upon your attention:
    [T][X] read book
  ____________________________________________________________
  ____________________________________________________________
  Excellent. One less claim upon your attention:
    [D][X] return book (by: Sep 6 2026)
  ____________________________________________________________
  ____________________________________________________________
  These tasks answer to your search:
  1.[T][X] read book
  2.[D][X] return book (by: Sep 6 2026)
  ____________________________________________________________
  ____________________________________________________________
  I found no task answering to that description.
  ____________________________________________________________
  ____________________________________________________________
  Give me a word to look for.
  Use: find <keyword>.
  ____________________________________________________________
  ____________________________________________________________
  Goodbye! May your plans prosper—and leave you a little leisure.
  ____________________________________________________________
  ```
