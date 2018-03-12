package ds.chord.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ds.chord.common.dto.ClientMetaData;

public interface ServerClientInterface extends Remote {
	// Methods Client --> Server
	public String connectToServer(String message) throws RemoteException;

	// Methods Server --> Client
	public boolean notifyClient(ClientMetaData clientMetaData) throws RemoteException;
}