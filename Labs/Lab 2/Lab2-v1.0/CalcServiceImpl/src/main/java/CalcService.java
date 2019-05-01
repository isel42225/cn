import calcstubs.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CalcService extends CalcServiceGrpc.CalcServiceImplBase {

    private static final int SVC_PORT = 8080;
    private static final ExecutorService primePool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        try {
            Server svc = ServerBuilder
                    .forPort(SVC_PORT)
                    .addService(new CalcService())
                    .build()
                    .start();

            System.out.println("Server now listening on port " + SVC_PORT);
            System.out.println("Press any key to end...");

            Scanner scn = new Scanner(System.in);
            scn.nextLine();

            svc.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void add(OperationRequest request,
                    StreamObserver<OperationReply> responseObserver) {

        double op1 = request.getOp1();
        double op2 = request.getOp2();
        double res = op1 + op2;
        OperationReply reply = OperationReply
                .newBuilder()
                .setRes(res)
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

    public StreamObserver<OperationRequest> addAll(
            StreamObserver<OperationReply> responseObserver){

        return new AddRequestStreamObserver(responseObserver);
    }

    public StreamObserver<OperationRequest> addStream(
            StreamObserver<OperationReply> responseObserver){

        return new AddStreamRequestStreamObserver(responseObserver);
    }

    @Override
    public void findPrimes(NumOfPrimes request,
                           StreamObserver<Prime> responseObserver) {

        primePool.submit(() -> calcPrimes(request, responseObserver));
    }

    private void calcPrimes(NumOfPrimes request,
                            StreamObserver<Prime> responseObserver){
        int start = request.getStartNum();

        for(int i = 0 ; i < request.getNumOfPrimes(); ++i){
            if(isPrime(start)){
                Prime p = Prime
                        .newBuilder()
                        .setPrime(start)
                        .build();
                responseObserver.onNext(p);
            }
            start += 1;
        }
        responseObserver.onCompleted();
    }

    private boolean isPrime(int n){
        if(n % 2 == 0) return false;

        for(int i = 3 ; i < n; ++i){
            if(n % i == 0)return false;
        }
        return true;
    }
}
