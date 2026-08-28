import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that starts and ends at specified times.
 */
public class Event extends Task {
    /** The format used to present parsed dates to the user. */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    /** The event start date. */
    protected final LocalDate from;

    /** The event end date. */
    protected final LocalDate to;

    /**
     * Creates an incomplete event.
     *
     * @param description the text describing the event
     * @param from the event start date
     * @param to the event end date
     */
    public Event(String description, LocalDate from, LocalDate to) {
        this(description, from, to, false);
    }

    /**
     * Restores an event with its saved completion state.
     *
     * @param description the text describing the event
     * @param from the event start date
     * @param to the event end date
     * @param isDone whether the event has been completed
     */
    public Event(String description, LocalDate from, LocalDate to, boolean isDone) {
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
        return "[E]" + super.toString() + " (from: " + from.format(DISPLAY_DATE_FORMAT)
                + " to: " + to.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
