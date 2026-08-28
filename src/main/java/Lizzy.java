import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The entry point for the Lizzy chatbot application.
 */
public class Lizzy {
    private static final String DIVIDER = "____________________________________________________________";
    private static final Path DATA_FILE_PATH = Path.of(
            System.getProperty("lizzy.data.path", "data/lizzy.txt"));
    private static final String EMPTY_INPUT_ERROR = "Silence may be elegant, but it gives me very little to work with.\n"
            + "Try: todo <description>, list, or another command.";
    private static final String INVALID_TODO_ERROR = "A task with nothing to do is hardly a task at all.\n"
            + "Use: todo <description>.";
    private static final String INVALID_DEADLINE_ERROR = "Something seems to be missing from this deadline.\n"
            + "Use: deadline <description> /by <yyyy-MM-dd>.";
    private static final String INVALID_EVENT_ERROR = "This event appears to be missing part of its arrangement.\n"
            + "Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.";
    private static final String INVALID_DATE_ERROR = "I couldn't understand that date.\n"
            + "Use dates in yyyy-MM-dd format, for example 2019-10-15.";

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

        Storage storage = new Storage(DATA_FILE_PATH);
        List<Task> tasks;
        try {
            tasks = storage.load();
        } catch (LizzyException exception) {
            tasks = new ArrayList<>();
            System.out.println(exception.getMessage());
            System.out.println(DIVIDER);
        }
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
                processCommand(command, tasks, storage);
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
     * @param tasks the task list to inspect or modify
     * @param storage the storage used to persist task-list changes
     * @throws LizzyException if the command cannot be processed or saved
     */
    private static void processCommand(String command, List<Task> tasks, Storage storage) throws LizzyException {
        if (command.isEmpty()) {
            throw new LizzyException(EMPTY_INPUT_ERROR);
        }

        String[] commandParts = command.split("\\s+", 2);
        String action = commandParts[0];
        String argument = commandParts.length == 2 ? commandParts[1].strip() : "";
        switch (action) {
        case "list":
            validateNoArgument(argument, "A list requires no further instruction.", "list");
            printTaskList(tasks);
            return;
        case "todo":
            validateTodo(argument);
            tasks.add(new Todo(argument));
            storage.save(tasks);
            printAddedTodo(tasks.getLast(), tasks.size());
            return;
        case "deadline":
            String[] deadlineParts = splitExactly(argument, "\\s+/by\\s+", INVALID_DEADLINE_ERROR);
            validateDeadline(deadlineParts[0], deadlineParts[1]);
            LocalDate deadlineDate = parseDate(deadlineParts[1]);
            tasks.add(new Deadline(deadlineParts[0].strip(), deadlineDate));
            storage.save(tasks);
            printAddedDeadline(tasks.getLast(), tasks.size());
            return;
        case "event":
            String[] eventParts = splitExactly(argument, "\\s+/from\\s+", INVALID_EVENT_ERROR);
            String[] timeParts = splitExactly(eventParts[1], "\\s+/to\\s+", INVALID_EVENT_ERROR);
            validateEvent(eventParts[0], timeParts[0], timeParts[1]);
            LocalDate eventStartDate = parseDate(timeParts[0]);
            LocalDate eventEndDate = parseDate(timeParts[1]);
            if (eventEndDate.isBefore(eventStartDate)) {
                throw new LizzyException("An event cannot end before it begins.\n"
                        + "Use an end date on or after the start date.");
            }
            tasks.add(new Event(eventParts[0].strip(), eventStartDate, eventEndDate));
            storage.save(tasks);
            printAddedEvent(tasks.getLast(), tasks.size());
            return;
        case "mark":
            int markedTaskNumber = parseTaskNumber(argument, "mark");
            validateTaskIndex(markedTaskNumber, tasks, "mark");
            tasks.get(markedTaskNumber - 1).markAsDone();
            storage.save(tasks);
            System.out.println("Very good! That is one matter settled:");
            System.out.println("  " + tasks.get(markedTaskNumber - 1));
            return;
        case "unmark":
            int unmarkedTaskNumber = parseTaskNumber(argument, "unmark");
            validateTaskIndex(unmarkedTaskNumber, tasks, "unmark");
            tasks.get(unmarkedTaskNumber - 1).markAsNotDone();
            storage.save(tasks);
            System.out.println("Ah, it seems this matter is not quite settled:");
            System.out.println("  " + tasks.get(unmarkedTaskNumber - 1));
            return;
        case "delete":
            int deletedTaskNumber = parseTaskNumber(argument, "delete");
            validateTaskIndex(deletedTaskNumber, tasks, "delete");
            Task deletedTask = tasks.remove(deletedTaskNumber - 1);
            storage.save(tasks);
            System.out.println("That matter is off the list:");
            System.out.println("  " + deletedTask);
            System.out.println("You now have " + tasks.size() + " tasks on your list.");
            return;
        case "bye":
            throw new LizzyException("One farewell at a time, if you please.\nUse: bye.");
        default:
            throw unknownCommand(action);
        }
    }

    /** Prints every task in the current task list. */
    private static void printTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
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

    /** Parses one strictly formatted ISO date for a deadline or event. */
    private static LocalDate parseDate(String dateText) throws LizzyException {
        try {
            return LocalDate.parse(dateText.strip());
        } catch (DateTimeParseException exception) {
            throw new LizzyException(INVALID_DATE_ERROR);
        }
    }

    /**
     * Splits an argument around one required separator.
     *
     * @param argument the argument to split
     * @param separatorPattern the regular expression for the required separator
     * @param errorMessage the error message for a missing or repeated separator
     * @return exactly two parts surrounding the separator
     * @throws LizzyException if the separator does not occur exactly once
     */
    private static String[] splitExactly(String argument, String separatorPattern, String errorMessage)
            throws LizzyException {
        String[] parts = argument.split(separatorPattern, -1);
        if (parts.length != 2) {
            throw new LizzyException(errorMessage);
        }
        return parts;
    }

    /** Parses a task number supplied to {@code mark}, {@code unmark}, or {@code delete}. */
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
    private static void validateTaskIndex(int taskNumber, List<Task> tasks, String command) throws LizzyException {
        if (tasks.isEmpty()) {
            throw new LizzyException("There is very little to " + command
                    + " when the list is entirely empty.\nAdd a task first.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LizzyException("That task seems to exist only in your imagination.\n"
                    + "Choose a task number from 1 to " + tasks.size() + ".");
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
                + "Try todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /** Creates an exception for a missing or malformed task number. */
    private static LizzyException invalidTaskNumber(String command) {
        return new LizzyException("I'm afraid that will not quite do; I need a proper task number.\n"
                + "Use: " + command + " <task number>.");
    }
}
