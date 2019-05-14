public class ImageManager {

    private final BlobManager blobManager;

    public ImageManager() {
        this.blobManager = new BlobManager();
    }

    public void upload(String absFilename){
        System.out.println("Uploading...");
        boolean success = blobManager.uploadBlob(absFilename);
        if(success) {
           // notify PUB/SUB
        }
        else{
           // try again with exponential backOff
           // https://cloud.google.com/storage/docs/exponential-backoff
       }
    }
}
