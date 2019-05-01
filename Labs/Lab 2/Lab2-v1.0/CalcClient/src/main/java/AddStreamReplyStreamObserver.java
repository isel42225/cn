import calcstubs.OperationReply;
import io.grpc.stub.StreamObserver;

public class AddStreamReplyStreamObserver implements StreamObserver<OperationReply> {

    private int operCount = 0;
    @Override
    public void onNext(OperationReply reply) {
        System.out.println("Result = " + reply.getRes());
        operCount += 1;
    }

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println(String.format("You did %d operations", operCount));
    }
}
