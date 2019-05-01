import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.pubsub.v1.ProjectName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.Topic;

import java.io.IOException;
import java.util.Scanner;

public class TopicManager {

    private final TopicAdminClient topicAdmin;
    private final String project_id;

    public TopicManager(String project) throws IOException {
        project_id = project;
        topicAdmin = TopicAdminClient.create();
    }

    public void create(){
        Scanner scn = new Scanner(System.in);
        System.out.println("Topic name ?");
        System.out.print("> ");
        String name = scn.nextLine();
        ProjectTopicName tName = ProjectTopicName.of(project_id, name);
        topicAdmin.createTopic(tName);
    }

    public void listAll() {
        TopicAdminClient.ListTopicsPagedResponse res =
                topicAdmin.listTopics(ProjectName.of(project_id));

        for (Topic top : res.iterateAll()) {
            System.out.println("TopicName=" + top.getName());
        }
    }
}
