import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Worker w = new Worker();
        w.run();
        Scanner scn = new Scanner(System.in);
        System.out.println("Press any key to end...");
        scn.next();
    }
}
