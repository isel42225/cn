import calcstubs.CalcServiceGrpc;
import calcstubs.NumOfPrimes;
import calcstubs.OperationRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;

public class Client {

    private static final String SV_IP = "localhost";
    private static final int SV_PORT = 8080;


    public static void main(String[] args) {
        /*syncAdd();
        asyncAdd();
        asyncAddAll();
        asyncAddStream();*/
        asyncFindPrimesInInterval();


        Scanner scn = new Scanner(System.in);
        scn.nextLine();
    }

    private static void syncAdd(){
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SV_IP,SV_PORT)
                .usePlaintext()
                .build();

        double op1 = 1;
        double op2 = 1;

        OperationRequest request = OperationRequest
                .newBuilder()
                .setOp1(op1)
                .setOp2(op2)
                .build();

        CalcServiceGrpc.CalcServiceBlockingStub syncStub =
                CalcServiceGrpc.newBlockingStub(channel);

        double res = syncStub.add(request).getRes();
        System.out.println(
                String.format("Result(blocking) of %f + %f = %f", op1, op2, res)) ;
    }

    private static void asyncAdd(){
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SV_IP,SV_PORT)
                .usePlaintext()
                .build();

        CalcServiceGrpc.CalcServiceStub asyncStub =
                CalcServiceGrpc.newStub(channel);

        double op1 = 1;
        double op2 = 1;

        OperationRequest request = OperationRequest
                .newBuilder()
                .setOp1(op1)
                .setOp2(op2)
                .build();

        ReplyStreamObserver observer = new ReplyStreamObserver(op1, op2);
        asyncStub.add(request, observer);

        System.out.println("Calculating (async)....");
    }

    private static void asyncAddAll(){
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SV_IP,SV_PORT)
                .usePlaintext()
                .build();

        CalcServiceGrpc.CalcServiceStub asyncStub =
                CalcServiceGrpc.newStub(channel);

        StreamObserver<OperationRequest> requestStream =
                asyncStub.addAll(new AddAllReplyStreamObserver());

        OperationRequest req1 = OperationRequest
                .newBuilder()
                .setOp1(1)
                .setOp2(1)
                .build();
        OperationRequest req2 = OperationRequest
                .newBuilder(req1)
                .setOp1(2)
                .setOp2(2)
                .build();

        requestStream.onNext(req1);
        System.out.println("Sent 1 + 1");

        requestStream.onNext(req2);
        System.out.println("Sent 2 + 2");

        requestStream.onCompleted();
        System.out.println("Getting total sum...");
    }

    private static void asyncAddStream(){
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SV_IP,SV_PORT)
                .usePlaintext()
                .build();

        CalcServiceGrpc.CalcServiceStub asyncStub =
                CalcServiceGrpc.newStub(channel);

        StreamObserver<OperationRequest> requestStream =
                asyncStub.addStream(new AddStreamReplyStreamObserver());

        Scanner scn = new Scanner(System.in);

        System.out.println("Write your sums (num + num) or 'done' to end");

        String input = "";
        while(!(input = scn.nextLine()).equals("done")){
            String [] opers = input.trim().split("\\+");
            double op1 = Double.valueOf(opers[0]);
            double op2 = Double.valueOf(opers[1]);

            OperationRequest req = OperationRequest
                    .newBuilder()
                    .setOp1(op1)
                    .setOp2(op2)
                    .build();
            requestStream.onNext(req);
        }
        requestStream.onCompleted();

    }

    private static void asyncFindPrimesInInterval(){
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SV_IP,SV_PORT)
                .usePlaintext()
                .build();

        CalcServiceGrpc.CalcServiceStub asyncStub =
                CalcServiceGrpc.newStub(channel);

        NumOfPrimes first50 = NumOfPrimes
                .newBuilder()
                .setNumOfPrimes(50)
                .setStartNum(1)
                .build();

        NumOfPrimes last50 = NumOfPrimes
                .newBuilder()
                .setNumOfPrimes(50)
                .setStartNum(50)
                .build();

        asyncStub.findPrimes(first50, new PrimesInIntervalReplyStreamObserver(1,49));
        asyncStub.findPrimes(last50, new PrimesInIntervalReplyStreamObserver(50, 100));


    }
}
