import java.net.*;
import java.io.*;
import java.util.*;

public class SerialWordSearch {
    public static String process(String request) throws InterruptedException {
        String[] tmp = request.split(" ");
        String bigWord = "";
        for (String s : tmp) {
            if (s.length() > bigWord.length()) {
                bigWord = s;
            }   
        }
        // simula tempo de processamento
        Thread.sleep(10*1000);
        // ...
        return bigWord;
    }
    public static void main(String[] args) throws Exception {
        int svcPort=4000;
        ServerSocket svcSocket = new ServerSocket(svcPort);
        System.out.println("Now listening on port 4000");
        for(;;) {
            try {
                //block until receive connection
                Socket cliSocket = svcSocket.accept();
                System.out.println(cliSocket);
                BufferedReader inStream = new BufferedReader(
                      new InputStreamReader(cliSocket.getInputStream())
                );
                PrintWriter outStream = new PrintWriter(cliSocket.getOutputStream(), true);
                String request = inStream.readLine();  //receive the request
                String response = process(request);
                outStream.println("biggest word: " + response + " size = " + response.length()); // send the reply
                cliSocket.close();
            } catch (IOException ex) {
                System.out.println("Server crashed!"); System.exit(-1); 
            }
        }
    }

}