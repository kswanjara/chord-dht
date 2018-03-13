package ds.chord.common.dto;

import java.io.Serializable;
import java.util.List;

public class RequestDto implements Serializable {
	private static final long serialVersionUID = -1310325218801056000L;

	private int fileNum;
	private int idealNode;
	private int requestor;
	private CommunicationDto requestSource;
	private int hopsTaken;
	private String communicationType;
	private List<CommunicationDto> pathTaken;
	private int hopsReq;
	private boolean valPresent = false;

	/**
	 * @return the fileNum
	 */
	public int getFileNum() {
		return fileNum;
	}

	/**
	 * @return the idealNode
	 */
	public int getIdealNode() {
		return idealNode;
	}

	/**
	 * @return the requester
	 */
	public int getRequestor() {
		return requestor;
	}

	/**
	 * @return the requestSource
	 */
	public CommunicationDto getRequestSource() {
		return requestSource;
	}

	/**
	 * @return the hopsTaken
	 */
	public int getHopsTaken() {
		return hopsTaken;
	}

	/**
	 * @return the communicationType
	 */
	public String getCommunicationType() {
		return communicationType;
	}

	/**
	 * @return the pathTaken
	 */
	public List<CommunicationDto> getPathTaken() {
		return pathTaken;
	}

	/**
	 * @param fileNum
	 *            the fileNum to set
	 */
	public void setFileNum(int fileNum) {
		this.fileNum = fileNum;
	}

	/**
	 * @param idealNode
	 *            the idealNode to set
	 */
	public void setIdealNode(int idealNode) {
		this.idealNode = idealNode;
	}

	/**
	 * @param requestor
	 *            the requester to set
	 */
	public void setRequestor(int requestor) {
		this.requestor = requestor;
	}

	/**
	 * @param requestSource
	 *            the requestSource to set
	 */
	public void setRequestSource(CommunicationDto requestSource) {
		this.requestSource = requestSource;
	}

	/**
	 * @param hopsTaken
	 *            the hopsTaken to set
	 */
	public void setHopsTaken(int hopsTaken) {
		this.hopsTaken = hopsTaken;
	}

	/**
	 * @param communicationType
	 *            the communicationType to set
	 */
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}

	/**
	 * @param pathTaken
	 *            the pathTaken to set
	 */
	public void setPathTaken(List<CommunicationDto> pathTaken) {
		this.pathTaken = pathTaken;
	}

	public int getHopsReq() {
		return hopsReq;
	}

	public void setHopsReq(int hopsReq) {
		this.hopsReq = hopsReq;
	}

	public boolean isValPresent() {
		return valPresent;
	}

	public void setValPresent(boolean valPresent) {
		this.valPresent = valPresent;
	}

}
