import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Saves Lizzy's task list in a text file on disk.
 */
public class Storage {
    private static final String FIELD_SEPARATOR = " | ";

    /** The relative or user-configured path of the task data file. */
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

    /** Converts one task into a delimiter-separated storage record. */
    private static String serialize(Task task) {
        String status = task.isDone() ? "1" : "0";
        String description = encode(task.getDescription());
        if (task instanceof Deadline deadline) {
            return String.join(FIELD_SEPARATOR, "D", status, description, encode(deadline.by));
        }
        if (task instanceof Event event) {
            return String.join(FIELD_SEPARATOR, "E", status, description, encode(event.from), encode(event.to));
        }
        return String.join(FIELD_SEPARATOR, "T", status, description);
    }

    /** Encodes user-entered text so it cannot be confused with file delimiters. */
    private static String encode(String text) {
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }
}
