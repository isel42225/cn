import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.util.Collections;
import java.util.List;

public class VisionService {

    public void analyse(byte[] content) {
        try(ImageAnnotatorClient vision = ImageAnnotatorClient.create()){
            ByteString imgBytes = ByteString.copyFrom(content);
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feature = Feature.newBuilder()
                    .setType(Feature.Type.LABEL_DETECTION)
                    .setType(Feature.Type.FACE_DETECTION)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest
                    .newBuilder()
                    .addFeatures(feature)
                    .setImage(img)
                    .build();
            BatchAnnotateImagesResponse response = vision
                    .batchAnnotateImages(Collections.singletonList(request));
            List<AnnotateImageResponse> responsesList = response.getResponsesList();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
