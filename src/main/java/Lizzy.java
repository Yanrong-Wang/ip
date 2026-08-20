import java.util.Scanner;

/**
 * The entry point for the Lizzy chatbot application.
 */
public class Lizzy {
    /**
     * Greets the user, stores entered tasks, lists them on request, and ends on {@code bye}.
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

        String[] tasks = new String[100];
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
                for (int i = 0; i < numberOfTasks; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[numberOfTasks] = command;
                numberOfTasks++;
                System.out.println("added: " + command);
            }
            System.out.println(divider);
        }
    }
}
