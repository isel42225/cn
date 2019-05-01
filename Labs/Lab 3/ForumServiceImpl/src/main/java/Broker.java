import forumstubs.*;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// TODO Alterar concurrent para hashmap e usar locks

public class Broker extends ForumServiceGrpc.ForumServiceImplBase {
    private class User{
        public final String name;
        public final StreamObserver<ForumMessage> channel;

        public User(String name, StreamObserver<ForumMessage> channel){
            this.name = name;
            this.channel = channel;
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof User)) return false;
            if(((User) obj).name.equals(this.name))return true;
            return false;
        }
    }

    private static final int SVC_PORT = 8080;

    private final Map<String, List<User>> topicSubscribers = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try {
            Server svc = ServerBuilder
                    .forPort(SVC_PORT)
                    .addService(new Broker())
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
    public void topicSubscribe(SubscribeUnSubscribe request,
                               StreamObserver<ForumMessage> responseObserver) {
        String user = request.getUsrName();
        String topic = request.getTopicName();
        topicSubscribers.computeIfAbsent(topic, (key) -> new ArrayList<>());
        List<User> subscribers  = topicSubscribers.get(topic);
        subscribers.add(new User(user, responseObserver));
        //System.out.println("Subscribe " + Instant.now() + " " + topicSubscribers.entrySet());
    }

    @Override
    public void topicUnSubscribe(SubscribeUnSubscribe request, StreamObserver<ForumMessage> responseObserver) {
        String name = request.getUsrName();
        String topic = request.getTopicName();
        List<User> users = topicSubscribers.get(topic);
        User user  =  users.stream()
                .filter(u -> u.name.equals(name))
                .findFirst().get();

        user.channel.onCompleted();
        users.remove(user);
    }

    @Override
    public void getAllTopics(Empty request, StreamObserver<ExistingTopics> responseObserver) {
        ExistingTopics topics = ExistingTopics
                .newBuilder()
                .addAllTopicName(topicSubscribers.keySet())
                .build();
        responseObserver.onNext(topics);
        responseObserver.onCompleted();
    }

    @Override
    public void messagePublish(ForumMessage request, StreamObserver<Empty> responseObserver) {
        List<User> users = topicSubscribers.get(request.getTopicName());
        //System.out.println("Publish " + Instant.now() + " " + users);

        for(User u : users){
            try {
                u.channel.onNext(request);
            }catch (Exception e){
                users.remove(u);
            }
        }
        Empty empty = Empty.newBuilder().build();
        responseObserver.onNext(empty);
        responseObserver.onCompleted();

    }
}
