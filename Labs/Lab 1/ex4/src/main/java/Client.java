import contracts.IMessagingService;
import contracts.IMsgBox;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Scanner;

public class Client {

    private static final String SV_IP = //"localhost";
                                //"10.62.73.29";
                                "10.62.73.69";

    private static final int REGISTER_PORT = 7000;
    private static final int BOX_PORT = 0;
    private static final String USER_ME = "Gonçalo";
    private static final String PUBLIC_ME = "61D-G-08-Gonçalo";

    public static void main(String[] args) {
        publicChat();

    }

    private static void privateChat(){
        try {
            Properties props = System.getProperties();
            props.put("java.rmi.server.hostname", "10.10.68.12");
            MsgBox myBox = new MsgBox(USER_ME);
            IMsgBox stubBox = (IMsgBox) UnicastRemoteObject.exportObject(myBox, BOX_PORT);
            Registry reg = LocateRegistry.getRegistry(SV_IP, REGISTER_PORT);
            IMessagingService service = (IMessagingService) reg.lookup("MessagingService");
            System.out.println(service.ping("ISEL"));
            service.register(USER_ME,stubBox);
            System.out.println(service.getRegisteredUsers());
            //service.sendMulticastMessage(USER_ME, "Olá");
            IMsgBox nunoBox = null;
            while(nunoBox == null) {
                nunoBox = service.connetUser("Nuno");
            }
            Scanner scn = new Scanner(System.in);
            System.out.println("You are in a talk with Nuno.\nInsert empty line to end");
            while(true){
                String msg = scn.nextLine();
                if(msg.isEmpty())break;
                nunoBox.messageNotification(USER_ME, msg);
            }

            service.unRegister(USER_ME);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Client unhandled exception: " + e.toString());
        }finally {
            System.exit(0);
        }
    }

    private static void publicChat(){
        try {
            Properties props = System.getProperties();
            props.put("java.rmi.server.hostname", "10.10.68.12");
            MsgBox myBox = new MsgBox(PUBLIC_ME);
            IMsgBox stubBox = (IMsgBox) UnicastRemoteObject.exportObject(myBox, BOX_PORT);
            Registry reg = LocateRegistry.getRegistry(SV_IP, REGISTER_PORT);
            IMessagingService service = (IMessagingService) reg.lookup("MessagingService");
            //service.unRegister(PUBLIC_ME);
            service.register(PUBLIC_ME, stubBox);
            System.out.println("Registered Users :" + service.getRegisteredUsers());
            Scanner scn = new Scanner(System.in);

            while(true){
                String msg = scn.nextLine();
                if(msg.equals("."))break;
                service.sendMulticastMessage(PUBLIC_ME, msg);
            }
            service.unRegister(PUBLIC_ME);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            System.exit(0);
        }
    }
}
