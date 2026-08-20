/**
 * Represents a task with a description and a completion status.
 */
public class Task {
    /** The one-letter code that identifies this task as a todo, deadline, or event. */
    private final String taskType;

    /** The text describing the task. */
    private final String description;

    /** Extra display information such as a deadline or an event's start and end times. */
    private final String timeDetails;

    /** Whether the task has been completed. */
    private boolean isDone;

    /**
     * Creates an incomplete todo, deadline, or event task.
     *
     * @param taskType the one-letter task type code
     * @param description the text describing the task
     * @param timeDetails the date/time information to show after the description, if any
     */
    public Task(String taskType, String description, String timeDetails) {
        this.taskType = taskType;
        this.description = description;
        this.timeDetails = timeDetails;
        this.isDone = false;
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
     * Returns a display-ready representation of this task.
     *
     * @return the task type, status icon, description, and any date/time details
     */
    @Override
    public String toString() {
        return "[" + taskType + "][" + getStatusIcon() + "] " + description + timeDetails;
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
