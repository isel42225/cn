import pubsub.PubSubManager;
import storage.BlobManager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ImageManager {

    private final BlobManager blobManager;
    private final PubSubManager pubSubManager;

    public ImageManager() {
        this.blobManager = new BlobManager();
        this.pubSubManager = new PubSubManager();

    }

    public void upload(){
        Scanner scn = new Scanner(System.in);
        System.out.println("File (absolute path)?");
        System.out.print("> ");
        String absFilename = scn.nextLine();
        Path filePath = Paths.get(absFilename);
        System.out.println("Uploading...");
        boolean success = blobManager.uploadBlob(filePath);
        if(success) {
            System.out.println("Upload completed");
            String filename = filePath.getFileName().toString();
            pubSubManager.notify(filename);
        }
        else{
           // try again with exponential backOff
           // https://cloud.google.com/storage/docs/exponential-backoff
       }
    }
}
