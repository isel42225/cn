import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.firestore.*;
import com.google.cloud.firestore.v1.FirestoreAdminClient;
import com.google.cloud.firestore.v1.FirestoreClient;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FireStoreService {

    private final String projectId = ServiceOptions.getDefaultProjectId();

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

    public void addData(String image, String label) throws ExecutionException, InterruptedException {
        // TODO put collection instead of string
        Map<String, String> data = new HashMap<String, String> () {
            {
                put(image, image);
            }
        };
        DocumentReference docRef = db.collection("photos").document(label);
        docRef.set(data, SetOptions.merge());



    }

}
