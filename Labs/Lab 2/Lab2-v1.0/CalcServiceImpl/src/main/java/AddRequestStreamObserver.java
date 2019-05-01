import calcstubs.OperationReply;
import calcstubs.OperationRequest;
import io.grpc.stub.StreamObserver;

public class AddRequestStreamObserver implements StreamObserver<OperationRequest> {


    private final StreamObserver<OperationReply> responseObserver;
    private double acc;

    public AddRequestStreamObserver(StreamObserver<OperationReply> responseObserver){
        this.responseObserver = responseObserver;
        acc = 0;
    }

    @Override
    public void onNext(OperationRequest operationRequest) {
        double sum = operationRequest.getOp1() + operationRequest.getOp2();
        acc += sum;
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        OperationReply reply = OperationReply
                .newBuilder()
                .setRes(acc)
                .build();

        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
