/**
 * Represents a user command after its command word has been separated from its argument.
 */
public record ParsedCommand(String action, String argument) {
}
