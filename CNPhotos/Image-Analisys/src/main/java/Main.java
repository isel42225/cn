import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ImageWorker w = new ImageWorker();
        w.run();
        Scanner scn = new Scanner(System.in);
        System.out.println("Press any key to end...");
        scn.next();
    }
}
