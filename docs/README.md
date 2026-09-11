# Lizzy User Guide

Lizzy is a task tracker for todos, deadlines, events, and tasks that must be
completed within a date period. Dates use the `yyyy-MM-dd` format.

## Add a task to complete within a period

Use `within` for work that can be completed on any date in an inclusive period.

```text
within <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
```

Example:

```text
within collect certificate /from 2026-09-10 /to 2026-09-15
```

Lizzy displays the task with the `[W]` marker. The start and end dates are both
included, so a task with the same start and end date is valid. The end date must
not be earlier than the start date.

## View dated tasks on a date

Use `on` to view deadlines, events, and period tasks relevant on a date.

```text
on <yyyy-MM-dd>
```

For a period task, Lizzy includes every date from its `/from` date through its
`/to` date, inclusive.

## Other task commands

```text
todo <description>
deadline <description> /by <yyyy-MM-dd>
event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
list
find <keyword>
mark <task number>
unmark <task number>
delete <task number>
bye
```
