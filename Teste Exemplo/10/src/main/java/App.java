import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class App {
    private static final String VENDOR = "42225";
    private static final String MONTH = "06";
    private static final String MOCK_FILE = "42225_4_4.csv";
    private static final String COLLECTION = "vendors";
    private static final String BUCKET = "cn-g08-vendas";
    private static final Map<String, List<String>> mockFiles = new HashMap<>();


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        realImpl();

    }

    private static void realImpl() throws ExecutionException, InterruptedException {
        Storage storage = StorageOptions.getDefaultInstance().getService();
        List<Blob> monthlyBlobs = new ArrayList<>();
        for(Blob b : storage.list(BUCKET).iterateAll()){
            String [] split = b.getName().split("[_.]");
            String vendor = split[0];
            String month = split[2];
            if(vendor.equals(VENDOR) && month.equals(MONTH)){
                monthlyBlobs.add(b);
            }
        }

        Map<String, MonthlySale> monthlySales = new HashMap<>();
        for(Blob b : monthlyBlobs) {
            byte[] content = b.getContent();
            String c = new String(content);
            String [] lines = c.replaceAll("\r", "").split("\n");
            for(String l : lines) {
                String [] saleInfo = l.split(",");
                String prodId = saleInfo[0];
                int quantity = Integer.parseInt(saleInfo[1]);
                int value = Integer.parseInt(saleInfo[2]);
                int discount = Integer.parseInt(saleInfo[3]);
                int total = Integer.parseInt(saleInfo[4]);
                MonthlySale ms =  monthlySales.computeIfAbsent(prodId, (unused) -> new MonthlySale());
                ms.addNewSale(quantity, value, discount, total);
            }
        }

        saveOnFirestore(monthlySales);
    }

    private static void mockImpl() throws ExecutionException, InterruptedException {
        initMock();
        List<String> sales = mockFiles.get(MOCK_FILE);
        Map<String, MonthlySale> monthlySales = new HashMap<>();
        for(String s : sales) {
            String [] saleInfo = s.split(",");
            String prodId = saleInfo[0];
            int quantity = Integer.parseInt(saleInfo[1]);
            int value = Integer.parseInt(saleInfo[2]);
            int discount = Integer.parseInt(saleInfo[3]);
            int total = Integer.parseInt(saleInfo[4]);
            MonthlySale ms =  monthlySales.computeIfAbsent(prodId, (unused) -> new MonthlySale());
            ms.addNewSale(quantity, value, discount, total);
        }

        saveOnFirestore(monthlySales);
    }


    private static void saveOnFirestore(Map<String, MonthlySale> sales) throws ExecutionException, InterruptedException {
        Firestore firestore = FirestoreOptions.getDefaultInstance().getService();
        ApiFuture<WriteResult> write = firestore.collection(COLLECTION).document(VENDOR).set(sales);
        write.get();
    }

    // template : <codProd, quantity, value, discount, total
    private static void initMock() {
        String [] arr = {
                "1,4,5,0,20",
                "3,1,2,0,2",
                "4,10,1,20,8",
                "1,3,7,0,21",
        };
        mockFiles.put(MOCK_FILE, Arrays.asList(arr));
    }
}
