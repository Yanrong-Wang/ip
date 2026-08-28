import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

/**
 * Handles all console input and output for Lizzy.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);

    /** Reads one command at a time from standard input. */
    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /** Displays Lizzy's greeting and banner. */
    public void showWelcome() {
        String banner = "    __    _\n"
                + "   / /   (_)_______  __  __\n"
                + "  / /   / /_  /_  / / / / /\n"
                + " / /___/ / / /_/ /_/ /_/ /\n"
                + "/_____/_/ /___/___/\\__, /\n"
                + "                  /____/\n";
        showDivider();
        System.out.print(banner);
        System.out.println("Hello! I'm Lizzy.");
        System.out.println("What brings you here today?");
        showDivider();
    }

    /** Returns whether another user command is available. */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /** Reads and trims the next user command. */
    public String readCommand() {
        return scanner.nextLine().strip();
    }

    /** Displays the standard divider line. */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /** Displays Lizzy's farewell. */
    public void showGoodbye() {
        System.out.println("Bye! I hope our next conversation will be just as agreeable.");
    }

    /** Displays a user-facing error message. */
    public void showError(String message) {
        System.out.println(message);
    }

    /** Displays every task in the supplied task list. */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /** Displays confirmation after adding a todo. */
    public void showTodoAdded(Task task, int numberOfTasks) {
        System.out.println("Here comes another matter to keep track of:");
        System.out.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /** Displays confirmation after adding a deadline. */
    public void showDeadlineAdded(Task task, int numberOfTasks) {
        System.out.println("A deadline, then. We'd better not keep it waiting.");
        System.out.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /** Displays confirmation after adding an event. */
    public void showEventAdded(Task task, int numberOfTasks) {
        System.out.println("An engagement! I've added it to your list:");
        System.out.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /** Displays confirmation after a task is marked as complete. */
    public void showTaskMarked(Task task) {
        System.out.println("Very good! That is one matter settled:");
        System.out.println("  " + task);
    }

    /** Displays confirmation after a task is marked as incomplete. */
    public void showTaskUnmarked(Task task) {
        System.out.println("Ah, it seems this matter is not quite settled:");
        System.out.println("  " + task);
    }

    /** Displays confirmation after a task is deleted. */
    public void showTaskDeleted(Task task, int numberOfTasks) {
        System.out.println("That matter is off the list:");
        System.out.println("  " + task);
        System.out.println("You now have " + numberOfTasks + " tasks on your list.");
    }

    /** Displays deadlines and events that occur on the specified date. */
    public void showTasksOnDate(TaskList tasks, LocalDate date) {
        boolean foundTask = false;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).occursOn(date)) {
                if (!foundTask) {
                    System.out.println("Here are the deadlines and events scheduled on "
                            + date.format(DISPLAY_DATE_FORMAT) + ":");
                    foundTask = true;
                }
                System.out.println((i + 1) + "." + tasks.get(i));
            }
        }
        if (!foundTask) {
            System.out.println("There are no deadlines or events scheduled on "
                    + date.format(DISPLAY_DATE_FORMAT) + ".");
        }
    }

    /** Displays the current number of tasks. */
    private void showTaskCount(int numberOfTasks) {
        System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
    }
}
