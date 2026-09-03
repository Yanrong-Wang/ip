package lizzy.ui;

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
     * Creates a UI that reads commands from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
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
        System.out.print(banner);
        System.out.println("Hello! I'm Lizzy.");
        System.out.println("What brings you here today?");
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
        System.out.println(DIVIDER);
    }

    /**
     * Displays Lizzy's farewell.
     */
    public void showGoodbye() {
        System.out.println("Bye! I hope our next conversation will be just as agreeable.");
    }

    /**
     * Displays a user-facing error message.
     *
     * @param message the message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Displays every task in the supplied task list.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
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
            System.out.println("There are no matching tasks in your list.");
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int taskNumber : matchingTaskNumbers) {
            System.out.println(taskNumber + "." + tasks.get(taskNumber - 1));
        }
    }

    /**
     * Displays confirmation after adding a todo.
     *
     * @param task the todo that was added
     * @param numberOfTasks the new number of tasks in the list
     */
    public void showTodoAdded(Task task, int numberOfTasks) {
        System.out.println("Here comes another matter to keep track of:");
        System.out.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays confirmation after adding a deadline.
     *
     * @param task the deadline that was added
     * @param numberOfTasks the new number of tasks in the list
     */
    public void showDeadlineAdded(Task task, int numberOfTasks) {
        System.out.println("A deadline, then. We'd better not keep it waiting.");
        System.out.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays confirmation after adding an event.
     *
     * @param task the event that was added
     * @param numberOfTasks the new number of tasks in the list
     */
    public void showEventAdded(Task task, int numberOfTasks) {
        System.out.println("An engagement! I've added it to your list:");
        System.out.println("  " + task);
        showTaskCount(numberOfTasks);
    }

    /**
     * Displays confirmation after a task is marked as complete.
     *
     * @param task the task marked as complete
     */
    public void showTaskMarked(Task task) {
        System.out.println("Very good! That is one matter settled:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation after a task is marked as incomplete.
     *
     * @param task the task marked as incomplete
     */
    public void showTaskUnmarked(Task task) {
        System.out.println("Ah, it seems this matter is not quite settled:");
        System.out.println("  " + task);
    }

    /**
     * Displays confirmation after a task is deleted.
     *
     * @param task the task that was deleted
     * @param numberOfTasks the number of tasks remaining in the list
     */
    public void showTaskDeleted(Task task, int numberOfTasks) {
        System.out.println("That matter is off the list:");
        System.out.println("  " + task);
        System.out.println("You now have " + numberOfTasks + " tasks on your list.");
    }

    /**
     * Displays deadlines and events that occur on the specified date.
     *
     * @param tasks the task list to search
     * @param date the requested calendar date
     */
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

    /**
     * Displays the current number of tasks after a task has been added.
     *
     * @param numberOfTasks the total number of tasks now in the list
     */
    private void showTaskCount(int numberOfTasks) {
        System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
    }
}
