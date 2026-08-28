/** Adds one parsed todo to the task list. */
public class TodoCommand extends Command {
    /** The todo to add. */
    private final Todo todo;

    /** Creates a command that adds the supplied todo. */
    public TodoCommand(Todo todo) {
        this.todo = todo;
    }

    /** Adds, saves, and confirms the todo. */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws LizzyException {
        tasks.add(todo);
        storage.save(tasks.asList());
        ui.showTodoAdded(todo, tasks.size());
    }
}
