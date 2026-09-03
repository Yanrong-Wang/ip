package lizzy.command;

import lizzy.storage.Storage;
import lizzy.task.TaskList;
import lizzy.ui.Ui;

/** Displays every task in the task list. */
public class ListCommand extends Command {
    /** Displays the current task list. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showTaskList(tasks);
    }
}
