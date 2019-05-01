import com.google.protobuf.Empty;
import forum.ExistingTopics;
import forum.ForumGrpc;
import io.grpc.stub.StreamObserver;

public class ListAllTopics extends Command {
    private class TopicListStreamObserver implements StreamObserver<ExistingTopics>{

        @Override
        public void onNext(ExistingTopics existingTopics) {
            for (String s : existingTopics.getTopicNameList()) {
                System.out.println(s);
            }
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {

        }
    }

    private final ForumGrpc.ForumStub stub;

    public ListAllTopics() {
        super();
        stub = this.getAsyncStub();
    }

    @Override
    public void execute() {
        Empty req = Empty.newBuilder().build();
        stub.getAllTopics(req, new TopicListStreamObserver());
    }
}
