package ds.chord.client;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;

import ds.chord.common.ServerInterface;
import ds.chord.common.dto.ClientMetaData;
import ds.chord.server.CentralHubMain;

public class ClientNodeMain {

	private static Properties props;

	private ServerInterface serverRef;

	public static void main(String[] args) {
		loadProperties();
		ClientNodeMain node = new ClientNodeMain();
		node.getServerReference();
		ClientMetaData metaData = node.connectToServer();
	}

	private ClientMetaData connectToServer() {
		try {
			System.out.println("Notifying server...");
			// ClientMetaData metaData = this.serverRef.connectToServer("I am new here!");
			String metaData = this.serverRef.connectToServer("I am new here!");
			System.out.println("Got NodeId for myself from server: " + metaData);
			// return metaData;
		} catch (RemoteException e) {
			System.out.println("Exception: Occured while notifying server first time coming online!");
			e.printStackTrace();
		}
		return null;
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
			/*
			 * String appConfigPath = System.getProperty("java.class.path") +
			 * System.getProperty("file.separator") + "application.properties";
			 */

			props = new Properties();
			props.load(CentralHubMain.class.getClassLoader().getResourceAsStream("application.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}