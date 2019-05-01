import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class StorageFactory {

    private static String KEY_PATH = "C:\\Users\\gonca\\ISEL\\Cadeiras\\18-19v\\CN\\Lab 5\\g08-leic61d-f27cb4b823e6.json";
    private static Storage storage = null;

    public static Storage get() throws IOException {
        if(storage == null) {
            storage = StorageOptions
                        .newBuilder()
                        .setCredentials(getCredentials())
                        .build()
                        .getService();
        }
        return storage;
    }

    private static GoogleCredentials getCredentials() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream(KEY_PATH));
    }
}
