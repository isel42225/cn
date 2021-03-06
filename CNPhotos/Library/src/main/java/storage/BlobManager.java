package storage;

import com.google.cloud.WriteChannel;
import com.google.cloud.storage.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class BlobManager {
    private final String BUCKET_NAME = "cn-photos-g08";
    private final long FILE_SIZE_THRESHOLD = 1_000_000;
    private final int BUFFER_SIZE = 4 * 1024;
    private final Storage storage = StorageOptions.getDefaultInstance().getService();


    public byte [] getBlobContent(String name) {
        BlobId blobId = BlobId.of(BUCKET_NAME, name);
        Blob blob = storage.get(blobId);
        byte [] content =  blob.getContent();
        return content;
    }

    public List<Blob> getBlobs(List<String> blobNames){
        List<BlobId> blobIds = blobNames
                .stream()
                .map(name -> BlobId.of(BUCKET_NAME, name))
                .collect(Collectors.toList());
        List<Blob> blobList = storage.get(blobIds);
        return blobList;
    }


    public boolean uploadBlob(Path filePath){
        try {
            String contentType = Files.probeContentType(filePath);
            String filename = filePath.getFileName().toString();
            BlobId bId = BlobId.of(BUCKET_NAME, filename);
            BlobInfo bInfo = BlobInfo.newBuilder(bId).setContentType(contentType).build();
            long fileSize = Files.size(filePath);
            if(fileSize < FILE_SIZE_THRESHOLD){
                return smallUpload(filePath, bInfo);
            }
            else{
                return bigUpload(filePath, bInfo);
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean uploadBlob(byte [] content, String filename) {
        String contentType = "image/jpeg";
        BlobId blobId = BlobId.of(BUCKET_NAME, filename);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        storage.create(blobInfo, content);
        return true;
    }

    private boolean bigUpload(Path filePath, BlobInfo bInfo) {
        try(WriteChannel channel = storage.writer(bInfo)){
            byte [] buffer = new byte[BUFFER_SIZE];
            try(InputStream inputStream = Files.newInputStream(filePath)){
                int limit;
                while((limit = inputStream.read(buffer)) > 0) {
                    channel.write(ByteBuffer.wrap(buffer, 0 , limit));
                }
                return true;
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean smallUpload(Path filePath, BlobInfo bInfo) {
        try {
            byte[] content = Files.readAllBytes(filePath);
            storage.create(bInfo, content);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
