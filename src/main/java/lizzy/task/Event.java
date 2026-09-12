package lizzy.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Represents a task that starts and ends at specified times.
 */
public class Event extends Task {
    /**
     * The format used to present parsed dates to the user.
     */
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    /**
     * The event start date.
     */
    protected final LocalDate from;

    /**
     * The event end date.
     */
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
        if (from == null || to == null) {
            throw new IllegalArgumentException("Event dates cannot be null.");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("An event's end date cannot be before its start date.");
        }
        this.from = from;
        this.to = to;
    }

    /**
     * Returns whether another task is an event with the same description and date range.
     *
     * @param other the task to compare
     * @return {@code true} if both events have the same details
     */
    @Override
    boolean hasSameDetails(Task other) {
        return super.hasSameDetails(other)
                && from.equals(((Event) other).from)
                && to.equals(((Event) other).to);
    }

    /**
     * Returns whether the date falls within this event's inclusive date range.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(from) && !date.isAfter(to);
    }

    /**
     * Returns this event's start date.
     *
     * @return the event start date
     */
    public LocalDate getFrom() {
        return from;
    }

    /**
     * Returns this event's end date.
     *
     * @return the event end date
     */
    public LocalDate getTo() {
        return to;
    }

    /**
     * Returns a display-ready representation of this event.
     *
     * @return the event type marker, completion status, description, and formatted date range.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(DISPLAY_DATE_FORMAT)
                + " to: " + to.format(DISPLAY_DATE_FORMAT) + ")";
    }
}
