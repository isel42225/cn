import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class StorageFactory {

    //private static String KEY_PATH = "C:\\Users\\gonca\\ISEL\\Cadeiras\\18-19v\\CN\\Lab 5\\g08-leic61d-f27cb4b823e6.json";
    private static Storage storage = null;

    public static Storage get()  {
        if(storage == null) {
            storage = StorageOptions.getDefaultInstance().getService();
        }
        return storage;
    }

}
