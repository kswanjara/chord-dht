package ds.chord.common.dto;

import java.io.Serializable;

public class CommunicationDto implements Serializable {
	private static final long serialVersionUID = 6983068710915837881L;

	private String ObjectReference;
	private String ip;
	private int port;
	private int nodeId;
	private int positionId;

	/**
	 * @param ip
	 * @param port
	 */
	public CommunicationDto(String ip, int port, String ObjectReference) {
		super();
		this.ip = ip;
		this.port = port;
		this.ObjectReference = ObjectReference;
	}

	/**
	 * @return the objectReference
	 */
	public String getObjectReference() {
		return ObjectReference;
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
	 * @param objectReference
	 *            the objectReference to set
	 */
	public void setObjectReference(String objectReference) {
		ObjectReference = objectReference;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ObjectReference == null) ? 0 : ObjectReference.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
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
		CommunicationDto other = (CommunicationDto) obj;
		if (ObjectReference == null) {
			if (other.ObjectReference != null)
				return false;
		} else if (!ObjectReference.equals(other.ObjectReference))
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public int getPositionId() {
		return positionId;
	}

	public void setPositionId(int positionId) {
		this.positionId = positionId;
	}
}
