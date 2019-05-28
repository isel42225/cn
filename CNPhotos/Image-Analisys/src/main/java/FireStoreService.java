
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.firestore.DocumentReference;

import javax.swing.text.Document;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FireStoreService {

    private final String projectId = ServiceOptions.getDefaultProjectId();
    private final String collection = "photos";
    private Firestore db;


    public FireStoreService() {
        try {
            initialize();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialize() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        db = FirestoreOptions.getDefaultInstance()
                .toBuilder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build()
                .getService();

    }

    public void addData(String image, String label)  {
        Map<String, String> data = new HashMap<String, String> () {
            {
                put(image, image);
            }
        };
        DocumentReference docRef = db.collection(collection).document(label);
        docRef.set(data, SetOptions.merge());
    }
    public Map searchImage(String label) throws ExecutionException, InterruptedException {
        DocumentReference dr = db.collection(collection).document(label);
        ApiFuture<DocumentSnapshot> future = dr.get();
        DocumentSnapshot document = future.get();
        if(document.exists()){
            return document.getData();
        }
        else{
            System.out.println("No images available");
            return null;
        }
    }

}
