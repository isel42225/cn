import java.util.HashMap;
import java.util.Scanner;

public class Client {

    private static HashMap<Integer, Command> commandHashMap = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printCommands();
        int input = -1;
        while((input = scanner.nextInt()) != 99) {
            Command cmd = commandHashMap.get(input);
            cmd.execute();
        }
    }

    public static void printCommands(){
        System.out.println("Available commands:");
        for(int idx : commandHashMap.keySet()){
            System.out.println(String.format("%d : %s", idx, commandHashMap.get(idx).commandTitle));
        }
        System.out.println("99 : Exit");
        System.out.print("> ");
    }

}
