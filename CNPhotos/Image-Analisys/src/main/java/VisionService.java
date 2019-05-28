import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Image;
import com.google.protobuf.ByteString;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisionService {

    public static final double SCORE_THRESHOLD = 0.85;

    public List<String> analyse(byte[] content) {
        try(ImageAnnotatorClient vision = ImageAnnotatorClient.create()){
            ByteString imgBytes = ByteString.copyFrom(content);
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feature = Feature.newBuilder()
                    .setType(Feature.Type.LABEL_DETECTION)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest
                    .newBuilder()
                    .addFeatures(feature)
                    .setImage(img)
                    .build();
            BatchAnnotateImagesResponse response = vision
                    .batchAnnotateImages(Collections.singletonList(request));
            AnnotateImageResponse imgResponse = response.getResponsesList().get(0);
            List<String> res = new ArrayList<>();
            for(EntityAnnotation annotation : imgResponse.getLabelAnnotationsList()){
                if(annotation.getScore() > SCORE_THRESHOLD)
                    res.add(annotation.getDescription());
            }
            return res;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage detectFaces(byte[] content) {
        try(ImageAnnotatorClient vision = ImageAnnotatorClient.create()){
            ByteString imgBytes = ByteString.copyFrom(content);
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feature = Feature.newBuilder()
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
            if(responsesList.size() == 0){
                return null;
            }
            AnnotateImageResponse imageResponse = responsesList.get(0);
            ByteArrayInputStream in = new ByteArrayInputStream(content);
            BufferedImage bImg = ImageIO.read(in);
            for(FaceAnnotation annotation : imageResponse.getFaceAnnotationsList()){
                annotateWithFace(bImg,annotation);
            }
            return bImg;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static void annotateWithFace(BufferedImage img, FaceAnnotation face) {
        Graphics2D gfx = img.createGraphics();
        Polygon poly = new Polygon();
        for (Vertex vertex : face.getFdBoundingPoly().getVerticesList()) {
            poly.addPoint(vertex.getX(), vertex.getY());
        }
        gfx.setStroke(new BasicStroke(5));
        gfx.setColor(new Color(0x00ff00));
        gfx.draw(poly);
    }
}
