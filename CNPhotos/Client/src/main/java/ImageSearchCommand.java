import java.io.IOException;

public class ImageSearchCommand extends Command {

    public static final String CommandTitle = "Search an image";
    private final ImageManager imageManager;

    public ImageSearchCommand(ImageManager imageManager) {
        super(CommandTitle);
        this.imageManager = imageManager;
    }

    @Override
    public void execute() {
        try {
            imageManager.search();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
