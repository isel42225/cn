public class ImageFolderUploadCommand extends Command {

    private static final String COMMAND_TITLE = "Upload an image folder ";
    private final ImageManager imageManager;

    public ImageFolderUploadCommand(ImageManager imageManager) {
        super(COMMAND_TITLE);
        this.imageManager = imageManager;
    }


    @Override
    public void execute() {
        imageManager.uploadFolder();
    }
}
