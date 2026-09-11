package lizzy.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import lizzy.task.Task;
import lizzy.task.TaskList;

/**
 * Handles all console input and output for Lizzy.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    /**
     * Reads one command at a time from standard input.
     */
    private final Scanner scanner;
    /**
     * Writes user-facing output to the current interface.
     */
    private final PrintStream outputStream;

    /**
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        this(System.in, System.out);
    }

    /**
     * Creates a UI with caller-provided input and output streams.
     *
     * @param inputStream the source of user commands
     * @param outputStream the destination for user-facing output
     */
    public Ui(InputStream inputStream, PrintStream outputStream) {
        scanner = new Scanner(inputStream);
        this.outputStream = outputStream;
    }

    /**
     * Displays Lizzy's greeting and banner.
     */
    public void showWelcome() {
        String banner = "    __    _\n"
                + "   / /   (_)_______  __  __\n"
                + "  / /   / /_  /_  / / / / /\n"
                + " / /___/ / / /_/ /_/ /_/ /\n"
                + "/_____/_/ /___/___/\\__, /\n"
                + "                  /____/\n";
        showDivider();
        outputStream.print(banner);
        outputStream.println("Good day! I'm Elizabeth Bennet—Lizzy, if you please.");
        outputStream.println("Tell me, what shall we set in order?");
        showDivider();
    }

    /**
     * Returns whether another user command is available.
     *
     * @return {@code true} if standard input contains another line
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads and trims the next user command.
     *
     * @return the next command without leading or trailing whitespace
     */
    public String readCommand() {
        return scanner.nextLine().strip();
    }

    /**
     * Displays the standard divider line.
     */
    public void showDivider() {
        outputStream.println(DIVIDER);
    }

    /**
     * Displays Lizzy's farewell.
     */
    public void showGoodbye() {
        outputStream.println("Goodbye! May your plans prosper—and leave you a little leisure.");
    }

    /**
     * Displays a user-facing error message.
     *
     * @param message the message to display
     */
    public void showError(String message) {
        outputStream.println(message);
    }

    /**
     * Displays every task in the supplied task list.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(TaskList tasks) {
        if (tasks.size() == 0) {
            outputStream.println("Your list is perfectly untroubled—there is nothing on it.");
            return;
        }

        outputStream.println("Here is your present list:");
        for (int i = 0; i < tasks.size(); i++) {
            outputStream.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays tasks that matched a description search while retaining their list numbers.
     *
     * @param tasks the complete task list
     * @param matchingTaskNumbers the one-based numbers of the matching tasks
     */
    public void showMatchingTasks(TaskList tasks, List<Integer> matchingTaskNumbers) {
        if (matchingTaskNumbers.isEmpty()) {
            outputStream.println("I found no task answering to that description.");
            return;
        }

        outputStream.println("These tasks answer to your search:");
        for (int taskNumber : matchingTaskNumbers) {
            outputStream.println(taskNumber + "." + tasks.get(taskNumber - 1));
        }
    }

    /**
     * Displays confirmation after adding a todo.
     *
     * @param task the todo that was added
     * @param numberOfTasks the new number of tasks in the list
     */
    public void showTodoAdded(Task task, int numberOfTasks) {
        outputStream.println("Very well—one more matter worth remembering:");
        outputStream.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays confirmation after adding a deadline.
     *
     * @param task the deadline that was added
     * @param numberOfTasks the new number of tasks in the list
     */
    public void showDeadlineAdded(Task task, int numberOfTasks) {
        outputStream.println("A deadline. Let us give it its proper attention:");
        outputStream.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays confirmation after adding an event.
     *
     * @param task the event that was added
     * @param numberOfTasks the new number of tasks in the list
     */
    public void showEventAdded(Task task, int numberOfTasks) {
        outputStream.println("An engagement! I have reserved it a place:");
        outputStream.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays confirmation after adding a task with an inclusive completion period.
     *
     * @param task the period task that was added
     * @param numberOfTasks the new number of tasks in the list
     */
    public void showPeriodTaskAdded(Task task, int numberOfTasks) {
        outputStream.println("A useful interval; I have noted it:");
        outputStream.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays confirmation after a task is marked as complete.
     *
     * @param task the task marked as complete
     */
    public void showTaskMarked(Task task) {
        outputStream.println("Excellent. One less claim upon your attention:");
        outputStream.println("  " + task);
    }

    /**
     * Displays confirmation after a task is marked as incomplete.
     *
     * @param task the task marked as incomplete
     */
    public void showTaskUnmarked(Task task) {
        outputStream.println("No harm done—this matter wants our attention again:");
        outputStream.println("  " + task);
    }

    /**
     * Displays confirmation after a task is deleted.
     *
     * @param task the task that was deleted
     * @param numberOfTasks the number of tasks remaining in the list
     */
    public void showTaskDeleted(Task task, int numberOfTasks) {
        outputStream.println("Consider that matter dismissed:");
        outputStream.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays dated tasks that are relevant on the specified date.
     *
     * @param tasks the task list to search
     * @param date the requested calendar date
     */
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        boolean hasFoundTask = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).occursOn(date)) {
                if (!hasFoundTask) {
                    outputStream.println("Here are the matters requiring attention on "
                            + date.format(DISPLAY_DATE_FORMAT) + ":");
                    hasFoundTask = true;
                }
                outputStream.println((i + 1) + "." + tasks.get(i));
            }
        }
        if (!hasFoundTask) {
            outputStream.println("That date makes no demands upon you: "
                    + date.format(DISPLAY_DATE_FORMAT) + ".");
        }
    }

    /**
     * Displays the current number of tasks after a task has been added.
     *
     * @param numberOfTasks the total number of tasks now in the list
     */
    private void showTaskCount(int numberOfTasks) {
        String taskNoun = numberOfTasks == 1 ? "task" : "tasks";
        outputStream.println("You now have " + numberOfTasks + " " + taskNoun + " in your list.");
    }
}
