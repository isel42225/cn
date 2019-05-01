import forum.ForumGrpc;
import forum.SubscribeUnSubscribe;

import java.util.Scanner;

public class Subscribe extends Command{
    private final ForumGrpc.ForumStub stub;
    private final String user;

    public Subscribe(String user) {
        super();
        this.stub = getAsyncStub();
        this.user = user;
    }

    @Override
    void execute() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Topic : ");
        String topic = scn.nextLine();
        SubscribeUnSubscribe subscription =
                SubscribeUnSubscribe
                        .newBuilder()
                        .setTopicName(topic)
                        .setUsrName(user)
                        .build();
        stub.topicSubscribe(subscription, new SubscribeStreamObserver());
    }
}
