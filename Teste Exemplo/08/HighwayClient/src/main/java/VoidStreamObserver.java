import highwaystubs.Void;
import io.grpc.stub.StreamObserver;

public class VoidStreamObserver implements StreamObserver<Void> {
    @Override
    public void onNext(Void aVoid) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
