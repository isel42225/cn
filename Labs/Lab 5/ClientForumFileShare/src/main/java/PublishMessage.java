import com.google.cloud.WriteChannel;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import forum.ForumGrpc;
import forum.ForumMessage;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class PublishMessage extends Command {
    private static String UPLOAD_PATH =
            "C:\\Users\\gonca\\ISEL\\Cadeiras\\18-19v\\CN\\Lab 5\\upload\\";
    private final ForumGrpc.ForumStub stub;
    private final String user ;

    public PublishMessage(String user) {
        super();
        this.stub = getAsyncStub();
        this.user = user;
    }

    @Override
    void execute() {
        Scanner scn = new Scanner(System.in);
        System.out.println("Topic?");
        String topic = scn.nextLine();
        System.out.println("Write your message (<text>[;<bucket>;<blob>]");
        String msg = scn.nextLine();
        String [] parts = msg.split(";");

        if(parts.length > 2) {
            String bucket = parts[1];
            String blob = parts[2];
            uploadBlob(bucket, blob);
        }

        ForumMessage message = ForumMessage
                .newBuilder()
                .setFromUser(user)
                .setTopicName(topic)
                .setTxtMsg(msg)
                .build();
        stub.messagePublish(message, new EmptyStreamObserver());
    }

    private void uploadBlob(String bucket, String blob) {
        try {
            Path uploadFrom = Paths.get(UPLOAD_PATH + blob);
            String contentType = Files.probeContentType(uploadFrom);
            Storage storage = StorageFactory.get();
            BlobId bId = BlobId.of(bucket, blob);
            BlobInfo bInfo = BlobInfo
                    .newBuilder(bId)
                    .setContentType(contentType)
                    .build();

            if(Files.size(uploadFrom) < 1000000000){
                byte [] bytes = Files.readAllBytes(uploadFrom);
                storage.create(bInfo,bytes);
            }
            else{
                try(WriteChannel writer = storage.writer(bInfo)){
                    byte [] buffer = new byte[1024 * 10];
                    try(InputStream input = Files.newInputStream(uploadFrom)){
                        int limit;
                        while((limit = input.read(buffer)) >= 0) {
                            writer.write(ByteBuffer.wrap(buffer));
                        }
                    }
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
