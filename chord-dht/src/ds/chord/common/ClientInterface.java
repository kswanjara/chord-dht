package ds.chord.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

import ds.chord.common.dto.ClientMetaData;
import ds.chord.common.dto.CommunicationDto;
import ds.chord.common.dto.RequestDto;

public interface ClientInterface extends Remote {
	// Methods Server --> Client
	public boolean notifyClient(ClientMetaData clientMetaData) throws RemoteException;

	public void routeReq(RequestDto requestDto) throws RemoteException;

	public void messageFromFinalNode(RequestDto requestDto) throws RemoteException;
}
