package lizzy.task;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

/**
 * Tests the inclusive dates associated with {@link WithinPeriodTask}.
 */
public class WithinPeriodTaskTest {
    @Test
    void occursOn_boundaryAndInteriorDates_includesCompletePeriodOnly() {
        WithinPeriodTask task = new WithinPeriodTask("collect certificate", LocalDate.of(2026, 9, 3),
                LocalDate.of(2026, 9, 5));

        assertFalse(task.occursOn(LocalDate.of(2026, 9, 2)));
        assertTrue(task.occursOn(LocalDate.of(2026, 9, 3)));
        assertTrue(task.occursOn(LocalDate.of(2026, 9, 4)));
        assertTrue(task.occursOn(LocalDate.of(2026, 9, 5)));
        assertFalse(task.occursOn(LocalDate.of(2026, 9, 6)));
    }

    @Test
    void constructor_endBeforeStart_rejectsInvalidPeriod() {
        assertThrows(IllegalArgumentException.class, () -> new WithinPeriodTask("collect certificate",
                LocalDate.of(2026, 9, 5), LocalDate.of(2026, 9, 3)));
    }
}
