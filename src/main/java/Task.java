/**
 * Represents common data and behavior shared by all task types.
 */
public class Task {
    /** The text describing the task. */
    private final String description;

    /** Whether the task has been completed. */
    private boolean isDone;

    /**
     * Creates an incomplete task with the given description.
     *
     * @param description the text describing the task
     */
    public Task(String description) {
        this(description, false);
    }

    /**
     * Restores a task with its saved completion state.
     *
     * @param description the text describing the task
     * @param isDone whether the task has been completed
     */
    protected Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns the symbol used to show this task's completion status.
     *
     * @return {@code "X"} if the task is complete, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns this task's description.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if this task is complete
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns a display-ready representation of this task.
     *
     * @return the task status icon followed by the task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /** Marks this task as complete. */
    public void markAsDone() {
        isDone = true;
    }

    /** Marks this task as incomplete. */
    public void markAsNotDone() {
        isDone = false;
    }
}
