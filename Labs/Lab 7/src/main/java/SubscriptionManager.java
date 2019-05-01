import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.pubsub.v1.*;

import java.io.IOException;
import java.util.Scanner;

public class SubscriptionManager {
    private final String project_id;
    private final SubscriptionAdminClient subscriptionAdminClient;
    private final Scanner scn;

    public SubscriptionManager(String project_id) throws IOException {
        this.project_id = project_id;
        this.subscriptionAdminClient = SubscriptionAdminClient.create();
        scn = new Scanner(System.in);
    }

    public void createSubscription() {
        System.out.println("What is the topic ?");
        System.out.print("> ");
        String topicName = scn.nextLine();
        ProjectTopicName tName = ProjectTopicName.of(project_id, topicName);
        System.out.println("What is the subscription name ?");
        System.out.print("> ");
        String subName = scn.nextLine();
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(project_id, subName);
        PushConfig pushConfig = PushConfig.getDefaultInstance();
        subscriptionAdminClient.createSubscription(subscriptionName, tName,pushConfig, 0);
    }

    public void listSubscriptions() {
        ProjectName projNAme= ProjectName.of(project_id);
        SubscriptionAdminClient.ListSubscriptionsPagedResponse subscriptions =
                subscriptionAdminClient.listSubscriptions(projNAme);
        System.out.println("Current Subscriptions:");
        for (Subscription sub: subscriptions.iterateAll()) {
            System.out.println(sub.getName()+" on " + sub.getTopic());
        }
    }

    public void subscribe() {
        System.out.println("What is the subscription name ?");
        System.out.print("> ");
        String subName = scn.nextLine();
        ProjectSubscriptionName projectSubscriptionName = ProjectSubscriptionName.of(project_id, subName);
        Subscriber subscriber = Subscriber.newBuilder(projectSubscriptionName, this::messageReceiverHandler ).build();
        subscriber.startAsync().awaitRunning();
    }

    private void messageReceiverHandler(PubsubMessage msg, AckReplyConsumer ackReply) {
        System.out.println("Message (Id:" + msg.getMessageId() + " Data:" + msg.getData().toStringUtf8() + ")");
        ackReply.ack();
    }
}
