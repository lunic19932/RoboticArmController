package structure;

import java.util.ArrayList;

public class NetworkConnection {
	private Address address;
	private int sendSQN;
	private int expectedSQN;
	private ArrayList<Message> messages;
	private int heartBeatCounter;
	private boolean initialized;
	private boolean isConnected;
	

	public NetworkConnection(Address address) {
		setAddress(address);
		setSendSQN(1);
		setExpectedSQN(1);
		messages = new ArrayList<Message>();
		setHeartBeatCounter(0);
		setInitialized(false);
	}

//	@Override
//	public String toString() {
//		return address.getName() + " " + address.getIpAddress() + " " + address.getPort();
//	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof NetworkConnection)) {
			return false;
		}
		NetworkConnection c = (NetworkConnection) o;
		if(c.getAddress()==null || this.address==null) {
			return false;
		}
		if (address.getIpAddress().equals(c.getAddress().getIpAddress())
				&& address.getPort() == c.getAddress().getPort()) {
			return true;
		} else {
			return false;
		}
	}

	public synchronized Address getAddress() {
		return address;
	}

	public synchronized  void setAddress(Address address) {
		this.address = address;
	}

	public  synchronized int getSendSQN() {
		return sendSQN;
	}

	public synchronized  void setSendSQN(int sendSQN) {
		this.sendSQN = sendSQN;
	}

	public synchronized  void increaseSendSQN() {
		sendSQN++;
	}

	public  synchronized int getExpectedSQN() {
		return expectedSQN;
	}

	public  synchronized void setExpectedSQN(int expectedSQN) {
		this.expectedSQN = expectedSQN;
	}

	public  synchronized void increaseExpectedSQN() {
		expectedSQN++;
	}

	public synchronized  void deleteAllMessages() {
		messages=new ArrayList<Message>();
	}
	public synchronized  void addMessage(Message msg) {
		messages.add(msg);
	}

	public synchronized  void deleteMessage(Message msg) {
		if (!messages.remove(msg)) {
			System.out.println("Acknowledgement not in List!");
		}
	}

	public  synchronized void deleteMessageIndex(int index) {
		messages.remove(index);
	}

	public  synchronized void deleteMessageSQN(int sqn) {
		for (int i = 0; i < messages.size(); i++) {
			if (messages.get(i).getHeader().getSequenceNumber() <= sqn) {
				messages.remove(i);
			}
		}
	}

	public  synchronized ArrayList<Message> getMessages() {
		return messages;
	}

	public synchronized  int getMessagesSize() {
		return messages.size();
	}

	public  synchronized int getHeartBeatCounter() {
		return heartBeatCounter;
	}

	public  synchronized void setHeartBeatCounter(int heartBeatCounter) {
		this.heartBeatCounter = heartBeatCounter;
	}
	
	public  synchronized void increaseHeartbeatCounter() {
		heartBeatCounter++;
	}

	public  synchronized boolean isInitialized() {
		return initialized;
	}

	public  synchronized void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public  synchronized boolean isConnected() {
		return isConnected;
	}

	public synchronized  void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	
}
