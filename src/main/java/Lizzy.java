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
            } else {
                tasks[numberOfTasks] = new Task(command);
                numberOfTasks++;
                System.out.println("added: " + command);
            }
            System.out.println(divider);
        }
    }
}
