import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import pubsub.PubSubManager;
import storage.BlobManager;

public class Worker implements Runnable {
    private class MessageHandler implements MessageReceiver {
        @Override
        public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
            // Fetch image from storage
            // Go to Vision API
            // Submit results to firestore and storage
            try {
                String filename = pubsubMessage.getData().toStringUtf8();
                byte[] content = blobManager.getBlobContent(filename);
                visionService.analyse(content);
                ackReplyConsumer.ack();
            }catch (Exception e) {
                ackReplyConsumer.ack();
            }
        }
    }

    private final PubSubManager pubSubManager = new PubSubManager();
    private final BlobManager blobManager = new BlobManager();
    private final VisionService visionService = new VisionService();

    @Override
    public void run() {
        pubSubManager.subscribe(new MessageHandler());
    }
}
