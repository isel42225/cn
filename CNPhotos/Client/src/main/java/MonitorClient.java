import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import monitorstubs.Empty;
import monitorstubs.MonitorServiceGrpc;
import monitorstubs.SetRequest;

import java.util.Scanner;

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

    public void setThreshold() {
        Scanner scn = new Scanner(System.in);
        System.out.print("New threshold : ");
        int threshold = scn.nextInt();
        SetRequest request = SetRequest.
                newBuilder()
                .setImgPerMin(threshold)
                .build();
        monitorServiceStub.setThreshold(request, new SetResultStreamObserver());
    }
}
