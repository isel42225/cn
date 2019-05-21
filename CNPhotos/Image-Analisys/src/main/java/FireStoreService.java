import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FireStoreService {

    private final String projectId = ServiceOptions.getDefaultProjectId();

    private Firestore db;

    private void initialize() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();
        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    private void addData(String image, String label) throws ExecutionException, InterruptedException {
        Map<String, String> data = new HashMap<>();
        DocumentReference docRef = db.collection("photos").document(label);
        data.put(label, image);
        ApiFuture<WriteResult> result = docRef.set(data);
        System.out.println("Update time : " + result.get().getUpdateTime());
    }

}
