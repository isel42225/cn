import com.google.cloud.ReadChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BlobManager {

    public static final String CACHE_KEY = "valid-until";
    private final List<Blob> blobs = new ArrayList<>();
    private Storage storage;


    public BlobManager() {
        try {
            this.storage = StorageFactory.get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Check if blob is in cache, Get blob, download to File
     * @param bucket
     * @param name
     * @param path
     * @return
     */
    public Blob downloadTo(String bucket, String name, String path) {
        Blob blob = null;
        if((blob = find(bucket, name)) != null)
            return blob;
        BlobId bId = BlobId.of(bucket, name);
        blob = storage.get(bId);
        blob = blob
                .toBuilder()
                .setMetadata(buildCacheMetadata())
                .build();
        blobs.add(blob);
        saveToFile(blob, path);
        return blob;
    }

    private Blob find(String bucket, String name){
        for(Blob b : blobs){
            if(b.getName().equals(name) && b.getBucket().equals(bucket) && b.getMetadata().containsKey(CACHE_KEY)){
                long now = System.currentTimeMillis();
                long validTime = Long.parseLong(b.getMetadata().get(CACHE_KEY));
                if(now > validTime) {
                    blobs.remove(b);
                    return null;
                }
                return b;
            }
        }
        return null;
    }

    private void saveToFile(Blob blob, String path){
        try {
            Path downloadTo = Paths.get(path+blob.getName());
            PrintStream writeTo = new PrintStream(
                    new FileOutputStream(downloadTo.toFile()));
            if(blob.getSize() < 1000000000){
                writeTo.write(blob.getContent());
            }
            else{
                try(ReadChannel reader = blob.reader()){
                    WritableByteChannel channel = Channels.newChannel(writeTo);
                    ByteBuffer buffer = ByteBuffer.allocate(256 * 1024);
                    while(reader.read(buffer) > 0) {
                        channel.write(buffer);
                        buffer.clear();
                    }
                }
            }
            writeTo.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uploadFrom(String bucket, String blob, String path) throws IOException {
        Path uploadFrom = Paths.get(path + blob);
        String contentType = Files.probeContentType(uploadFrom);
        BlobId bId = BlobId.of(bucket, blob);
        BlobInfo bInfo = BlobInfo
                .newBuilder(bId)
                .setMetadata(buildCacheMetadata())
                .setContentType(contentType)
                .build();

        Blob created = storage.create(bInfo);
        blobs.add(created);
    }

    private Map<String, String> buildCacheMetadata() {
        long curr = System.currentTimeMillis();
        long validTime = curr + (3600 * 1000);
        return Collections.singletonMap(CACHE_KEY, ""+validTime);
    }
}

