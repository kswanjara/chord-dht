package ds.chord.server;

import java.io.Serializable;

import ds.chord.common.dto.ClientMetaData;

public class DataManager implements Serializable {
	private static final long serialVersionUID = -6132786905970416880L;

	/**
	 * @param nodeData
	 */
	public DataManager(int power) {
		super();
		this.setPower(power);
		this.nodeData = new ClientMetaData[(int) Math.pow(2, power)];
	}

	private ClientMetaData[] nodeData;
	private int power;

	/**
	 * @return the nodeData
	 */
	public ClientMetaData[] getNodeData() {
		return nodeData;
	}

	/**
	 * @param nodeData
	 *            the nodeData to set
	 */
	public void setNodeData(ClientMetaData[] nodeData) {
		this.nodeData = nodeData;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

}
