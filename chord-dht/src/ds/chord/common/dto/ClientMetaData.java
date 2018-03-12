package ds.chord.common.dto;

import java.io.Serializable;
import java.util.Map;

public class ClientMetaData implements Serializable {
	private static final long serialVersionUID = 3174920282163972987L;

	private int nodeId;
	private String ObjectReference;
	private Map<Integer, CommunicationDetails> fingerTable;

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
	 * @return the fingerTable
	 */
	public Map<Integer, CommunicationDetails> getFingerTable() {
		return fingerTable;
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

	/**
	 * @param fingerTable
	 *            the fingerTable to set
	 */
	public void setFingerTable(Map<Integer, CommunicationDetails> fingerTable) {
		this.fingerTable = fingerTable;
	}

}
