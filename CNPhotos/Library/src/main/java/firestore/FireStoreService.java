package firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.firestore.*;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    public void addData(String image, String label) {
        // TODO put collection instead of string
        Map<String, String> data = new HashMap<String, String> () {
            {
                put(image, image);
            }
        };
        DocumentReference docRef = db.collection(collection).document(label);
        docRef.set(data, SetOptions.merge());
    }

    public Map searchImage(String label) {
        try {
            DocumentReference dr = db.collection(collection).document(label);
            ApiFuture<DocumentSnapshot> future = dr.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.getData();
            } else {
                System.out.println("No images available");
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
