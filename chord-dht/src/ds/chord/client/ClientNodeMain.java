package ds.chord.client;

import java.io.IOException;
import java.util.Properties;

import ds.chord.common.ServerClientInterface;
import ds.chord.common.dto.ClientMetaData;
import ds.chord.server.CentralHubMain;

public class ClientNodeMain {

	private static Properties props;

	private ServerClientInterface serverRef;

	public static void main(String[] args) {
		getServerReference();
		ClientMetaData metaData = connectToServer();
	}

	/**
	 * This method loads the properties of application. It insitialises the
	 * server.ip & server.port.number in a object which is then used to redirect
	 * packet to eventManager.
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