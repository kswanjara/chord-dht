package ds.chord.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerClientInterface extends Remote {
	public String connectToServer(String message) throws RemoteException;
}
