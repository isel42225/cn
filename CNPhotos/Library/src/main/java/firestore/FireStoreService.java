package firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.ServiceOptions;
import com.google.cloud.firestore.*;

import java.io.IOException;
import java.util.*;

public class FireStoreService {

    private final String projectId = ServiceOptions.getDefaultProjectId();
    private final String collection = "photos";
    private Firestore db;

    private Set<String> labelCache = new HashSet<>();


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
        try {
            DocumentReference docRef = db.collection(collection).document(label);
            if (!labelCache.contains(label)) {
                ApiFuture<WriteResult> future = docRef.set(initImgCollection());
                future.get();
            }

            docRef.update("images", FieldValue.arrayUnion(image));

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, List<String>> initImgCollection() {
        return new HashMap<String, List<String>>() {
            {
                put("images", new ArrayList<>());
            }
        };
    }

    public List<String> searchImage(String label) {
        try {
            DocumentReference dr = db.collection(collection).document(label);
            ApiFuture<DocumentSnapshot> future = dr.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                List<String> ret = new ArrayList<>(document.getData().keySet());
                return ret;
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
