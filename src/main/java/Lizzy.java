import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * The entry point and application coordinator for the Lizzy chatbot.
 */
public class Lizzy {
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

    /** The component responsible for all console interaction. */
    private final Ui ui;

    /** Creates Lizzy with its standard console UI. */
    public Lizzy() {
        ui = new Ui();
    }

    /** Runs Lizzy until the user enters {@code bye} or closes standard input. */
    public void run() {
        ui.showWelcome();
        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (LizzyException exception) {
            tasks = new TaskList();
            ui.showError(exception.getMessage());
            ui.showDivider();
        }

        while (ui.hasNextCommand()) {
            String command = ui.readCommand();
            ui.showDivider();
            if (command.equals("bye")) {
                ui.showGoodbye();
                ui.showDivider();
                break;
            }

            try {
                processCommand(command, tasks, storage, ui);
            } catch (LizzyException exception) {
                ui.showError(exception.getMessage());
            }
            ui.showDivider();
        }
    }

    /** Starts the Lizzy application. */
    public static void main(String[] args) {
        new Lizzy().run();
    }

    /** Validates and processes one command without changing the task list on invalid input. */
    private static void processCommand(String command, TaskList tasks, Storage storage, Ui ui)
            throws LizzyException {
        if (command.isEmpty()) {
            throw new LizzyException(EMPTY_INPUT_ERROR);
        }

        String[] commandParts = command.split("\\s+", 2);
        String action = commandParts[0];
        String argument = commandParts.length == 2 ? commandParts[1].strip() : "";
        switch (action) {
        case "list":
            validateNoArgument(argument, "A list requires no further instruction.", "list");
            ui.showTaskList(tasks);
            return;
        case "on":
            if (argument.isBlank()) {
                throw new LizzyException("A date is needed to consult the schedule.\n"
                        + "Use: on <yyyy-MM-dd>.");
            }
            ui.showTasksOnDate(tasks, parseDate(argument));
            return;
        case "todo":
            validateTodo(argument);
            tasks.add(new Todo(argument));
            storage.save(tasks.asList());
            ui.showTodoAdded(tasks.getLast(), tasks.size());
            return;
        case "deadline":
            String[] deadlineParts = splitExactly(argument, "\\s+/by\\s+", INVALID_DEADLINE_ERROR);
            validateDeadline(deadlineParts[0], deadlineParts[1]);
            tasks.add(new Deadline(deadlineParts[0].strip(), parseDate(deadlineParts[1])));
            storage.save(tasks.asList());
            ui.showDeadlineAdded(tasks.getLast(), tasks.size());
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
            storage.save(tasks.asList());
            ui.showEventAdded(tasks.getLast(), tasks.size());
            return;
        case "mark":
            int markedTaskNumber = parseTaskNumber(argument, "mark");
            Task markedTask = tasks.getTask(markedTaskNumber, "mark");
            markedTask.markAsDone();
            storage.save(tasks.asList());
            ui.showTaskMarked(markedTask);
            return;
        case "unmark":
            int unmarkedTaskNumber = parseTaskNumber(argument, "unmark");
            Task unmarkedTask = tasks.getTask(unmarkedTaskNumber, "unmark");
            unmarkedTask.markAsNotDone();
            storage.save(tasks.asList());
            ui.showTaskUnmarked(unmarkedTask);
            return;
        case "delete":
            int deletedTaskNumber = parseTaskNumber(argument, "delete");
            Task deletedTask = tasks.deleteTask(deletedTaskNumber, "delete");
            storage.save(tasks.asList());
            ui.showTaskDeleted(deletedTask, tasks.size());
            return;
        case "bye":
            throw new LizzyException("One farewell at a time, if you please.\nUse: bye.");
        default:
            throw unknownCommand(action);
        }
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

    /** Splits an argument around one required separator. */
    private static String[] splitExactly(String argument, String separatorPattern, String errorMessage)
            throws LizzyException {
        String[] parts = argument.split(separatorPattern, -1);
        if (parts.length != 2) {
            throw new LizzyException(errorMessage);
        }
        return parts;
    }

    /** Parses a task number supplied to a task-status or deletion command. */
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

    /** Validates that commands which require no argument have not received one. */
    private static void validateNoArgument(String argument, String firstSentence, String command) throws LizzyException {
        if (!argument.isBlank()) {
            throw new LizzyException(firstSentence + "\nUse: " + command + ".");
        }
    }

    /** Creates an exception for an unrecognised command. */
    private static LizzyException unknownCommand(String command) {
        return new LizzyException("I'm afraid \"" + command + "\" is quite beyond my acquaintance.\n"
                + "Try todo, deadline, event, list, on, mark, unmark, delete, or bye.");
    }

    /** Creates an exception for a missing or malformed task number. */
    private static LizzyException invalidTaskNumber(String command) {
        return new LizzyException("I'm afraid that will not quite do; I need a proper task number.\n"
                + "Use: " + command + " <task number>.");
    }
}
