import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;

import java.io.*;
import java.util.HashMap;


public class App {
    private static final String KEY_JSON =
            "src\\main\\resources\\g08-leic61d-f27cb4b823e6.json";

    private static final String CSV_FILE =
            "src\\main\\resources\\OcupacaoEspacosPublicos-v2.csv";

    public static void main (String [] args) throws Exception {
        InputStream account = new FileInputStream(KEY_JSON);
        GoogleCredentials credentials = GoogleCredentials.fromStream(account);
        FirestoreOptions  options = FirestoreOptions
                .newBuilder()
                .setCredentials(credentials)
                .build();

        Firestore db = options.getService();

        FileInputStream csv = new FileInputStream(CSV_FILE);

        CollectionReference colRef = db.collection("ocupTemp");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csv))) {
            String line;
            while ((line = reader.readLine()) != null) {
                OcupacaoTemporaria temp = OcupacaoTemporaria.build(line);
                DocumentReference docRef = colRef.document(String.valueOf(temp.ID));
                ApiFuture<WriteResult> result = docRef.set(temp);
                // process fields here
                System.out.println(result.get());
            }
        }

    }

}
