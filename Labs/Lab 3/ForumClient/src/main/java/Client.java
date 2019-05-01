import com.google.protobuf.ProtocolStringList;
import forumstubs.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.ApplicationProtocolConfig;

import java.util.Scanner;

public class Client {

    private static final String SV_IP = "localhost";
    private static final int SV_PORT = 8080;


    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(SV_IP,SV_PORT)
                .usePlaintext()
                .build();

        ForumServiceGrpc.ForumServiceStub stub =
                                                ForumServiceGrpc.newStub(channel);
        ForumServiceGrpc.ForumServiceBlockingStub bStub =
                                                ForumServiceGrpc.newBlockingStub(channel);

        SubscribeUnSubscribe gSubscription = SubscribeUnSubscribe
                .newBuilder()
                .setTopicName("CN")
                .setUsrName("Gonçalo")
                .build();


        stub.topicSubscribe(gSubscription, new SubscribeStreamObserver());

        //Force topic to exist to prevent NPE in message-publish
        Empty req = null;
        ProtocolStringList topics = null;
        do {
            req = Empty.newBuilder().build();
            topics = bStub.getAllTopics(req).getTopicNameList();

        }while (!topics.contains("CN"));


        ForumMessage message = ForumMessage
                .newBuilder()
                .setFromUser("Nuno")
                .setTopicName("CN")
                .setTxtMsg("Alguém trabalha ?")
                .build();

        stub.messagePublish(message, new EmptyStreamObserver());

        Scanner scn = new Scanner(System.in);
        scn.nextLine();
    }


}
