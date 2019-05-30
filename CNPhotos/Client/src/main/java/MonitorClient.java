import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import monitorstubs.Empty;
import monitorstubs.MonitorServiceGrpc;

public class MonitorClient {

    private static final String SV_IP = "localhost";
    private static final int SV_PORT = 8080;
    private final ManagedChannel channel;
    private final MonitorServiceGrpc.MonitorServiceStub monitorServiceStub;

    public MonitorClient() {
        channel = ManagedChannelBuilder
                .forAddress(SV_IP,SV_PORT)
                .usePlaintext()
                .build();
        monitorServiceStub = MonitorServiceGrpc.newStub(channel);
    }

    public void getMonitorState() {
        Empty req = Empty.newBuilder().build();
        monitorServiceStub.getState(req, new StateStreamObserver());
    }
}
