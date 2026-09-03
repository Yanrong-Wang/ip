package lizzy.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import lizzy.exception.LizzyException;

/**
 * Tests task ordering, one-based command access, and safe list snapshots.
 */
public class TaskListTest {
    @Test
    void addAndGet_tasksKeepInsertionOrder() {
        TaskList tasks = new TaskList();
        Todo first = new Todo("read chapter");
        Todo second = new Todo("revise notes");

        tasks.add(first);
        tasks.add(second);

        assertEquals(2, tasks.size());
        assertSame(first, tasks.get(0));
        assertSame(second, tasks.getLast());
    }

    @Test
    void constructor_inputListLaterChanged_taskListKeepsOwnCopy() {
        List<Task> sourceTasks = new ArrayList<>();
        Todo first = new Todo("read chapter");
        sourceTasks.add(first);

        TaskList tasks = new TaskList(sourceTasks);
        sourceTasks.add(new Todo("revise notes"));

        assertEquals(1, tasks.size());
        assertSame(first, tasks.getLast());
    }

    @Test
    void getTask_validOneBasedNumber_matchingTaskReturned() throws LizzyException {
        TaskList tasks = new TaskList(List.of(new Todo("first"), new Todo("second")));

        Task task = tasks.getTask(2, "mark");

        assertEquals("second", task.getDescription());
    }

    @Test
    void getTask_emptyOrOutOfRangeNumber_helpfulExceptionThrown() {
        TaskList emptyTasks = new TaskList();
        TaskList oneTask = new TaskList(List.of(new Todo("first")));

        assertTaskNumberError(emptyTasks, 1, "mark", "Add a task first.");
        assertTaskNumberError(oneTask, 0, "mark", "Choose a task number from 1 to 1.");
        assertTaskNumberError(oneTask, 2, "mark", "Choose a task number from 1 to 1.");
    }

    @Test
    void deleteTask_validOneBasedNumber_taskRemovedAndFollowingTaskReindexed() throws LizzyException {
        Todo first = new Todo("first");
        Todo second = new Todo("second");
        TaskList tasks = new TaskList(List.of(first, second));

        Task deletedTask = tasks.deleteTask(1, "delete");

        assertSame(first, deletedTask);
        assertEquals(1, tasks.size());
        assertSame(second, tasks.getTask(1, "delete"));
    }

    @Test
    void asList_snapshotIsImmutableAndUnaffectedByLaterAdditions() {
        TaskList tasks = new TaskList();
        tasks.add(new Todo("first"));

        List<Task> snapshot = tasks.asList();
        tasks.add(new Todo("second"));

        assertEquals(1, snapshot.size());
        assertThrows(UnsupportedOperationException.class, () -> snapshot.add(new Todo("third")));
    }

    @Test
    void findMatchingTaskNumbers_matchingDescriptionsKeepOriginalNumbersAndOrder() {
        TaskList tasks = new TaskList(List.of(new Todo("read book"), new Todo("buy milk"),
                new Todo("return book"), new Todo("Book review")));

        assertEquals(List.of(1, 3), tasks.findMatchingTaskNumbers("book"));
        assertEquals(List.of(), tasks.findMatchingTaskNumbers("pen"));
        assertEquals(List.of(4), tasks.findMatchingTaskNumbers("Book"));
    }

    private static void assertTaskNumberError(TaskList tasks, int number, String command,
                                              String expectedMessage) {
        LizzyException exception = assertThrows(LizzyException.class, () -> tasks.getTask(number, command));
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
