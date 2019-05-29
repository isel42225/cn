import io.grpc.stub.StreamObserver;
import monitorstubs.*;

public class MonitorBroker extends MonitorServiceGrpc.MonitorServiceImplBase {

    @Override
    public void setThreshold(SetRequest request, StreamObserver<SetResult> responseObserver) {

    }

    @Override
    public void getState(Empty request, StreamObserver<State> responseObserver) {

    }
}
