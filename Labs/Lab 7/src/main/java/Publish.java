import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;

import java.io.IOException;
import java.util.Scanner;

public class Publish implements Command {
    private final String project_id;


    public Publish(String project_id) {
        this.project_id = project_id;

    }

    @Override
    public void execute() {
        try {
            Scanner scn = new Scanner(System.in);
            System.out.println("What is the topic?");
            System.out.print("> ");
            String topicName = scn.nextLine();

            ProjectTopicName ptn = ProjectTopicName.of(project_id, topicName);
            Publisher publisher = Publisher.newBuilder(ptn).build();

            System.out.println("Write your message.");
            System.out.print("> ");
            String msgText = scn.nextLine();
            ByteString msgData = ByteString.copyFromUtf8(msgText);
            PubsubMessage msg = PubsubMessage
                    .newBuilder()
                    .setData(msgData)
                    .build();
            ApiFuture<String> future = publisher.publish(msg);
            future.get();
            publisher.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
