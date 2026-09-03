package lizzy.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/** Tests inclusive date-range matching for event tasks. */
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
}
