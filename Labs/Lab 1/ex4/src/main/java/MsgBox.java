import contracts.IMsgBox;

import java.rmi.RemoteException;

public class MsgBox implements IMsgBox {
    private final String owner;
    public MsgBox(String owner){
        this.owner = owner;
    }
    @Override
    public void messageNotification(String user, String msg) throws RemoteException {
        System.out.println(
                String.format("(%s) %s", user, msg)
        );
    }
}
