package lizzy.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import lizzy.command.DeadlineCommand;
import lizzy.command.DeleteCommand;
import lizzy.command.EventCommand;
import lizzy.command.ExitCommand;
import lizzy.command.FindCommand;
import lizzy.command.ListCommand;
import lizzy.command.MarkCommand;
import lizzy.command.TodoCommand;
import lizzy.command.UnmarkCommand;
import lizzy.command.ViewScheduleCommand;
import lizzy.exception.LizzyException;

/** Tests command recognition and input validation in {@link Parser}. */
public class ParserTest {
    @Test
    void parse_supportedCommands_correctCommandSubtypes() throws LizzyException {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(ViewScheduleCommand.class, Parser.parse("on 2026-09-03"));
        assertInstanceOf(TodoCommand.class, Parser.parse("todo review notes"));
        assertInstanceOf(DeadlineCommand.class, Parser.parse("deadline submit work /by 2026-09-04"));
        assertInstanceOf(EventCommand.class,
                Parser.parse("event consultation /from 2026-09-04 /to 2026-09-05"));
        assertInstanceOf(FindCommand.class, Parser.parse("find notes"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
    }

    @Test
    void parse_flexibleWhitespaceInDatedCommands_commandsCreated() throws LizzyException {
        assertInstanceOf(DeadlineCommand.class,
                Parser.parse("deadline   submit work    /by    2026-09-04"));
        assertInstanceOf(EventCommand.class,
                Parser.parse("event   consultation   /from   2026-09-04   /to   2026-09-05"));
    }

    @Test
    void parse_emptyAndUnknownInput_exceptionsContainHelpfulMessages() {
        assertParseFails("", "Silence may be elegant, but it gives me very little to work with.");
        assertParseFails("postpone", "I'm afraid \"postpone\" is quite beyond my acquaintance.");
    }

    @Test
    void parse_missingTaskDetails_exceptionsExplainRequiredSyntax() {
        assertParseFails("todo", "Use: todo <description>.");
        assertParseFails("deadline submit work", "Use: deadline <description> /by <yyyy-MM-dd>.");
        assertParseFails("deadline /by 2026-09-04", "Use: deadline <description> /by <yyyy-MM-dd>.");
        assertParseFails("event consultation /from 2026-09-04",
                "Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        assertParseFails("event /from 2026-09-04 /to 2026-09-05",
                "Use: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        assertParseFails("find", "Use: find <keyword>.");
    }

    @Test
    void parse_invalidDatesAndEventRange_exceptionsRejectInvalidSchedules() {
        assertParseFails("deadline submit work /by 2026-02-29",
                "Use dates in yyyy-MM-dd format, for example 2019-10-15.");
        assertParseFails("on", "Use: on <yyyy-MM-dd>.");
        assertParseFails("event consultation /from 2026-09-05 /to 2026-09-04",
                "Use an end date on or after the start date.");
    }

    @Test
    void parse_invalidTaskNumbersAndExtraArguments_exceptionsRejectInvalidCommands() {
        assertParseFails("mark", "Use: mark <task number>.");
        assertParseFails("unmark first", "Use: unmark <task number>.");
        assertParseFails("delete 1 2", "Use: delete <task number>.");
        assertParseFails("list now", "Use: list.");
        assertParseFails("bye now", "Use: bye.");
    }

    private static void assertParseFails(String input, String expectedMessage) {
        LizzyException exception = assertThrows(LizzyException.class, () -> Parser.parse(input));
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
