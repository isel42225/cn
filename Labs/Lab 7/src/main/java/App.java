import com.google.cloud.ServiceOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    private static Map<Integer, Command> commands = new HashMap<>();
    private static String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    private static TopicManager topicManager;

    public static void main(String[] args) {
        initTopicManager();
        Scanner scanner = new Scanner(System.in);

        commands.put(1, topicManager::create);
        commands.put(2, topicManager::listAll);
        commands.put(3, null);
        commands.put(4, null);
        commands.put(5, null);
        commands.put(6, null);

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

    private static void initTopicManager() {
        try {
            topicManager = new TopicManager(PROJECT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
