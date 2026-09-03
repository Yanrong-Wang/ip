package lizzy.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import lizzy.exception.LizzyException;
import lizzy.task.Deadline;
import lizzy.task.Event;
import lizzy.task.Task;
import lizzy.task.Todo;

/**
 * Saves Lizzy's task list in a text file on disk.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String FIELD_SEPARATOR_PATTERN = "\\s*\\|\\s*";

    /**
     * The relative or user-configured path of the task data file.
     */
    private final Path filePath;

    /**
     * Creates storage that writes to the given path.
     *
     * @param filePath the path of the task data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all tasks from disk, or returns an empty list when the file does not yet exist.
     *
     * @return the tasks stored in the data file
     * @throws LizzyException if the file cannot be read or contains an invalid record
     */
    public List<Task> load() throws LizzyException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        try {
            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                if (!lines.get(i).isBlank()) {
                    tasks.add(deserialize(lines.get(i), i + 1));
                }
            }
            return tasks;
        } catch (IOException exception) {
            throw new LizzyException("I couldn't read your saved tasks from " + filePath + ".\n"
                    + "Starting with an empty task list for this session.");
        }
    }

    /**
     * Writes the complete task list, creating its parent folder when necessary.
     * Text fields are Base64-encoded so descriptions containing delimiters remain safe.
     *
     * @param tasks the tasks to save
     * @throws LizzyException if the data file cannot be written
     */
    public void save(List<Task> tasks) throws LizzyException {
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(serialize(task));
        }

        try {
            Path parentFolder = filePath.getParent();
            if (parentFolder != null) {
                Files.createDirectories(parentFolder);
            }
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new LizzyException("I updated your task list, but couldn't save it to "
                    + filePath + ".");
        }
    }

    /**
     * Converts one task into a delimiter-separated storage record.
     */
    private static String serialize(Task task) {
        String status = task.isDone() ? "1" : "0";
        String description = encode(task.getDescription());
        if (task instanceof Deadline deadline) {
            return String.join(FIELD_SEPARATOR, "D", status, description,
                    encode(deadline.getBy().toString()));
        }
        if (task instanceof Event event) {
            return String.join(FIELD_SEPARATOR, "E", status, description,
                    encode(event.getFrom().toString()), encode(event.getTo().toString()));
        }
        return String.join(FIELD_SEPARATOR, "T", status, description);
    }

    /**
     * Restores one task from a storage record.
     */
    private static Task deserialize(String line, int lineNumber) throws LizzyException {
        String[] fields = line.split(FIELD_SEPARATOR_PATTERN, -1);
        try {
            validateRecord(fields);
            boolean isDone = fields[1].equals("1");
            String description = decode(fields[2]);
            return switch (fields[0]) {
            case "T" -> new Todo(description, isDone);
            case "D" -> new Deadline(description, LocalDate.parse(decode(fields[3])), isDone);
            case "E" -> new Event(description, LocalDate.parse(decode(fields[3])),
                    LocalDate.parse(decode(fields[4])), isDone);
            default -> throw new IllegalArgumentException("Unknown task type");
            };
        } catch (IllegalArgumentException exception) {
            throw new LizzyException("I couldn't understand the saved task data at line " + lineNumber + ".\n"
                    + "Starting with an empty task list for this session.");
        }
    }

    /**
     * Validates the type, completion state, and field count of a storage record.
     */
    private static void validateRecord(String[] fields) {
        if (fields.length < 2 || !(fields[1].equals("0") || fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid task status");
        }

        int expectedFieldCount = switch (fields[0]) {
        case "T" -> 3;
        case "D" -> 4;
        case "E" -> 5;
        default -> throw new IllegalArgumentException("Unknown task type");
        };
        if (fields.length != expectedFieldCount) {
            throw new IllegalArgumentException("Invalid field count");
        }
    }

    /**
     * Encodes user-entered text so it cannot be confused with file delimiters.
     */
    private static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decodes one Base64-encoded text field from the data file.
     */
    private static String decode(String text) {
        byte[] decodedBytes = Base64.getDecoder().decode(text);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }
}
