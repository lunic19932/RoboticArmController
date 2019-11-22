package structure;

import interfaces.IStub;

public class StubConnection {
	IStub stub;
	NetworkConnection connection;
	public StubConnection(IStub stub, NetworkConnection connection) {
		this.stub = stub;
		this.connection = connection;
	}
	public IStub getStub() {
		return stub;
	}
	public void setStub(IStub stub) {
		this.stub = stub;
	}
	public NetworkConnection getConnection() {
		return connection;
	}
	public void setConnection(NetworkConnection connection) {
		this.connection = connection;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof StubConnection)) {
			return false;
		}
		StubConnection c = (StubConnection) o;
		if((connection!=null && c.getConnection()!=null) && connection.equals(c.getConnection())) {
			return true;
		}
		if ((this.stub!=null && c.getStub()!=null)&& stub== c.getStub()) {
			return true;
		} 
		
		return false;
	}
	
	
}
