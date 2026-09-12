package lizzy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests inclusive date-range matching for event tasks.
 */
public class EventTest {
    @Test
    void occursOn_startAndEndDatesIncludedDatesBetweenIncludedOutsideDatesExcluded() {
        Event event = new Event("project meeting", LocalDate.of(2026, 9, 3),
                LocalDate.of(2026, 9, 5));

        assertFalse(event.occursOn(LocalDate.of(2026, 9, 2)));
        assertTrue(event.occursOn(LocalDate.of(2026, 9, 3)));
        assertTrue(event.occursOn(LocalDate.of(2026, 9, 4)));
        assertTrue(event.occursOn(LocalDate.of(2026, 9, 5)));
        assertFalse(event.occursOn(LocalDate.of(2026, 9, 6)));
    }

    @Test
    void constructor_endBeforeStart_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Event("project meeting",
                LocalDate.of(2026, 9, 5), LocalDate.of(2026, 9, 3)));
    }

    @Test
    void constructor_nullDate_exceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Event("project meeting", null,
                LocalDate.of(2026, 9, 3)));
        assertThrows(IllegalArgumentException.class, () -> new Event("project meeting",
                LocalDate.of(2026, 9, 3), null));
    }

    @Test
    void gettersAndToString_completedEvent_detailsReturnedAndFormatted() {
        Event event = new Event("conference", LocalDate.of(2026, 9, 3),
                LocalDate.of(2026, 9, 5), true);

        assertEquals(LocalDate.of(2026, 9, 3), event.getFrom());
        assertEquals(LocalDate.of(2026, 9, 5), event.getTo());
        assertEquals("[E][X] conference (from: Sep 3 2026 to: Sep 5 2026)", event.toString());
        assertTrue(event.isDone());
    }

    @Test
    void constructor_sameStartAndEnd_singleDayEventAccepted() {
        Event event = new Event("conference", LocalDate.of(2026, 9, 3), LocalDate.of(2026, 9, 3));

        assertTrue(event.occursOn(LocalDate.of(2026, 9, 3)));
    }
}
