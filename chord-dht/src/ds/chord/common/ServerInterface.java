package ds.chord.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

import ds.chord.common.dto.ClientMetaData;

public interface ServerInterface extends Remote {
	// Methods Client --> Server
	public ClientMetaData connectToServer(ClientMetaData metaData) throws RemoteException;

	public void goingOffline(ClientMetaData metaData) throws RemoteException;

	public Set<Integer> askForFiles(ClientMetaData metaData) throws RemoteException;
}