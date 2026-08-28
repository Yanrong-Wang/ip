/** Marks one numbered task as complete. */
public class MarkCommand extends Command {
    /** The one-based task number to mark. */
    private final int taskNumber;

    /** Creates a command that marks the supplied task number. */
    public MarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /** Marks, saves, and confirms the referenced task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LizzyException {
        Task task = tasks.getTask(taskNumber, "mark");
        task.markAsDone();
        storage.save(tasks.asList());
        ui.showTaskMarked(task);
    }
}
