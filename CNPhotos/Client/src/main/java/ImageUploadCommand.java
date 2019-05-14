
public class ImageUploadCommand  extends  Command{
    private static final String COMMAND_TITLE = "Upload an image";
    private final ImageManager imageManager;

    public ImageUploadCommand(ImageManager imageManager) {
        super(COMMAND_TITLE);
        this.imageManager = imageManager;
    }

    @Override
    public void execute() {
        imageManager.upload();
    }
}
