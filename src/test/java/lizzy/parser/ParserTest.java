package lizzy.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import lizzy.command.WithinPeriodCommand;
import lizzy.exception.LizzyException;

/**
 * Tests command recognition and input validation in {@link Parser}.
 */
public class ParserTest {
    @Test
    void parse_supportedCommands_correctCommandSubtypes() throws LizzyException {
        assertInstanceOf(ListCommand.class, Parser.parse("list"));
        assertInstanceOf(ViewScheduleCommand.class, Parser.parse("on 2026-09-03"));
        assertInstanceOf(TodoCommand.class, Parser.parse("todo review notes"));
        assertInstanceOf(DeadlineCommand.class, Parser.parse("deadline submit work /by 2026-09-04"));
        assertInstanceOf(EventCommand.class,
                Parser.parse("event consultation /from 2026-09-04 /to 2026-09-05"));
        assertInstanceOf(WithinPeriodCommand.class,
                Parser.parse("within collect certificate /from 2026-09-04 /to 2026-09-05"));
        assertInstanceOf(FindCommand.class, Parser.parse("find notes"));
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 1"));
        assertInstanceOf(UnmarkCommand.class, Parser.parse("unmark 1"));
        assertInstanceOf(DeleteCommand.class, Parser.parse("delete 1"));
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
    }

    @Test
    void parse_flexibleWhitespaceInDatedCommands_commandsCreated() throws LizzyException {
        assertInstanceOf(ListCommand.class, Parser.parse("  list  "));
        assertInstanceOf(DeadlineCommand.class,
                Parser.parse("deadline   submit work    /by    2026-09-04"));
        assertInstanceOf(EventCommand.class,
                Parser.parse("event   consultation   /from   2026-09-04   /to   2026-09-05"));
        assertInstanceOf(WithinPeriodCommand.class,
                Parser.parse("within   collect certificate   /from   2026-09-04   /to   2026-09-05"));
    }

    @Test
    void parse_validCalendarAndBoundaryDates_commandsCreated() throws LizzyException {
        assertInstanceOf(DeadlineCommand.class,
                Parser.parse("deadline leap-day task /by 2028-02-29"));
        assertInstanceOf(EventCommand.class,
                Parser.parse("event interview /from 2026-09-04 /to 2026-09-04"));
        assertInstanceOf(WithinPeriodCommand.class,
                Parser.parse("within collect form /from 2026-09-04 /to 2026-09-04"));
    }

    @Test
    void parse_exitAndOrdinaryCommand_isExitDistinguishesCommands() throws LizzyException {
        assertTrue(Parser.parse("bye").isExit());
        assertFalse(Parser.parse("list").isExit());
    }

    @Test
    void parse_emptyAndUnknownInput_exceptionsContainHelpfulMessages() {
        assertParseFails(null, "Silence may be elegant, but it gives me very little to work with.");
        assertParseFails("", "Silence may be elegant, but it gives me very little to work with.");
        assertParseFails("   ", "Silence may be elegant, but it gives me very little to work with.");
        assertParseFails("postpone", "I'm afraid \"postpone\" is quite beyond my acquaintance.");
    }

    @Test
    void parse_missingTaskDetails_exceptionsExplainRequiredSyntax() {
        assertParseFails("todo", "Give it some substance: todo <description>.");
        assertParseFails("deadline submit work",
                "Set it out like this: deadline <description> /by <yyyy-MM-dd>.");
        assertParseFails("deadline /by 2026-09-04",
                "Set it out like this: deadline <description> /by <yyyy-MM-dd>.");
        assertParseFails("event consultation /from 2026-09-04",
                "Arrange it like this: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        assertParseFails("event /from 2026-09-04 /to 2026-09-05",
                "Arrange it like this: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        assertParseFails("within collect certificate /from 2026-09-04",
                "Give it proper bounds: within <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        assertParseFails("find", "Give me something to seek: find <keyword>.");
    }

    @Test
    void parse_invalidDatesAndEventRange_exceptionsRejectInvalidSchedules() {
        assertParseFails("deadline submit work /by 2026-02-29",
                "Dates behave best as yyyy-MM-dd—for example, 2019-10-15.");
        assertParseFails("on", "Name the day like this: on <yyyy-MM-dd>.");
        assertParseFails("event consultation /from 2026-09-05 /to 2026-09-04",
                "Let time keep its proper order: choose an end date on or after the start date.");
        assertParseFails("within collect certificate /from 2026-09-05 /to 2026-09-04",
                "Keep the interval sensible: choose an end date on or after the start date.");
    }

    @Test
    void parse_invalidTaskNumbersAndExtraArguments_exceptionsRejectInvalidCommands() {
        assertParseFails("mark", "Be precise: mark <task number>.");
        assertParseFails("mark +1", "Be precise: mark <task number>.");
        assertParseFails("mark -1", "Be precise: mark <task number>.");
        assertParseFails("mark 01", "Be precise: mark <task number>.");
        assertParseFails("unmark first", "Be precise: unmark <task number>.");
        assertParseFails("delete 1 2", "Be precise: delete <task number>.");
        assertParseFails("delete 999999999999999999999", "Be precise: delete <task number>.");
        assertParseFails("list now", "A simple \"list\" will do.");
        assertParseFails("bye now", "A simple \"bye\" will do.");
    }

    @Test
    void parse_repeatedDateMarkers_exceptionsExplainRequiredSyntax() {
        assertParseFails("deadline report /by 2026-09-04 /by 2026-09-05",
                "Set it out like this: deadline <description> /by <yyyy-MM-dd>.");
        assertParseFails("event meeting /from 2026-09-04 /from 2026-09-05 /to 2026-09-06",
                "Arrange it like this: event <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
        assertParseFails("within task /from 2026-09-04 /to 2026-09-05 /to 2026-09-06",
                "Give it proper bounds: within <description> /from <yyyy-MM-dd> /to <yyyy-MM-dd>.");
    }

    private static void assertParseFails(String input, String expectedMessage) {
        LizzyException exception = assertThrows(LizzyException.class, () -> Parser.parse(input));
        assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
