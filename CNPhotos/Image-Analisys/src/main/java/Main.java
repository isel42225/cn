import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception{
        ImageWorker w = new ImageWorker();
        VisionService vision = new VisionService();
        w.work();
        System.out.println("Working....");
    }
}
