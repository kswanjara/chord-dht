package ds.chord.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.Scanner;

import ds.chord.common.ServerInterface;
import ds.chord.common.dto.ClientMetaData;
import ds.chord.server.CentralHubMain;

public class ClientNodeMain {

	private static Properties props;

	private ServerInterface serverRef;

	private ClientMetaData metaData;

	public static void main(String[] args) {
		try {
			loadProperties();

			Scanner sc = new Scanner(System.in);

			ClientNodeMain node = new ClientNodeMain();
			node.getServerReference();

			System.out.println("Enter the NodeID : ");
			int nodeId = sc.nextInt();

			node.metaData = new ClientMetaData();

			node.metaData.setNodeId(nodeId);
			node.metaData.setIp(InetAddress.getLocalHost().getHostAddress().trim());
			node.metaData.setPort(Integer.parseInt(props.getProperty("client.port.number")));
			node.metaData = node.connectToServer(node.metaData);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ClientMetaData connectToServer(ClientMetaData metaData) {
		try {
			System.out.println("Notifying server...");
			// ClientMetaData metaData = this.serverRef.connectToServer("I am new here!");
			this.metaData = this.serverRef.connectToServer(metaData);
			System.out.println("Got NodeId for myself from server: " + metaData);
			// return metaData;
		} catch (RemoteException e) {
			System.out.println("Exception: Occured while notifying server first time coming online!");
			e.printStackTrace();
		}
		return metaData;
	}

	private void getServerReference() {
		try {

			String serverIp = props.getProperty("server.ip");
			int portNumber = Integer.parseInt(props.getProperty("server.port.number"));
			String serverReferenceName = props.getProperty("server.rmi.reference");

			Registry registry = LocateRegistry.getRegistry(serverIp, portNumber);

			// Looking up the registry for the remote object
			this.serverRef = (ServerInterface) registry.lookup(serverReferenceName);

			// this.serverRef = (ServerInterface) Naming
			// .lookup("//" + serverIp + ":" + portNumber + "/" + serverReferenceName);
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
}