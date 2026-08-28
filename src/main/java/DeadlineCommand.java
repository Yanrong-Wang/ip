/** Adds one parsed deadline to the task list. */
public class DeadlineCommand extends Command {
    /** The deadline to add. */
    private final Deadline deadline;

    /** Creates a command that adds the supplied deadline. */
    public DeadlineCommand(Deadline deadline) {
        this.deadline = deadline;
    }

    /** Adds, saves, and confirms the deadline. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LizzyException {
        tasks.add(deadline);
        storage.save(tasks.asList());
        ui.showDeadlineAdded(deadline, tasks.size());
    }
}
