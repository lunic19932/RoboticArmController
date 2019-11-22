package structure;

public class Message {
	private Header header;
	private byte[] message;
	private int counter;
	
	public Message(byte[] message, int sqnNumber) {
		this.header = new Header();
		this.message = message;
		counter = Constants.COUNTER_START;
	}
	
	public Message(Header header, byte[] message, int sqnNumber) {
		this.header = header;
		this.message = message;
		counter = Constants.COUNTER_START;
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	public void decreaseCounter() {
		counter--;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}	
}
