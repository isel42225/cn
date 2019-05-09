import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;

import java.util.ArrayList;
import java.util.List;

public class App {
    private static String APPLICATION_NAME = "APP";

    public static void main(String[] args) throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GoogleCredential googleCredential = GoogleCredential.getApplicationDefault();
        if (googleCredential.createScopedRequired()) {
            List<String> scopes = new ArrayList<>();
            scopes.add(ComputeScopes.COMPUTE);
            googleCredential = googleCredential.createScoped(scopes);
        }
        Compute computeService =
                new Compute.Builder(httpTransport, JacksonFactory.getDefaultInstance(), googleCredential)
                        .setApplicationName(APPLICATION_NAME)
                        .build();
    }
}

