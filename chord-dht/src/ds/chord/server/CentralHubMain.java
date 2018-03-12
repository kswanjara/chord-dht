package ds.chord.server;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import ds.chord.common.ServerInterface;

public class CentralHubMain extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 4011425218883307369L;

	protected CentralHubMain() throws RemoteException {
		// super();
	}

	private static Properties props;

	public static void main(String[] args) {
		loadProperties();
		registerServerObject();
	}

	private static void registerServerObject() {
		try {
			ServerInterface serverObj = new CentralHubMain();
			int portNumber = Integer.parseInt(props.getProperty("server.port.number"));
			String serverReferenceName = props.getProperty("server.rmi.reference");

			ServerInterface stub;
			// stub = (ServerInterface) UnicastRemoteObject.exportObject(serverObj,
			// portNumber);

			System.out.println("Server started. Listening on Port");

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.createRegistry(portNumber);
			registry.rebind(serverReferenceName, serverObj);

			System.err.println("Server ready");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
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

	@Override
	public String connectToServer(String message) throws RemoteException {
		// TODO Auto-generated method stub
		return "Node" + (int) Math.random() * 100;
	}
}
