import highwaystubs.CarInfo;
import highwaystubs.HighwayServiceGrpc;
import highwaystubs.Toll;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class App {
    private static final String HOST = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(HOST, PORT)
                .usePlaintext()
                .build();
        //HighwayServiceGrpc.HighwayServiceStub stub = HighwayServiceGrpc.newStub(channel);
        HighwayServiceGrpc.HighwayServiceBlockingStub stub = HighwayServiceGrpc.newBlockingStub(channel);
        String licensePlate = "22-12-FD";
        CarInfo carEnter = CarInfo
                .newBuilder()
                .setEntranceOrExit(2)
                .setLicensePlate(licensePlate)
                .build();
        //stub.enter(carEnter,new VoidStreamObserver());
        stub.enter(carEnter);
        //Thread.sleep(2000);
        CarInfo carExit = CarInfo
                .newBuilder()
                .setEntranceOrExit(3)
                .setLicensePlate(licensePlate)
                .build();
        //stub.leave(carExit, new TollStreamObserver());
        Toll toll = stub.leave(carExit);
        String toPay = toll.getValue();
        System.out.println(String.format("A pagar : %s", toPay));
        //Thread.sleep(2000);
    }
}
