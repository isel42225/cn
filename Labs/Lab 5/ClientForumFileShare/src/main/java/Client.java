import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {

    private static Map<Integer, Command> commands = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Your name?");
        String user = scanner.nextLine();

        commands.put(1, new ListAllTopics());
        commands.put(2, new Subscribe(user));
        commands.put(3, new SubscribeAll(user));
        commands.put(4, new UnsubscribeAll(user));
        commands.put(5, new Unsubscribe(user));
        commands.put(6, new PublishMessage(user));

        printCommands();

        int input = -1;

        while((input = scanner.nextInt()) != 99) {
            Command cmd = commands.get(input);
            cmd.execute();
        }
    }

    private static void printCommands() {
        System.out.println("Available commands");
        for (int idx : commands.keySet()) {
            System.out.println(
                    String.format("%d : %s", idx, commands.get(idx).getClass().getCanonicalName())
            );
        }
        System.out.println("99 : Exit");
        System.out.print("> ");
    }
}
