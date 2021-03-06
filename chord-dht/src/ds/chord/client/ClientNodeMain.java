package ds.chord.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import ds.chord.common.ClientInterface;
import ds.chord.common.ServerInterface;
import ds.chord.common.dto.ClientMetaData;
import ds.chord.common.dto.CommunicationDto;
import ds.chord.common.dto.RequestDto;
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
			node.getFilesBack();

			node.waitForInput();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void waitForInput() {
		try {
			Scanner sc = new Scanner(System.in);
			if (this.metaData.isOnline()) {
				System.out.println("Press 1 for look up: ");
				System.out.println("Press 2 for uploading file: ");
				System.out.println("Press 3 to see all files here: ");
				System.out.println("Press 4 to go offline: ");
				int choice = sc.nextInt();
				if (choice == 1) {
					System.out.println("Enter the file number to look up: ");
					int fileNum = sc.nextInt();
					this.lookUpFile(fileNum);
				} else if (choice == 2) {
					System.out.println("Enter the file number to upload: ");
					int fileNum = sc.nextInt();
					this.uploadFile(fileNum);
				} else if (choice == 3) {
					System.out.println("Files available: ");
					for (Integer integer : this.metaData.getFileNumHolder()) {
						System.out.print(integer + "  ");
					}
					System.out.println();
				} else if (choice == 4) {
					sendAllFiles();
					notifyServer();
					this.metaData.setOnline(false);
				} else {
					System.out.println("Incorrect input! Try again.");
				}
			} else {
				System.out.println("Press 1 to go online: ");
				int choice = sc.nextInt();
				if (choice == 1) {
					this.metaData.setOnline(true);
					this.connectToServer(this.metaData);

					this.getFilesBack();
					System.out.println("You are back online! ");
				} else {
					System.out.println("Incorrect input! Try again.");
				}
			}
			waitForInput();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getFilesBack() {
		try {
			Set<Integer> list = this.serverRef.askForFiles(this.metaData);

			if (list != null) {
				this.metaData.getFileNumHolder().addAll(list);
				System.out.println("Got back the files: ");
				for (Integer integer : list) {
					System.out.print(integer + "  ");
				}
				this.metaData.getFileNumHolder().addAll(list);
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void notifyServer() {
		try {
			this.serverRef.goingOffline(this.metaData);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendAllFiles() {
		CommunicationDto closestSuccessor = this.metaData.getFingerTable().get(1);
		try {
			Registry registry = LocateRegistry.getRegistry(closestSuccessor.getIp(), closestSuccessor.getPort());
			ClientInterface clientRef = (ClientInterface) registry.lookup(closestSuccessor.getObjectReference());
			clientRef.holdData(this.metaData.getFileNumHolder());
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private void uploadFile(int fileNum) throws RemoteException {
		int totalNodes = (int) Math.pow(2, this.metaData.getFingerTable().size());
		int idealNode = fileNum % totalNodes;

		int current = this.metaData.getPosition();

		int hopsReq = 0;

		if (idealNode > current) {
			hopsReq = idealNode - current;
		} else if (idealNode < current) {
			hopsReq = (totalNodes - current) + idealNode;
		}

		RequestDto request = new RequestDto();
		request.setFileNum(fileNum);
		request.setHopsTaken(0);
		request.setPathTaken(new ArrayList<CommunicationDto>());
		request.setIdealNode(idealNode);
		request.setRequestSource(this.metaData.getCommunicationDto());
		request.setRequestor(current);
		request.setCommunicationType("UPLOAD");
		request.setHopsReq(hopsReq);

		routeReq(request);

		// return destination;
	}

	private void lookUpFile(int fileNum) throws RemoteException {
		int totalNodes = (int) Math.pow(2, this.metaData.getFingerTable().size());
		int idealNode = fileNum % totalNodes;

		int current = this.metaData.getPosition();

		int hopsReq = 0;

		if (idealNode > current) {
			hopsReq = idealNode - current;
		} else if (idealNode < current) {
			hopsReq = (totalNodes - current) + idealNode;
		}

		RequestDto request = new RequestDto();
		request.setFileNum(fileNum);
		request.setHopsTaken(0);
		request.setPathTaken(new ArrayList<CommunicationDto>());
		request.setIdealNode(idealNode);
		request.setRequestSource(this.metaData.getCommunicationDto());
		request.setRequestor(current);
		request.setCommunicationType("LOOKUP");
		request.setHopsReq(hopsReq);

		routeReq(request);

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
			this.metaData = this.serverRef.connectToServer(metaData);
			System.out.println(
					"Got NodeId for myself from server: " + metaData.getCommunicationDto().getObjectReference());
		} catch (RemoteException e) {
			System.out.println("Exception: Occured while notifying server first time coming online!");
			e.printStackTrace();
		}
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
		this.metaData.setFingerTable(clientMetaData.getFingerTable());
	}

	@Override
	public void routeReq(RequestDto requestDto) throws RemoteException {
		if (requestDto.getHopsReq() > 0) {
			// take necessary hops and routeReq method
			NavigableSet<Integer> keys = (NavigableSet<Integer>) this.metaData.getFingerTable().keySet();
			CommunicationDto nextHop = this.metaData.getFingerTable().get(keys.floor(requestDto.getHopsReq()));
			int hopsTaken = 0;
			if (nextHop.getPositionId() > requestDto.getRequestor()) {
				hopsTaken = nextHop.getPositionId() - requestDto.getRequestor();
			} else {
				hopsTaken = ((int) (Math.pow(2, this.metaData.getFingerTable().size())) - requestDto.getRequestor())
						+ nextHop.getPositionId();
			}

			requestDto.setHopsTaken(requestDto.getHopsTaken() + hopsTaken);
			requestDto.setHopsReq(requestDto.getHopsReq() - hopsTaken);

			requestDto.getPathTaken().add(this.metaData.getCommunicationDto());

			forwardRequest(nextHop, requestDto);

		} else {
			requestDto.getPathTaken().add(this.metaData.getCommunicationDto());
			if (requestDto.getCommunicationType().equalsIgnoreCase("UPLOAD")) {
				this.metaData.getFileNumHolder().add(requestDto.getFileNum());
			} else {
				if (this.metaData.getFileNumHolder().contains(requestDto.getFileNum())) {
					requestDto.setValPresent(true);
				}
			}
			sendConfirmationToSource(requestDto);
		}
	}

	private void forwardRequest(CommunicationDto nextHop, RequestDto requestDto) {
		try {
			Registry registry = LocateRegistry.getRegistry(nextHop.getIp(), nextHop.getPort());
			ClientInterface clientRef = (ClientInterface) registry.lookup(nextHop.getObjectReference());
			clientRef.routeReq(requestDto);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	private void sendConfirmationToSource(RequestDto requestDto) {
		// TODO Auto-generated method stub
		if (this.metaData.getPosition() == requestDto.getRequestSource().getPositionId()) {
			if (requestDto.getCommunicationType().equalsIgnoreCase("upload")) {
				System.out.println("Uploaded the file on the same node.");
			} else {
				if (this.metaData.getFileNumHolder().contains(requestDto.getFileNum()))
					System.out.println("File found on the same node.");
				else
					System.out.println("File not available anywhere on the network!");
			}
		} else {
			// send the confirmation from final(this) to node to source node
			try {
				Registry registry = LocateRegistry.getRegistry(requestDto.getRequestSource().getIp(),
						requestDto.getRequestSource().getPort());
				ClientInterface clientRef = (ClientInterface) registry
						.lookup(requestDto.getRequestSource().getObjectReference());
				clientRef.messageFromFinalNode(requestDto);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void messageFromFinalNode(RequestDto requestDto) throws RemoteException {
		if (requestDto.getCommunicationType().equalsIgnoreCase("UPLOAD")) {
			System.out.print("Path taken while uploading: ");
			for (CommunicationDto path : requestDto.getPathTaken()) {
				System.out.print(path.getNodeId() + "( position: " + path.getPositionId() + " )  ");
			}
		} else {
			System.out.print("Path taken while lookup: ");
			for (CommunicationDto path : requestDto.getPathTaken()) {
				System.out.print(path.getNodeId() + "( position: " + path.getPositionId() + " )  ");
			}
			if (requestDto.isValPresent()) {
				System.out.println();
				System.out.println("File found at : "
						+ requestDto.getPathTaken().get(requestDto.getPathTaken().size() - 1).getNodeId());
			} else {
				System.out.println("File not available anywhere on the network!");
			}
		}
	}

	@Override
	public void holdData(Set<Integer> fileNumHolder) throws RemoteException {
		this.metaData.getFileNumHolder().addAll(fileNumHolder);
	}

	@SuppressWarnings("static-access")
	@Override
	public Set<Integer> giveFilesBack(List<Integer> list) throws RemoteException {
		Set<Integer> availData = this.metaData.getFileNumHolder();
		Set<Integer> returnData = new HashSet<>();
		int size = (int) Math.pow(2, this.metaData.getFingerTable().size());

		for (Integer integer : availData) {
			int modValue = integer % size;
			if (list.contains(modValue)) {
				returnData.add(integer);
			}
		}

		for (Integer integer : returnData) {
			this.metaData.getFileNumHolder().remove(integer);
		}

		return returnData;
	}
}