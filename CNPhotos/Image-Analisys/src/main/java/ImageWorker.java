import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;
import firestore.FireStoreService;
import pubsub.PubSubManager;
import storage.BlobManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ImageWorker implements MessageReceiver{

    private final PubSubManager pubSubManager = new PubSubManager();
    private final BlobManager blobManager = new BlobManager();
    private final VisionService visionService = new VisionService();
    private final FireStoreService fireStoreService = new FireStoreService();
    private final MonitorAgent monitorAgent = new MonitorAgent();


    public void work() {
        pubSubManager.subscribeToImg(this);
    }

    @Override
    public void receiveMessage(PubsubMessage pubsubMessage, AckReplyConsumer ackReplyConsumer) {
        // Fetch image from storage
        // Go to Vision API
        // Submit results to firestore and storage
        try {
            long start = System.currentTimeMillis();
            System.out.println("Message Handler enter");
            monitorAgent.notifyWork();
            System.out.println("Notified Monitor");
            String filename = pubsubMessage.getData().toStringUtf8();
            byte[] content = blobManager.getBlobContent(filename);
            System.out.println("Got Image");
            List<String> labels = visionService.analyse(content);
            System.out.println("Analysed image labels");
            BufferedImage imgWithFaces = visionService.detectFaces(content);
            System.out.println("Analysed image for faces");
            if(imgWithFaces != null) {
                saveImgWithFaces(imgWithFaces, filename);
                System.out.println("Drew square in face");
            }
            // save labels on firestore
            for (String label: labels ) {
                fireStoreService.addData(filename, label);
                System.out.println("Added labels to firestore");
            }
            long end = System.currentTimeMillis() - start;
            System.out.println("Analysis took : " + end + " ms");
            System.out.println("Message Handler leave!");
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            ackReplyConsumer.ack();
        }
    }

    private void saveImgWithFaces(BufferedImage img, String filename) throws IOException {
        //save on Storage
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img,"jpg",baos);
        byte [] byteFaces = baos.toByteArray();
        String [] split = filename.split("\\.");
        String facesFile = split[0]+"withFaces." + split[1];
        blobManager.uploadBlob(byteFaces, facesFile);
    }
}
