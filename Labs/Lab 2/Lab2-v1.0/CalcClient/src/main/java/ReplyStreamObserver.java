import calcstubs.OperationReply;
import io.grpc.stub.StreamObserver;

public class ReplyStreamObserver implements StreamObserver<OperationReply> {

    private double res;
    private final double op1;
    private final double op2;

    public ReplyStreamObserver(double op1, double op2) {
        this.op1 = op1;
        this.op2 = op2;
    }

    @Override
    public void onNext(OperationReply operationReply) {
        res = operationReply.getRes();
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println(
                    String.format("Result (async) for %f + %f = %f", op1, op2, res));
    }
}
