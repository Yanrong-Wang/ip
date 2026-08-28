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

        boolean isExit = false;
        while (ui.hasNextCommand() && !isExit) {
            String command = ui.readCommand();
            ui.showDivider();

            try {
                Command parsedCommand = Parser.parse(command);
                parsedCommand.execute(tasks, ui, storage);
                isExit = parsedCommand.isExit();
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

}
