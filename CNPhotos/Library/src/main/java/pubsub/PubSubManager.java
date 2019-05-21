package pubsub;

import com.google.api.core.ApiFuture;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

public class PubSubManager {
    private final static String TOPIC_NAME = "photo-scan";
    private final static String SUBSCRIPTION_NAME = "photo-scan-sub";
    private final String projectId = ServiceOptions.getDefaultProjectId();

    public void notify(String filename){
        try {
            ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, TOPIC_NAME);
            Publisher publisher = Publisher.newBuilder(projectTopicName).build();
            PubsubMessage message = PubsubMessage
                    .newBuilder()
                    .setData(ByteString.copyFromUtf8(filename))
                    .build();
            ApiFuture<String> future =  publisher.publish(message);
            while(!future.isDone());
            publisher.shutdown();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void subscribe(MessageReceiver messageHandler) {
        ProjectSubscriptionName projectSubscriptionName =
                ProjectSubscriptionName.of(projectId, SUBSCRIPTION_NAME);
        Subscriber subscriber = Subscriber
                .newBuilder(projectSubscriptionName, messageHandler)
                .build();
        subscriber.startAsync().awaitRunning();
    }
}
