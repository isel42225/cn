import highwaystubs.CarInfo;
import highwaystubs.HighwayServiceGrpc;
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

    @Override
    public void enter(CarInfo request, StreamObserver<Void> responseObserver) {
        String licensePlate = request.getLicensePlate();
        int entrance = request.getEntranceOrExit();
        tolls.put(licensePlate, entrance);
        Void v = Void.newBuilder().build();
        responseObserver.onNext(v);
        responseObserver.onCompleted();
        logger.info(String.format("Car %s entered on entrance %d", licensePlate, entrance));
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
        responseObserver.onNext(toll);
        responseObserver.onCompleted();
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
