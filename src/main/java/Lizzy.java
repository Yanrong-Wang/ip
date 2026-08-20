import java.util.Scanner;

/**
 * The entry point for the Lizzy chatbot application.
 */
public class Lizzy {
    /**
     * Greets the user, stores tasks, updates their status, lists them, and ends on {@code bye}.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String divider = "____________________________________________________________";
        String banner = "    __    _\n"
                + "   / /   (_)_______  __  __\n"
                + "  / /   / /_  /_  / / / / /\n"
                + " / /___/ / / /_/ /_/ /_/ /\n"
                + "/_____/_/ /___/___/\\__, /\n"
                + "                  /____/\n";
        System.out.println(divider);
        System.out.print(banner);
        System.out.println("Hello! I'm Lizzy.");
        System.out.println("What brings you here today?");
        System.out.println(divider);

        Task[] tasks = new Task[100];
        int numberOfTasks = 0;
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Bye! I hope our next conversation will be just as agreeable.");
                System.out.println(divider);
                break;
            }

            if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < numberOfTasks; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(command.substring(5));
                tasks[taskNumber - 1].markAsDone();
                System.out.println("Very good! That is one matter settled:");
                System.out.println("  " + tasks[taskNumber - 1]);
            } else if (command.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(command.substring(7));
                tasks[taskNumber - 1].markAsNotDone();
                System.out.println("Ah, it seems this matter is not quite settled:");
                System.out.println("  " + tasks[taskNumber - 1]);
            } else if (command.startsWith("todo ")) {
                tasks[numberOfTasks] = new Todo(command.substring(5));
                numberOfTasks++;
                System.out.println("Here comes another matter to keep track of:");
                System.out.println("  " + tasks[numberOfTasks - 1]);
                System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
            } else if (command.startsWith("deadline ")) {
                String[] deadlineParts = command.substring(9).split(" /by ", 2);
                tasks[numberOfTasks] = new Deadline(deadlineParts[0], deadlineParts[1]);
                numberOfTasks++;
                System.out.println("A deadline, then. We'd better not keep it waiting.");
                System.out.println("  " + tasks[numberOfTasks - 1]);
                System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
            } else if (command.startsWith("event ")) {
                String[] eventParts = command.substring(6).split(" /from ", 2);
                String[] timeParts = eventParts[1].split(" /to ", 2);
                tasks[numberOfTasks] = new Event(eventParts[0], timeParts[0], timeParts[1]);
                numberOfTasks++;
                System.out.println("An engagement! I've added it to your list:");
                System.out.println("  " + tasks[numberOfTasks - 1]);
                System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
            } else {
                tasks[numberOfTasks] = new Todo(command);
                numberOfTasks++;
                System.out.println("Another matter to keep track of:");
                System.out.println("  " + tasks[numberOfTasks - 1]);
                System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
            }
            System.out.println(divider);
        }
    }
}
