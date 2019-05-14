import java.util.HashMap;

public class Client {

    private static HashMap<Integer, Command> commandHashMap = new HashMap<>();

    public static void main(String[] args) {

    }

    public void printCommands(){
        System.out.println("Available commands:");
        for(int idx : commandHashMap.keySet()){
            System.out.println(String.format("%d : %s", idx, commandHashMap.get(idx).commandTitle));
        }
        System.out.println("99 : Exit");
        System.out.print("> ");
    }
}
