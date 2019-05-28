import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import pubsub.PubSubManager;
import storage.BlobManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.List;

public class ImageWorker implements Runnable {
    private class MessageHandler implements MessageReceiver {

        @Override
        public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
            // Fetch image from storage
            // Go to Vision API
            // Submit results to firestore and storage
            try {
                monitorAgent.notifyWork();
                String filename = pubsubMessage.getData().toStringUtf8();
                byte[] content = blobManager.getBlobContent(filename);
                List<String> labels = visionService.analyse(content);
                BufferedImage imgWithFaces = visionService.detectFaces(content);
                if(imgWithFaces != null) {
                    //save on Storage
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(imgWithFaces,"jpg",baos);
                    byte [] byteFaces = baos.toByteArray();
                    String [] split = filename.split("\\.");
                    String facesFile = split[0]+"withFaces." + split[1];
                    blobManager.uploadBlob(byteFaces, facesFile);
                }

                // save labels on firestore
                for (String label: labels ) {
                    fireStoreService.addData(filename, label);
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                ackReplyConsumer.ack();
            }
        }
    }

    private final PubSubManager pubSubManager = new PubSubManager();
    private final BlobManager blobManager = new BlobManager();
    private final VisionService visionService = new VisionService();
    private final FireStoreService fireStoreService = new FireStoreService();
    private final MonitorAgent monitorAgent = new MonitorAgent();

    @Override
    public void run() {
        pubSubManager.subscribeToImg(new MessageHandler());
    }
}
