import java.time.LocalDate;

/** Displays dated tasks that occur on one requested date. */
public class OnCommand extends Command {
    /** The date to search for. */
    private final LocalDate date;

    /** Creates a command that lists tasks occurring on {@code date}. */
    public OnCommand(LocalDate date) {
        this.date = date;
    }

    /** Displays the dated tasks that occur on the requested date. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTasksOnDate(tasks, date);
    }
}
