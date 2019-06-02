

import com.google.cloud.storage.Blob;
import firestore.FireStoreService;
import pubsub.PubSubManager;
import storage.BlobManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageManager {

    private final BlobManager blobManager;
    private final PubSubManager pubSubManager;
    private final FireStoreService fireStoreService;

    public ImageManager() {
        this.blobManager = new BlobManager();
        this.pubSubManager = new PubSubManager();
        this.fireStoreService = new FireStoreService();

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
            pubSubManager.publishImg(filename);
        }

    }

    public void uploadFolder() {
        String rootDir = "C:\\Users\\gonca\\ISEL\\Cadeiras\\18-19v\\CN\\repo\\CNPhotos\\images";
        try (Stream<Path> paths = Files.walk(Paths.get(rootDir))) {
            paths
                    .filter(Files::isRegularFile)
            .forEach(p -> {
                boolean success = blobManager.uploadBlob(p);
                if(success) {
                    System.out.println("Upload completed");
                    String filename = p.getFileName().toString();
                    pubSubManager.publishImg(filename);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void search() throws IOException {
        Scanner scn = new Scanner(System.in);
        System.out.print("Category ?\n>");
        List<String> blobNames =  fireStoreService.searchImage(scn.nextLine());

        List<Blob> list = blobManager.getBlobs(blobNames);
        String downloadDir = System.getProperty("user.dir");
        for (Blob b: list) {
            Files.write(new File(downloadDir + b.getName()).toPath(), b.getContent());
        }
        System.out.println("Your images were dowloaded to " + downloadDir);
    }
}
