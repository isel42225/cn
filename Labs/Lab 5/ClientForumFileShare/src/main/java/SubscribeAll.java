import com.google.protobuf.Empty;
import com.google.protobuf.ProtocolStringList;
import forum.ForumGrpc;
import forum.SubscribeUnSubscribe;

public class SubscribeAll extends Command {

    private final ForumGrpc.ForumStub stub;
    private final ForumGrpc.ForumBlockingStub bStub;
    private final String user;

    public SubscribeAll(String user) {
        super();
        this.stub  = super.getAsyncStub();
        this.bStub = super.getSyncStub();
        this.user = user;
    }

    @Override
    void execute() {
        Empty emptyReq = Empty.newBuilder().build();
        ProtocolStringList topics = bStub.getAllTopics(emptyReq).getTopicNameList();
        for(String topic : topics){
            SubscribeUnSubscribe sub = SubscribeUnSubscribe
                    .newBuilder()
                    .setTopicName(topic)
                    .setUsrName(user)
                    .build();
            stub.topicSubscribe(sub, new SubscribeStreamObserver());
        }
    }
}
