import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.ComputeScopes;
import com.google.api.services.compute.model.*;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static HttpTransport httpTransport;
    private static GoogleCredential credential;
    private static Compute computeService;
    private static String PROJECT_ID = "g08-leic61d";


    public static void main(String[] args) throws GeneralSecurityException, IOException {
        setUp();
        printInstancesInfo(computeService);

    }



    static void printInstancesInfo(Compute compute) throws IOException {
        Compute.Instances.List instances =
                compute.instances().list(PROJECT_ID, "europe-west2-c");
        InstanceList list = instances.execute();
        if (list.getItems() != null) {
            for (Instance instance : list.getItems()) {
                //System.out.println(instance.toPrettyString());
                System.out.println(instance.getName());
            }
        }
    }
    static void setUp() throws GeneralSecurityException, IOException {
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        // Authenticate using Google Application Default Credentials.
        credential = GoogleCredential.getApplicationDefault();
        if (credential.createScopedRequired()) {
            List<String> scopes = new ArrayList<String>();
            scopes.add(ComputeScopes.COMPUTE);
            credential = credential.createScoped(scopes);
        }
        computeService =
                new Compute.Builder(httpTransport, JacksonFactory.getDefaultInstance(), credential)
                        .setApplicationName("app name")
                        .build();
    }

}

