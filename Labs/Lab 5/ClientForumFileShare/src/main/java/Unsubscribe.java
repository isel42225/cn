import forum.ForumGrpc;
import forum.SubscribeUnSubscribe;

import java.util.Scanner;

public class Unsubscribe extends Command {

    private final ForumGrpc.ForumStub stub ;
    private final String user;

    public Unsubscribe(String user) {
        super();
        this.stub = getAsyncStub();
        this.user = user;
    }

    @Override
    void execute() {
        Scanner scn = new Scanner(System.in);
        System.out.print("Topic : ");
        String topic = scn.nextLine();

        SubscribeUnSubscribe unSubscribe = SubscribeUnSubscribe
                .newBuilder()
                .setTopicName(topic)
                .setUsrName(user)
                .build();
        stub.topicUnSubscribe(unSubscribe, new EmptyStreamObserver());
    }
}
