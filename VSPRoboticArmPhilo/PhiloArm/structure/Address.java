package structure;


public class Address {
	private int id;
	private String ipAddress;
	private int port;

	public Address(int id, String address, int port) {
		this.id = id;
		this.ipAddress = address;
		this.port = port;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String address) {
		this.ipAddress = address;
	}

	public int getPort() {
		return port;
	}
	@Override
	public boolean equals(Object o) {
		if(o==this) {
			return true;
		}
		if(!(o instanceof Address)) {
			return false;
		}
		Address a=(Address)o;
		if(!a.getIpAddress().equals(ipAddress) || !(a.getId()==id) || a.getPort()!=port) {
			return false;
		}
		return true;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
