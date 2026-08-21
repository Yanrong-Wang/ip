import java.util.Scanner;

/**
 * The entry point for the Lizzy chatbot application.
 */
public class Lizzy {
    private static final int TASK_CAPACITY = 100;
    private static final String DIVIDER = "____________________________________________________________";
    private static final String EMPTY_INPUT_ERROR = "Silence may be elegant, but it gives me very little to work with.\n"
            + "Try: todo <description>, list, or another command.";
    private static final String INVALID_TODO_ERROR = "A task with nothing to do is hardly a task at all.\n"
            + "Use: todo <description>.";
    private static final String INVALID_DEADLINE_ERROR = "Something seems to be missing from this deadline.\n"
            + "Use: deadline <description> /by <date>.";
    private static final String INVALID_EVENT_ERROR = "This event appears to be missing part of its arrangement.\n"
            + "Use: event <description> /from <start> /to <end>.";
    private static final String TASK_LIST_FULL_ERROR = "I think the list has quite enough to occupy itself already.\n"
            + "No more tasks can be added.";

    /**
     * Greets the user, stores tasks, updates their status, lists them, and ends on {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = "    __    _\n"
                + "   / /   (_)_______  __  __\n"
                + "  / /   / /_  /_  / / / / /\n"
                + " / /___/ / / /_/ /_/ /_/ /\n"
                + "/_____/_/ /___/___/\\__, /\n"
                + "                  /____/\n";
        System.out.println(DIVIDER);
        System.out.print(banner);
        System.out.println("Hello! I'm Lizzy.");
        System.out.println("What brings you here today?");
        System.out.println(DIVIDER);

        Task[] tasks = new Task[TASK_CAPACITY];
        int numberOfTasks = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine().strip();
            System.out.println(DIVIDER);

            if (command.equals("bye")) {
                System.out.println("Bye! I hope our next conversation will be just as agreeable.");
                System.out.println(DIVIDER);
                break;
            }

            try {
                numberOfTasks = processCommand(command, tasks, numberOfTasks);
            } catch (LizzyException exception) {
                System.out.println(exception.getMessage());
            }
            System.out.println(DIVIDER);
        }
    }

    /**
     * Validates and processes one command without changing the task list on invalid input.
     *
     * @param command the trimmed command entered by the user
     * @param tasks the task storage array
     * @param numberOfTasks the number of occupied entries in {@code tasks}
     * @return the updated number of tasks
     * @throws LizzyException if the command cannot be processed
     */
    private static int processCommand(String command, Task[] tasks, int numberOfTasks) throws LizzyException {
        if (command.isEmpty()) {
            throw new LizzyException(EMPTY_INPUT_ERROR);
        }

        String[] commandParts = command.split("\\s+", 2);
        String action = commandParts[0];
        String argument = commandParts.length == 2 ? commandParts[1].strip() : "";
        switch (action) {
        case "list":
            validateNoArgument(argument, "A list requires no further instruction.", "list");
            printTaskList(tasks, numberOfTasks);
            return numberOfTasks;
        case "todo":
            validateTodo(argument);
            validateTaskCapacity(numberOfTasks);
            tasks[numberOfTasks] = new Todo(argument);
            printAddedTodo(tasks[numberOfTasks], numberOfTasks + 1);
            return numberOfTasks + 1;
        case "deadline":
            String[] deadlineParts = splitExactly(argument, " /by ", INVALID_DEADLINE_ERROR);
            validateDeadline(deadlineParts[0], deadlineParts[1]);
            validateTaskCapacity(numberOfTasks);
            tasks[numberOfTasks] = new Deadline(deadlineParts[0].strip(), deadlineParts[1].strip());
            printAddedDeadline(tasks[numberOfTasks], numberOfTasks + 1);
            return numberOfTasks + 1;
        case "event":
            String[] eventParts = splitExactly(argument, " /from ", INVALID_EVENT_ERROR);
            String[] timeParts = splitExactly(eventParts[1], " /to ", INVALID_EVENT_ERROR);
            validateEvent(eventParts[0], timeParts[0], timeParts[1]);
            validateTaskCapacity(numberOfTasks);
            tasks[numberOfTasks] = new Event(eventParts[0].strip(), timeParts[0].strip(), timeParts[1].strip());
            printAddedEvent(tasks[numberOfTasks], numberOfTasks + 1);
            return numberOfTasks + 1;
        case "mark":
            int markedTaskNumber = parseTaskNumber(argument, "mark");
            validateTaskIndex(markedTaskNumber, numberOfTasks);
            tasks[markedTaskNumber - 1].markAsDone();
            System.out.println("Very good! That is one matter settled:");
            System.out.println("  " + tasks[markedTaskNumber - 1]);
            return numberOfTasks;
        case "unmark":
            int unmarkedTaskNumber = parseTaskNumber(argument, "unmark");
            validateTaskIndex(unmarkedTaskNumber, numberOfTasks);
            tasks[unmarkedTaskNumber - 1].markAsNotDone();
            System.out.println("Ah, it seems this matter is not quite settled:");
            System.out.println("  " + tasks[unmarkedTaskNumber - 1]);
            return numberOfTasks;
        case "bye":
            throw new LizzyException("One farewell at a time, if you please.\nUse: bye.");
        default:
            throw unknownCommand(action);
        }
    }

    /** Prints every task in the current task list. */
    private static void printTaskList(Task[] tasks, int numberOfTasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < numberOfTasks; i++) {
            System.out.println((i + 1) + "." + tasks[i]);
        }
    }

    /** Prints confirmation after adding a todo. */
    private static void printAddedTodo(Task task, int numberOfTasks) {
        System.out.println("Here comes another matter to keep track of:");
        System.out.println("  " + task);
        printTaskCount(numberOfTasks);
    }

    /** Prints confirmation after adding a deadline. */
    private static void printAddedDeadline(Task task, int numberOfTasks) {
        System.out.println("A deadline, then. We'd better not keep it waiting.");
        System.out.println("  " + task);
        printTaskCount(numberOfTasks);
    }

    /** Prints confirmation after adding an event. */
    private static void printAddedEvent(Task task, int numberOfTasks) {
        System.out.println("An engagement! I've added it to your list:");
        System.out.println("  " + task);
        printTaskCount(numberOfTasks);
    }

    /** Prints the current number of stored tasks. */
    private static void printTaskCount(int numberOfTasks) {
        System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
    }

    /** Validates that a todo has a non-empty description. */
    private static void validateTodo(String description) throws LizzyException {
        if (description.isBlank()) {
            throw new LizzyException(INVALID_TODO_ERROR);
        }
    }

    /** Validates that a deadline has both a description and due date. */
    private static void validateDeadline(String description, String date) throws LizzyException {
        if (description.isBlank() || date.isBlank()) {
            throw new LizzyException(INVALID_DEADLINE_ERROR);
        }
    }

    /** Validates that an event has a description, start time, and end time. */
    private static void validateEvent(String description, String start, String end) throws LizzyException {
        if (description.isBlank() || start.isBlank() || end.isBlank()) {
            throw new LizzyException(INVALID_EVENT_ERROR);
        }
    }

    /**
     * Splits an argument around one required separator.
     *
     * @param argument the argument to split
     * @param separator the required separator
     * @param errorMessage the error message for a missing or repeated separator
     * @return exactly two parts surrounding the separator
     * @throws LizzyException if the separator does not occur exactly once
     */
    private static String[] splitExactly(String argument, String separator, String errorMessage) throws LizzyException {
        String[] parts = argument.split(separator, -1);
        if (parts.length != 2) {
            throw new LizzyException(errorMessage);
        }
        return parts;
    }

    /** Parses a task number supplied to {@code mark} or {@code unmark}. */
    private static int parseTaskNumber(String argument, String command) throws LizzyException {
        if (argument.isBlank()) {
            throw invalidTaskNumber(command);
        }
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException exception) {
            throw invalidTaskNumber(command);
        }
    }

    /** Validates that a task number refers to an existing task. */
    private static void validateTaskIndex(int taskNumber, int numberOfTasks) throws LizzyException {
        if (numberOfTasks == 0) {
            throw new LizzyException("There is very little to mark when the list is entirely empty.\nAdd a task first.");
        }
        if (taskNumber < 1 || taskNumber > numberOfTasks) {
            throw new LizzyException("That task seems to exist only in your imagination.\n"
                    + "Choose a task number from 1 to " + numberOfTasks + ".");
        }
    }

    /** Validates that the fixed-size task array has room for another task. */
    private static void validateTaskCapacity(int numberOfTasks) throws LizzyException {
        if (numberOfTasks >= TASK_CAPACITY) {
            throw new LizzyException(TASK_LIST_FULL_ERROR);
        }
    }

    /** Validates that commands which require no argument have not received one. */
    private static void validateNoArgument(String argument, String firstSentence, String command) throws LizzyException {
        if (!argument.isBlank()) {
            throw new LizzyException(firstSentence + "\nUse: " + command + ".");
        }
    }

    /** Creates an exception for an unrecognised command. */
    private static LizzyException unknownCommand(String command) {
        return new LizzyException("I'm afraid \"" + command + "\" is quite beyond my acquaintance.\n"
                + "Try todo, deadline, event, list, mark, unmark, or bye.");
    }

    /** Creates an exception for a missing or malformed task number. */
    private static LizzyException invalidTaskNumber(String command) {
        return new LizzyException("I'm afraid that will not quite do; I need a proper task number.\n"
                + "Use: " + command + " <task number>.");
    }
}
