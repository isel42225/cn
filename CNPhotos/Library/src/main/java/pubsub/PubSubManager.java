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
    private final static String IMAGE_TOPIC = "photo-scan";
    private final static String MONITOR_TOPIC = "photos-monitor";
    private final static String IMAGE_SUBSCRIPTION = "photo-scan-sub";
    private final static String MONITOR_SUBSCRIPTION = "photo-monitor-sub";
    private final String projectId = ServiceOptions.getDefaultProjectId();

    public void publishImg(String filename){
        try {
            ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, IMAGE_TOPIC);
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

    public void subscribeToImg(MessageReceiver messageHandler) {
        ProjectSubscriptionName projectSubscriptionName =
                ProjectSubscriptionName.of(projectId, IMAGE_SUBSCRIPTION);
        Subscriber subscriber = Subscriber
                .newBuilder(projectSubscriptionName, messageHandler)
                .build();
        subscriber.startAsync().awaitRunning();
    }

    public void publishMetric() {
        try{
            ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, MONITOR_TOPIC);
            Publisher publisher = Publisher.newBuilder(projectTopicName).build();
            PubsubMessage message = PubsubMessage
                    .newBuilder()
                    .build();
            ApiFuture<String> future =  publisher.publish(message);
            while(!future.isDone());
            publisher.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void subscribeToMonitor(MessageReceiver messageHandler) {
        ProjectSubscriptionName projectSubscriptionName =
                ProjectSubscriptionName.of(projectId, MONITOR_SUBSCRIPTION);
        Subscriber subscriber = Subscriber
                .newBuilder(projectSubscriptionName, messageHandler)
                .build();
        subscriber.startAsync().awaitRunning();
    }
}
