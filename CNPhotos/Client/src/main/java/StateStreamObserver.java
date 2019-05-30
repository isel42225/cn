import io.grpc.stub.StreamObserver;
import monitorstubs.State;

public class StateStreamObserver implements StreamObserver<State> {
    @Override
    public void onNext(State state) {
        String currLoad = state.getCurrLoad();
        String currThreshold = state.getCurrThreshold();
        int nOfVms = state.getNOfVms();
        System.out.println(
                String.format("Monitor current Load: %s\nMonitor currThreshold: %s\n Monitor vm number:%d", currLoad, currThreshold, nOfVms)
        );
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {
        System.out.println("State stream ended");
    }
}
