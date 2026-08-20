/**
 * Represents a task that must be completed by a specified time.
 */
public class Deadline extends Task {
    /** The deadline text, kept as entered by the user. */
    protected final String by;

    /**
     * Creates an incomplete deadline.
     *
     * @param description the text describing the deadline
     * @param by the deadline text
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns this deadline with its type marker and deadline text.
     *
     * @return the deadline's display-ready representation
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
