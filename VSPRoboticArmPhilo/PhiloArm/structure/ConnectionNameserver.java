package structure;

public class ConnectionNameserver {
	private Address address;
	private int counter = Constants.COUNTER_START;

	public ConnectionNameserver(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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
}