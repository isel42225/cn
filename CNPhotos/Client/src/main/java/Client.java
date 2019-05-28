import java.util.HashMap;
import java.util.Scanner;

public class Client {

    private static HashMap<Integer, Command> commandHashMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ImageManager imageManager = new ImageManager();
        commandHashMap.put(1, new ImageUploadCommand(imageManager));

        printCommands();
        int input = -1;
        while((input = scanner.nextInt()) != 99) {
            Command cmd = commandHashMap.get(input);
            cmd.execute();
            clearConsole();
            printCommands();
        }
    }

    private static void printCommands(){
        System.out.println("Available commands:");
        for(int idx : commandHashMap.keySet()){
            System.out.println(String.format("%d : %s", idx, commandHashMap.get(idx).commandTitle));
        }
        System.out.println("99 : Exit");
        System.out.print("> ");
    }

    private static void clearConsole()
    {
        for (int y = 0; y < 25; y++) {
            System.out.println("\n");
        }

    }

}
