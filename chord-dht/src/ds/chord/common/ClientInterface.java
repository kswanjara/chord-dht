package ds.chord.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ds.chord.common.dto.ClientMetaData;

public interface ClientInterface extends Remote {
	// Methods Server --> Client
	public boolean notifyClient(ClientMetaData clientMetaData) throws RemoteException;
}
