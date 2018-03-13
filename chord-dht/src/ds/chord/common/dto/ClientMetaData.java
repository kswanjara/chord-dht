package ds.chord.common.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientMetaData implements Serializable {
	private static final long serialVersionUID = 3174920282163972987L;

	private int nodeId;
	private int position;
	private boolean added;
	private String message;
	private CommunicationDto communicationDto;
	private Map<Integer, CommunicationDto> fingerTable;
	private boolean online;
	private Set<Integer> fileNumHolder = new HashSet<Integer>();

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
	 * @param position
	 *            the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the nodeId
	 */
	public int getNodeId() {
		return nodeId;
	}

	/**
	 * @param nodeId
	 *            the nodeId to set
	 */
	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public Map<Integer, CommunicationDto> getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(Map<Integer, CommunicationDto> fingerTable) {
		this.fingerTable = fingerTable;
	}

	public CommunicationDto getCommunicationDto() {
		return communicationDto;
	}

	public void setCommunicationDto(CommunicationDto communicationDto) {
		this.communicationDto = communicationDto;
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
		result = prime * result + ((communicationDto == null) ? 0 : communicationDto.hashCode());
		result = prime * result + nodeId;
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
		if (communicationDto == null) {
			if (other.communicationDto != null)
				return false;
		} else if (!communicationDto.equals(other.communicationDto))
			return false;
		if (nodeId != other.nodeId)
			return false;
		return true;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public Set<Integer> getFileNumHolder() {
		return fileNumHolder;
	}

	public void setFileNumHolder(Set<Integer> fileNumHolder) {
		this.fileNumHolder = fileNumHolder;
	}

}
