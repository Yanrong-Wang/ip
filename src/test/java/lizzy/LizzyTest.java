package lizzy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        assertEquals("Very well—one more matter worth remembering:\n"
                + "  [T][ ] test the GUI\n"
                + "You now have 1 task in your list.", addResponse);
        assertEquals("Here is your present list:\n"
                + "1.[T][ ] test the GUI", listResponse);
    }

    @Test
    void getResponse_invalidCommand_validationMessageReturned() {
        Lizzy lizzy = new Lizzy(temporaryDirectory.resolve("lizzy.txt"));

        String response = lizzy.getResponse("unknown");

        assertEquals("I do not believe I know the command \"unknown\"—yet.\n"
                + "Try: todo, deadline, event, within, list, find, on, mark, unmark, delete, or bye.", response);
    }

    @Test
    void getResponseWithStatus_validCommand_responseIsNotError() {
        Lizzy lizzy = new Lizzy(temporaryDirectory.resolve("lizzy.txt"));

        Lizzy.Response response = lizzy.getResponseWithStatus("list");

        assertEquals("Your list is perfectly untroubled—there is nothing on it.", response.text());
        assertFalse(response.isError());
    }

    @Test
    void getResponseWithStatus_invalidCommand_responseIsError() {
        Lizzy lizzy = new Lizzy(temporaryDirectory.resolve("lizzy.txt"));

        Lizzy.Response response = lizzy.getResponseWithStatus("dance");

        assertTrue(response.text().startsWith("I do not believe I know the command \"dance\"—yet."));
        assertTrue(response.isError());
    }
}
