package ds.chord.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ds.chord.common.dto.ClientMetaData;

public interface ServerInterface extends Remote {
	// Methods Client --> Server
	public String connectToServer(String message) throws RemoteException;
}