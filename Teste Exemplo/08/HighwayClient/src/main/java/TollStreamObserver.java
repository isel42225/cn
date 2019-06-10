import highwaystubs.Toll;
import io.grpc.stub.StreamObserver;

public class TollStreamObserver implements StreamObserver<Toll> {
    @Override
    public void onNext(Toll toll) {
        String toPay = toll.getValue();
        System.out.println(String.format("A pagar : %s", toPay));
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }
}
