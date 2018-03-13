package ds.chord.common.dto;

import java.io.Serializable;
import java.util.Map;

public class ClientMetaData implements Serializable {
	private static final long serialVersionUID = 3174920282163972987L;

	private int nodeId;
	private int position;
	private String ObjectReference;
	private String ip;
	private int port;
	private boolean added;
	private String message;
	private Map<Integer, ClientMetaData> fingerTable;

	/**
	 * @return the added
	 */
	public boolean isAdded() {
		return added;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param added
	 *            the added to set
	 */
	public void setAdded(boolean added) {
		this.added = added;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @param ip
	 *            the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * @return the objectReference
	 */
	public String getObjectReference() {
		return ObjectReference;
	}

	/**
	 * @param nodeId
	 *            the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	/**
	 * @param objectReference
	 *            the objectReference to set
	 */
	public void setObjectReference(String objectReference) {
		ObjectReference = objectReference;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + nodeId;
		result = prime * result + port;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientMetaData other = (ClientMetaData) obj;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (nodeId != other.nodeId)
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	public Map<Integer, ClientMetaData> getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(Map<Integer, ClientMetaData> fingerTable) {
		this.fingerTable = fingerTable;
	}

}
