import com.google.cloud.ServiceOptions;
import com.google.cloud.Tuple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    private final static String CREATE_TOPIC_CMD = "Create a topic";
    private final static String LIST_TOPICS_CMD = "List all topics";
    private final static String CREATE_SUBSCRIPTION_CMD = "Create a subscription";
    private final static String LIST_SUBSCRIPTIONS_CMD = "List all subscriptions";
    private final static String SUBSCRIBE_CMD = "Subscribe to a subscription";
    private final static String PUBLISH_CMD = "Publish a message";
    private static Map<Integer, Tuple<String, Command>> commands = new HashMap<>();
    private static String PROJECT_ID = ServiceOptions.getDefaultProjectId();

    private static TopicManager topicManager;
    private static SubscriptionManager subscriptionManager;

    public static void main(String[] args) {
        initManagers();
        Scanner scanner = new Scanner(System.in);

        commands.put(1, Tuple.of(CREATE_TOPIC_CMD,topicManager::create));
        commands.put(2, Tuple.of(LIST_TOPICS_CMD,topicManager::listAll));
        commands.put(3, Tuple.of(CREATE_SUBSCRIPTION_CMD,subscriptionManager::createSubscription));
        commands.put(4, Tuple.of(LIST_SUBSCRIPTIONS_CMD,subscriptionManager::listSubscriptions));
        commands.put(5, Tuple.of(SUBSCRIBE_CMD,subscriptionManager::subscribe));
        commands.put(6, Tuple.of(PUBLISH_CMD,new Publish(PROJECT_ID)));

        printCommands();

        int input = -1;

        while((input = scanner.nextInt()) != 99) {
            Command cmd = commands.get(input).y();
            cmd.execute();
        }
    }

    private static void printCommands() {
        System.out.println("Available commands");
        for (int idx : commands.keySet()) {
            String commandDesc = commands.get(idx).x();
            System.out.println(
                    String.format("%d : %s", idx, commandDesc));
        }
        System.out.println("99 : Exit");
        System.out.print("> ");
    }

    private static void initManagers() {
        try {
            topicManager = new TopicManager(PROJECT_ID);
            subscriptionManager = new SubscriptionManager(PROJECT_ID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
