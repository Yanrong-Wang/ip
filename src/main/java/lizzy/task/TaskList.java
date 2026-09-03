package lizzy.task;

import java.util.ArrayList;
import java.util.List;

import lizzy.exception.LizzyException;

/**
 * Owns Lizzy's ordered collection of tasks and its list-related operations.
 */
public class TaskList {
    /** The tasks in the order they were added. */
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this(new ArrayList<>());
    }

    /**
     * Creates a task list containing the supplied saved tasks.
     *
     * @param tasks the tasks loaded from storage
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the end of the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns the task at a zero-based position for presentation purposes. */
    public Task get(int index) {
        return tasks.get(index);
    }

    /** Returns the most recently added task. */
    public Task getLast() {
        return tasks.getLast();
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns one-based task numbers whose descriptions contain a case-sensitive keyword.
     *
     * @param keyword the text to search for
     * @return the matching task numbers in task-list order
     */
    public List<Integer> findMatchingTaskNumbers(String keyword) {
        List<Integer> taskNumbers = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDescription().contains(keyword)) {
                taskNumbers.add(i + 1);
            }
        }
        return List.copyOf(taskNumbers);
    }

    /**
     * Returns a task referenced by a one-based command number.
     *
     * @param taskNumber the one-based task number from the user
     * @param command the command that requested the task
     * @return the referenced task
     * @throws LizzyException if the task number cannot refer to a task
     */
    public Task getTask(int taskNumber, String command) throws LizzyException {
        validateTaskNumber(taskNumber, command);
        return tasks.get(taskNumber - 1);
    }

    /**
     * Removes and returns a task referenced by a one-based command number.
     *
     * @param taskNumber the one-based task number from the user
     * @param command the command that requested the deletion
     * @return the removed task
     * @throws LizzyException if the task number cannot refer to a task
     */
    public Task deleteTask(int taskNumber, String command) throws LizzyException {
        validateTaskNumber(taskNumber, command);
        return tasks.remove(taskNumber - 1);
    }

    /** Returns an immutable snapshot for saving to storage. */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }

    /** Validates that a one-based task number exists in this list. */
    private void validateTaskNumber(int taskNumber, String command) throws LizzyException {
        if (tasks.isEmpty()) {
            throw new LizzyException("There is very little to " + command
                    + " when the list is entirely empty.\nAdd a task first.");
        }
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            throw new LizzyException("That task seems to exist only in your imagination.\n"
                    + "Choose a task number from 1 to " + tasks.size() + ".");
        }
    }
}
