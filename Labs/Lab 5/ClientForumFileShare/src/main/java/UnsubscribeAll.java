import com.google.protobuf.Empty;
import com.google.protobuf.ProtocolStringList;
import forum.ForumGrpc;
import forum.SubscribeUnSubscribe;

public class UnsubscribeAll extends Command {

    private final String user;
    private final ForumGrpc.ForumBlockingStub bStub;
    private final ForumGrpc.ForumStub stub;

    public UnsubscribeAll(String user) {
        super();
        this.user = user;
        this.bStub = super.getSyncStub();
        this.stub = super.getAsyncStub();
    }

    @Override
    void execute() {
        Empty emptyReq = Empty.newBuilder().build();
        ProtocolStringList topics = bStub.getAllTopics(emptyReq).getTopicNameList();
        for(String topic  : topics){
            SubscribeUnSubscribe unsub = SubscribeUnSubscribe
                    .newBuilder()
                    .setUsrName(user)
                    .setTopicName(topic)
                    .build();
            stub.topicUnSubscribe(unsub, new EmptyStreamObserver());
        }
    }
}
