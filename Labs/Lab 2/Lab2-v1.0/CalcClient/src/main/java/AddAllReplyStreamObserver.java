import calcstubs.OperationReply;
import io.grpc.stub.StreamObserver;

public class AddAllReplyStreamObserver implements StreamObserver<OperationReply> {

    private double res;

    @Override
    public void onNext(OperationReply operationReply) {
        res = operationReply.getRes();
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        System.out.println("Total sum = " + res);
    }
}
