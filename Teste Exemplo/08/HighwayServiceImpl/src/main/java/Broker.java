import highwaystubs.CarInfo;
import highwaystubs.HighwayServiceGrpc;
import highwaystubs.Notification;
import highwaystubs.Toll;
import highwaystubs.Void;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Broker extends HighwayServiceGrpc.HighwayServiceImplBase {
    private static final int PORT = 8080;
    private static final Logger logger = Logger.getAnonymousLogger();

    private final Map<String, Integer> tolls = new HashMap<>();
    private final Map<String, StreamObserver<Notification>> notifications = new HashMap<>();

    @Override
    public void enter(CarInfo request, StreamObserver<Notification> responseObserver) {
        String licensePlate = request.getLicensePlate();
        int entrance = request.getEntranceOrExit();
        tolls.put(licensePlate, entrance);

        notifications.put(licensePlate, responseObserver);
        logger.info(String.format("Car %s entered on entrance %d", licensePlate, entrance));
    }


    public void notifyCar() {
        for(Map.Entry<String, StreamObserver<Notification>> e : notifications.entrySet()){
            StreamObserver<Notification> value = e.getValue();
            Notification n = Notification
                    .newBuilder()
                    .setInfo("Vai chover")
                    .build();
            value.onNext(n);
        }
    }

    @Override
    public void leave(CarInfo request, StreamObserver<Toll> responseObserver) {
        String licensePlate = request.getLicensePlate();
        int exit = request.getEntranceOrExit();
        int entrance = tolls.get(licensePlate);
        String value = String.valueOf(exit * entrance);
        Toll toll = Toll
                .newBuilder()
                .setValue(String.format("%s €", value))
                .build();
        StreamObserver<Notification> observer = notifications.get(licensePlate);
        observer.onCompleted();
        notifications.remove(licensePlate);
        responseObserver.onNext(toll);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Void> test(StreamObserver<Void> responseObserver) {

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server svc = ServerBuilder
                .forPort(PORT)
                .addService(new Broker())
                .build()
                .start();
        System.out.println("Listening on port : " + PORT);
        svc.awaitTermination();
    }
}
