package lizzy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests the command-response interface shared with the JavaFX GUI.
 */
public class LizzyTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getResponse_addThenList_stateRetainedAcrossCommands() {
        Lizzy lizzy = new Lizzy(temporaryDirectory.resolve("lizzy.txt"));

        String addResponse = lizzy.getResponse("todo test the GUI");
        String listResponse = lizzy.getResponse("list");

        assertEquals("Here comes another matter to keep track of:\n"
                + "  [T][ ] test the GUI\n"
                + "Now you have 1 tasks in the list.", addResponse);
        assertEquals("Here are the tasks in your list:\n"
                + "1.[T][ ] test the GUI", listResponse);
    }

    @Test
    void getResponse_invalidCommand_validationMessageReturned() {
        Lizzy lizzy = new Lizzy(temporaryDirectory.resolve("lizzy.txt"));

        String response = lizzy.getResponse("unknown");

        assertEquals("I'm afraid \"unknown\" is quite beyond my acquaintance.\n"
                + "Try todo, deadline, event, list, on, mark, unmark, delete, or bye.", response);
    }
}
