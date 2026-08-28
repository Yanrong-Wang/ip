import java.nio.file.Path;

/**
 * The entry point and application coordinator for the Lizzy chatbot.
 */
public class Lizzy {
    private static final Path DATA_FILE_PATH = Path.of(
            System.getProperty("lizzy.data.path", "data/lizzy.txt"));
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
        ParsedCommand parsedCommand = Parser.parse(command);
        switch (parsedCommand.action()) {
        case "list":
            Parser.requireNoArgument(parsedCommand.argument(), "A list requires no further instruction.", "list");
            ui.showTaskList(tasks);
            return;
        case "on":
            ui.showTasksOnDate(tasks, Parser.parseOnDate(parsedCommand.argument()));
            return;
        case "todo":
            tasks.add(Parser.parseTodo(parsedCommand.argument()));
            storage.save(tasks.asList());
            ui.showTodoAdded(tasks.getLast(), tasks.size());
            return;
        case "deadline":
            tasks.add(Parser.parseDeadline(parsedCommand.argument()));
            storage.save(tasks.asList());
            ui.showDeadlineAdded(tasks.getLast(), tasks.size());
            return;
        case "event":
            tasks.add(Parser.parseEvent(parsedCommand.argument()));
            storage.save(tasks.asList());
            ui.showEventAdded(tasks.getLast(), tasks.size());
            return;
        case "mark":
            int markedTaskNumber = Parser.parseTaskNumber(parsedCommand.argument(), "mark");
            Task markedTask = tasks.getTask(markedTaskNumber, "mark");
            markedTask.markAsDone();
            storage.save(tasks.asList());
            ui.showTaskMarked(markedTask);
            return;
        case "unmark":
            int unmarkedTaskNumber = Parser.parseTaskNumber(parsedCommand.argument(), "unmark");
            Task unmarkedTask = tasks.getTask(unmarkedTaskNumber, "unmark");
            unmarkedTask.markAsNotDone();
            storage.save(tasks.asList());
            ui.showTaskUnmarked(unmarkedTask);
            return;
        case "delete":
            int deletedTaskNumber = Parser.parseTaskNumber(parsedCommand.argument(), "delete");
            Task deletedTask = tasks.deleteTask(deletedTaskNumber, "delete");
            storage.save(tasks.asList());
            ui.showTaskDeleted(deletedTask, tasks.size());
            return;
        case "bye":
            Parser.requireNoArgument(parsedCommand.argument(), "One farewell at a time, if you please.", "bye");
            throw new LizzyException("One farewell at a time, if you please.\nUse: bye.");
        default:
            throw new IllegalStateException("Parser produced an unsupported command.");
        }
    }
}
