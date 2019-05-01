import forumstubs.ForumMessage;
import io.grpc.stub.StreamObserver;

public class SubscribeStreamObserver implements StreamObserver<ForumMessage> {
    @Override
    public void onNext(ForumMessage value) {
        System.out.println(
                String.format("%s in %s \n%s", value.getFromUser(),value.getTopicName(),value.getTxtMsg()));
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println("You are no longer subscribed");
    }
}
