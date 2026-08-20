import java.util.Scanner;

/**
 * The entry point for the Lizzy chatbot application.
 */
public class Lizzy {
    /**
     * Greets the user, echoes commands, and ends when the user enters {@code bye}.
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

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            System.out.println(divider);

            if (command.equals("bye")) {
                System.out.println("Bye! I hope our next conversation will be just as agreeable.");
                System.out.println(divider);
                break;
            }

            System.out.println(command);
            System.out.println(divider);
        }
    }
}
