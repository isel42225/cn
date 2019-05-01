import calcstubs.OperationReply;
import calcstubs.OperationRequest;
import io.grpc.stub.StreamObserver;

public class AddStreamRequestStreamObserver implements StreamObserver<OperationRequest> {

    private final StreamObserver<OperationReply> responseObserver;

    public AddStreamRequestStreamObserver(StreamObserver<OperationReply> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(OperationRequest request) {
        double op1 = request.getOp1();
        double op2 = request.getOp2();

        double res = op1 + op2;
        OperationReply reply = OperationReply
                .newBuilder()
                .setRes(res)
                .build();
        responseObserver.onNext(reply);
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        responseObserver.onCompleted();
    }
}
