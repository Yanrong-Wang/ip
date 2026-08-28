/**
 * Represents a task that starts and ends at specified times.
 */
public class Event extends Task {
    /** The event start text, kept as entered by the user. */
    protected final String from;

    /** The event end text, kept as entered by the user. */
    protected final String to;

    /**
     * Creates an incomplete event.
     *
     * @param description the text describing the event
     * @param from the event start text
     * @param to the event end text
     */
    public Event(String description, String from, String to) {
        this(description, from, to, false);
    }

    /**
     * Restores an event with its saved completion state.
     *
     * @param description the text describing the event
     * @param from the event start text
     * @param to the event end text
     * @param isDone whether the event has been completed
     */
    public Event(String description, String from, String to, boolean isDone) {
        super(description, isDone);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns this event with its type marker and time range.
     *
     * @return the event's display-ready representation
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
