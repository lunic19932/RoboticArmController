package structure;

public class Header {
	private byte messageType = 0;
	private int dataSize = Constants.MAX_DATA_SIZE;
	private int sequenceNumber = -1;
	private int ackNumber = -1;

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}

	public int getDataSize() {
		return dataSize;
	}

	public void setDataSize(int dataSize) {
		this.dataSize = dataSize;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public int getAckNumber() {
		return ackNumber;
	}

	public void setAckNumber(int ackNumber) {
		this.ackNumber = ackNumber;
	}
}
