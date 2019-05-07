import forum.ForumGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public abstract class Command {
    protected final ManagedChannel channel ;
    private static String FORUM_IP = "35.189.85.111";
    private static int FORUM_PORT = 8000;

    public Command() {
        channel = ManagedChannelBuilder
                .forAddress(FORUM_IP, FORUM_PORT)
                .usePlaintext()
                .build();
    }
    abstract void execute();

    protected ForumGrpc.ForumStub getAsyncStub() {
        return ForumGrpc.newStub(channel);
    }

    protected ForumGrpc.ForumBlockingStub getSyncStub() {
        return ForumGrpc.newBlockingStub(channel);
    }
}
