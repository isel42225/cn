import com.google.api.core.ApiFunction;
import com.google.api.core.ApiFuture;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PushConfig;

import java.io.IOException;

public class PubSubManager {
    private static String TOPIC_NAME = "photo-scan";
    private final String projectId = ServiceOptions.getDefaultProjectId();

    public void notify(String filename){
        try {
            ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, TOPIC_NAME);
            Publisher publisher = Publisher.newBuilder(projectTopicName).build();
            PubsubMessage message = PubsubMessage
                    .newBuilder()
                    .putAttributes("filename", filename)
                    .build();
            ApiFuture<String> future =  publisher.publish(message);
            while(!future.isDone());
            publisher.shutdown();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
