package lizzy.command;

import lizzy.exception.LizzyException;
import lizzy.storage.Storage;
import lizzy.task.Task;
import lizzy.task.TaskList;
import lizzy.ui.Ui;

/** Marks one numbered task as incomplete. */
public class UnmarkCommand extends Command {
    /** The one-based task number to mark as incomplete. */
    private final int taskNumber;

    /** Creates a command that unmarks the supplied task number. */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    /** Unmarks, saves, and confirms the referenced task. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LizzyException {
        Task task = tasks.getTask(taskNumber, "unmark");
        task.markAsNotDone();
        storage.save(tasks.asList());
        ui.showTaskUnmarked(task);
    }
}
