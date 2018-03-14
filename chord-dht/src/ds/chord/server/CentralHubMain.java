package ds.chord.server;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import ds.chord.common.ClientInterface;
import ds.chord.common.ServerInterface;
import ds.chord.common.dto.ClientMetaData;
import ds.chord.common.dto.CommunicationDto;

public class CentralHubMain extends UnicastRemoteObject implements ServerInterface {
	private static final long serialVersionUID = 4011425218883307369L;

	private static DataManager dataManager;

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
			System.out.println("How many nodes should be in the network? (2^?) ");

			@SuppressWarnings("resource")
			Scanner sc = new Scanner(System.in);

			int n = sc.nextInt();

			dataManager = new DataManager(n);

			ServerInterface serverObj = new CentralHubMain();
			int portNumber = Integer.parseInt(props.getProperty("server.port.number"));
			String serverReferenceName = props.getProperty("server.rmi.reference");

			System.out.println("Server started. Listening on Port");

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.createRegistry(portNumber);
			registry.rebind(serverReferenceName, serverObj);

			System.err.println("Server ready");
		} catch (RemoteException e) {
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
			e.printStackTrace();
		}
	}

	@Override
	public ClientMetaData connectToServer(ClientMetaData metaData) throws RemoteException {
		int position = metaData.getNodeId() % dataManager.getNodeData().length;
		int newPosition = position;
		boolean alreadyUsed = checkIfAlreadyUsed(position);
		if (alreadyUsed) {
			newPosition = handleCollision(position);
			if (position == newPosition) {
				System.out.println("Can't accomodate more nodes in the network!");
				metaData.setAdded(false);
				metaData.setMessage("Can't accomodate more nodes in the network!");
				return metaData;
			}
		}
		metaData.setPosition(position);
		metaData.setAdded(true);

		CommunicationDto dto = metaData.getCommunicationDto();

		dto.setNodeId(metaData.getNodeId());
		dto.setPositionId(metaData.getPosition());
		// dto.setObjectReference("ClientReference" + metaData.getPosition());

		metaData.setCommunicationDto(dto);

		metaData = getFingerTable(metaData);

		dataManager.getNodeData()[metaData.getPosition()] = metaData;

		updateOtherNodes(metaData);

		return metaData;
	}

	private ClientMetaData getFingerTable(ClientMetaData metaData) {
		metaData.setFingerTable(new TreeMap<>());
		if (dataManager.isEmpty()) {
			for (int i = 0; i < dataManager.getPower(); i++) {
				int hop = (int) Math.pow(2, i);
				metaData.getFingerTable().put(hop, metaData.getCommunicationDto());
			}
			dataManager.setEmpty(false);
		} else {
			for (int i = 0; i < dataManager.getPower(); i++) {
				int hop = (int) Math.pow(2, i);
				int succesor = (metaData.getPosition() + hop) % dataManager.getNodeData().length;
				ClientMetaData nextObj = dataManager.getNodeData()[succesor];
				if (nextObj == null) {
					nextObj = getSuccessorObj(succesor);
				}
				metaData.getFingerTable().put(hop, nextObj.getCommunicationDto());
			}
		}
		return metaData;
	}

	private void updateOtherNodes(ClientMetaData metaData) {
		for (ClientMetaData node : dataManager.getNodeData()) {
			if (node != null && node.getPosition() != metaData.getPosition()) {
				node = getFingerTable(node);
				notifyClient(node);
			}
		}
	}

	private void notifyClient(ClientMetaData node) {
		// notify the client about updated finger table
		try {
			Registry registry = LocateRegistry.getRegistry(node.getCommunicationDto().getIp(),
					node.getCommunicationDto().getPort());
			ClientInterface clientRef = (ClientInterface) registry
					.lookup(node.getCommunicationDto().getObjectReference());
			clientRef.notifyClient(node);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

	}

	private ClientMetaData getSuccessorObj(int succesor) {
		for (int i = (succesor + 1) % dataManager.getNodeData().length; i != succesor; i = ((i + 1)
				% dataManager.getNodeData().length)) {
			if (dataManager.getNodeData()[i] != null)
				return dataManager.getNodeData()[i];
		}
		return null;
	}

	private int handleCollision(int position) {
		int current = position;
		int netCap = dataManager.getNodeData().length;
		position = (position + 1) % netCap;
		while (position != current && dataManager.getNodeData()[position] == null) {
			position = (position + 1) % netCap;
		}
		return position;
	}

	private boolean checkIfAlreadyUsed(int position) {
		if (dataManager.getNodeData()[position] != null) {
			return true;
		}
		return false;
	}

	@Override
	public void goingOffline(ClientMetaData metaData) throws RemoteException {
		// TODO Auto-generated method stub
		dataManager.getNodeData()[metaData.getPosition()] = null;
		updateOtherNodes(metaData);
	}

	@Override
	public Set<Integer> askForFiles(ClientMetaData metaData) throws RemoteException {
		try {
			int i = metaData.getPosition() - 1;
			if (i < 0) {
				i = dataManager.getNodeData().length - 1;
			}
			for (i = metaData.getPosition() - 1; i != metaData.getPosition(); i--) {
				if (dataManager.getNodeData()[i] != null) {
					break;
				}
			}

			List<Integer> list = new ArrayList<>();

			for (int j = i + 1; j <= metaData.getPosition(); j = (j + 1) % dataManager.getNodeData().length) {
				list.add(j);
			}

			Registry registry = LocateRegistry.getRegistry(metaData.getFingerTable().get(1).getIp(),
					metaData.getFingerTable().get(1).getPort());
			ClientInterface clientRef = (ClientInterface) registry
					.lookup(metaData.getFingerTable().get(1).getObjectReference());

			return clientRef.giveFilesBack(list);

		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}