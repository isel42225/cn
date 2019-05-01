import forumstubs.Empty;
import io.grpc.stub.StreamObserver;


public class EmptyStreamObserver implements StreamObserver<Empty> {
    @Override
    public void onNext(Empty value) {

    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println("Your message was sent");
    }
}
