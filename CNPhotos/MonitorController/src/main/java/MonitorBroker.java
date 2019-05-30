import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import monitorstubs.*;

import java.util.Scanner;

public class MonitorBroker extends MonitorServiceGrpc.MonitorServiceImplBase {

    private static int SVC_PORT = 8080;
    private static MonitorController monitorController = new MonitorController();

    public static void main(String[] args) {
        try{
            Server svc = ServerBuilder
                    .forPort(SVC_PORT)
                    .addService(new MonitorBroker())
                    .build()
                    .start();
            System.out.println("Server now listening on port " + SVC_PORT);
            System.out.println("Press any key to end...");

            Scanner scn = new Scanner(System.in);
            scn.nextLine();

            svc.shutdown();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setThreshold(SetRequest request, StreamObserver<SetResult> responseObserver) {
        int newThreshold = request.getImgPerMin();
        monitorController.setImgThreshold(newThreshold);
    }

    @Override
    public void getState(Empty request, StreamObserver<State> responseObserver) {
        MonitorState state = monitorController.getState();
        int nOfVms = state.getnOfVms();
        int currLoad = state.getCurrImgLoad();
        int currThreshold = state.getImgThreshold();
        State s = State
                .newBuilder()
                .setNOfVms(nOfVms)
                .setCurrLoad(""+currLoad)
                .setCurrThreshold(""+currThreshold)
                .build();
        responseObserver.onNext(s);
    }
}
