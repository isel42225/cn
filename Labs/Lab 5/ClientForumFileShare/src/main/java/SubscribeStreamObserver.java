import forum.ForumMessage;
import io.grpc.stub.StreamObserver;

public class SubscribeStreamObserver implements StreamObserver<ForumMessage> {
    private static String SAVE_PATH =
            "C:\\Users\\gonca\\ISEL\\Cadeiras\\18-19v\\CN\\Lab 5\\download\\";

    private final BlobManager bm;

    public SubscribeStreamObserver() {
        bm = new BlobManager();
    }

    @Override
    public void onNext(ForumMessage forumMessage) {
        String msg = forumMessage.getTxtMsg();
        String [] splitted = msg.split(";");
        if(splitted.length > 2){
            String bucket = splitted[1];
            String blobName = splitted[2];
            bm.downloadTo(bucket, blobName, SAVE_PATH);
        }
        System.out.println(
                String.format("%s in %s\n\t%s", forumMessage.getFromUser(), forumMessage.getTopicName(), msg)
        );
    }

    @Override
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @Override
    public void onCompleted() {

    }

}
