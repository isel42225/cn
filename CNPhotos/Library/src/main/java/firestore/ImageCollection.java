package firestore;

import java.util.List;

public class ImageCollection {

    private List<String> images;
    public ImageCollection() {

    }

    public ImageCollection(List<String> images) {
        this.images = images;
    }

    public List<String> getImages() {
        return  images;
    }
}
