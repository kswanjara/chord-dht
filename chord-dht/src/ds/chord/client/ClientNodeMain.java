package ds.chord.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.Scanner;

import ds.chord.common.ClientInterface;
import ds.chord.common.ServerInterface;
import ds.chord.common.dto.ClientMetaData;
import ds.chord.common.dto.CommunicationDto;
import ds.chord.server.CentralHubMain;

public class ClientNodeMain extends UnicastRemoteObject implements ClientInterface {
	private static final long serialVersionUID = 6732793934834941463L;

	protected ClientNodeMain() throws RemoteException {
		super();
	}

	private static Properties props;

	private ServerInterface serverRef;

	private static ClientMetaData metaData;

	public static void main(String[] args) throws RemoteException {
		try {
			loadProperties();

			ClientNodeMain node = new ClientNodeMain();

			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);

			node.getServerReference();
			System.out.println("Enter the NodeID : ");
			int nodeId = sc.nextInt();

			String clientRef = node.registerClientObject(nodeId);

			node.metaData = new ClientMetaData();
			node.metaData.setNodeId(nodeId);
			CommunicationDto dto = new CommunicationDto(InetAddress.getLocalHost().getHostAddress().trim(),
					Integer.parseInt(props.getProperty("client.port.number")), clientRef);
			node.metaData.setCommunicationDto(dto);
			node.metaData.setOnline(true);
			node.connectToServer(node.metaData);

			node.printFingerTable();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String registerClientObject(int nodeId) {
		String serverReferenceName = "ClientReference" + nodeId;
		int portNumber = Integer.parseInt(props.getProperty("client.port.number"));

		try {
			ClientInterface clientObj = new ClientNodeMain();

			// Bind the remote object's stub in the registry
			System.out.println("Client trying to listen on port : " + portNumber);

			Registry registry = LocateRegistry.createRegistry(portNumber);
			registry.rebind(serverReferenceName, clientObj);

			System.err.println("Client ready to receive notifications...");

		} catch (RemoteException e) {
			if (e.getCause().getClass().getName().contains("BindException")) {
				System.err.println("Client could not listen on port : " + portNumber);
				props.setProperty("client.port.number", (portNumber + 1) + "");
				registerClientObject(nodeId);
			}
		} catch (Exception e) {
		}

		return serverReferenceName;
	}

	private void printFingerTable() {
		for (Integer key : metaData.getFingerTable().keySet()) {
			System.out
					.println("Hop : " + key + " Successor: " + metaData.getFingerTable().get(key).getObjectReference());
		}
	}

	private void connectToServer(ClientMetaData metaData) {
		try {
			System.out.println("Notifying server...");
			// ClientMetaData metaData = this.serverRef.connectToServer("I am new here!");
			this.metaData = this.serverRef.connectToServer(metaData);
			System.out.println(
					"Got NodeId for myself from server: " + metaData.getCommunicationDto().getObjectReference());
			// return this.metaData;
		} catch (RemoteException e) {
			System.out.println("Exception: Occured while notifying server first time coming online!");
			e.printStackTrace();
		}
		// return metaData;
	}

	private void getServerReference() {
		try {

			String serverIp = props.getProperty("server.ip");
			int portNumber = Integer.parseInt(props.getProperty("server.port.number"));
			String serverReferenceName = props.getProperty("server.rmi.reference");

			Registry registry = LocateRegistry.getRegistry(serverIp, portNumber);

			this.serverRef = (ServerInterface) registry.lookup(serverReferenceName);

			System.out.println("Found the server remote object!");
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Exception: Occured while searching for server remote object!");
			e.printStackTrace();
		}
	}

	/**
	 * This method loads the properties of application. It initializes the server.ip
	 * & server.port.number in a object which is then used to redirect packet to
	 * eventManager.
	 */
	private static void loadProperties() {
		try {
			props = new Properties();
			props.load(CentralHubMain.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean notifyClient(ClientMetaData clientMetaData) throws RemoteException {
		updateFingerTable(clientMetaData);
		System.out.println("Finger table updated: ");
		printFingerTable();
		return true;
	}

	public void updateFingerTable(ClientMetaData clientMetaData) {
		// System.out.println(this.props.getProperty("client.ip"));
		this.metaData.setFingerTable(clientMetaData.getFingerTable());
	}

}