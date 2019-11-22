package middlewareNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.primitives.Bytes;

import interfaces.IMiddleware;
import interfaces.IStub;
import staticMethods.StaticMethods;
import structure.Address;
import structure.Constants;
import structure.Header;
import structure.Message;
import structure.NetworkConnection;
import structure.StubConnection;
import stubs.PhiloStub;
import stubs.RacStub;
import stubs.ViewStub;

public abstract class MWNetwork implements IMiddleware {

	protected boolean isRunning = false;
	protected DatagramSocket socket;
	protected Thread tAging, tHeartbeat, tReceive, tSend, tHandlePacket;
	protected boolean sendAnswer, needsSequenceNumber;
	protected ArrayList<NetworkConnection> connections = new ArrayList<NetworkConnection>();
	protected ArrayList<StubConnection> stubConnection = new ArrayList<StubConnection>();
	protected MWNameserverClient mwNameserverClient;
	protected int nameserverStatus = -1;
	protected int id;
	private ExecutorService pool = Executors.newFixedThreadPool(5);
	protected LinkedBlockingQueue<DatagramPacket> receiveMessageQueue = new LinkedBlockingQueue<DatagramPacket>(
			Constants.QUEUE_CAPACITY);
	protected LinkedBlockingQueue<DatagramPacket> sendMessageQueue = new LinkedBlockingQueue<DatagramPacket>(
			Constants.QUEUE_CAPACITY);

	public MWNetwork(int id) throws SocketException {

		this.id = id;
		isRunning = true;
		socket = new DatagramSocket();
		mwNameserverClient = new MWNameserverClient(this, id, socket.getLocalPort());
		sendHeartbeat();
		startAging();
		getMessage();
		sendMessage();
		handlePacket();
		System.out.println("Port: " + socket.getLocalPort());
	}

	@Override
	protected void finalize() throws Throwable {
		isRunning = false;
		socket.close();
		tReceive.interrupt();
		tSend.interrupt();
		tAging.interrupt();
		tHandlePacket.interrupt();
		pool.shutdown();
	}

	protected abstract void functionCallHandler(JSONObject json, JSONObject jParameterliste,
			NetworkConnection connection);

	protected void handlePacket() {
		tHandlePacket = new Thread(new Runnable() {
			NetworkConnection connection;
			DatagramPacket packet;
			Header receivedHeader;
			byte[] receivedData;

			@Override
			public void run() {
				while (isRunning) {
					try {
						packet = receiveMessageQueue.take();
						if (packet != null) {
							connection = new NetworkConnection(
									new Address(-1, packet.getAddress().getHostAddress(), packet.getPort()));
							if (connections.contains(connection)) {
								connection = getConnection(connection);
								connection.setInitialized(true);
							}
							receivedHeader = new Header();
							receivedData = readData(packet.getData(), receivedHeader);
//							 if (receivedHeader.getMessageType() != 0 ) {
//							 System.out.println("Received: "+receivedHeader.getMessageType());
//							 }
					
							
							
							handleData(receivedData, receivedHeader, connection);

						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		tHandlePacket.start();
	}

	protected void handleData(byte[] receivedData, Header receivedHeader, NetworkConnection connection) {
		if (validatePacket(receivedHeader, connection)) {
			JSONParser jParser = new JSONParser();
			String string = new String(receivedData);
			JSONObject json;
			acknowledge(receivedHeader.getAckNumber(), connection);
			switch (receivedHeader.getMessageType()) {
			case Constants.HEARTBEAT:
				heartbeatHandler(connection);
				break;
			case Constants.STARTUP_CONVERSATION:
				connection.getAddress().setId(StaticMethods.parseByteToInt(receivedData));
				System.out.println("New connection: " + connection.getAddress().getId());
				startupConversationHandler(connection);
				break;
			case Constants.FUNCTION_CALL:
				try {
					json = (JSONObject) jParser.parse(string.trim());
					JSONObject jParameterliste = (JSONObject) jParser.parse((String) json.get(Constants.PARAMETERLIST));
					pool.execute(new Runnable() {
						@Override
						public void run() {
							functionCallHandler(json, jParameterliste, connection);
						}
					});
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case Constants.RETURN_VALUE:

				try {
					
						json = (JSONObject) jParser.parse(string.trim());
						StubConnection stubCon = new StubConnection(null, connection);
						int i = stubConnection.indexOf(stubCon);
						stubConnection.get(i).getStub().notifyReturn(json);
					
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			}
		}
	}

	protected void handleSequences(Header sendHeader, byte[] sendMessage, NetworkConnection connection) {
		if (connection != null) {
			sendHeader.setSequenceNumber(connection.getSendSQN());
			connection.increaseSendSQN();
			addMessageToAcknowledgements(sendHeader, sendMessage, connection);
		}

	}

	protected boolean validatePacket(Header receivedHeader, NetworkConnection connection) {
		int messageType = receivedHeader.getMessageType();
		boolean validPacket = true;
		if (messageType == Constants.FUNCTION_CALL || messageType == Constants.RETURN_VALUE) {
			validPacket &= checkSequenceNumber(receivedHeader.getSequenceNumber(), connection);
			// System.out.println("Size: " + connections.size());
		}
		return validPacket;
	}

	protected void getMessage() {
		tReceive = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					byte[] receiveBuffer = new byte[Constants.MAX_PACKET_SIZE];
					DatagramPacket packet = new DatagramPacket(receiveBuffer, receiveBuffer.length);
					try {
						socket.receive(packet);
						if (!receiveMessageQueue.offer(packet)) {
							System.out.println("receive message queue full, dumped message");
						}
						// TODO notify?
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		tReceive.start();
	}

	protected void sendMessage() {
		tSend = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					try {
						byte[] sendBuffer = new byte[Constants.MAX_PACKET_SIZE];
						DatagramPacket packet = new DatagramPacket(sendBuffer, sendBuffer.length);
						packet = sendMessageQueue.take();
						if (packet != null) {
							socket.send(packet);
							Header sendHeader = new Header();
							readData(packet.getData(), sendHeader);
//							if (sendHeader.getMessageType() != 0) {
//								System.out.println("message sent to: " + packet.getAddress().getHostAddress() + " "
//										+ packet.getPort() + " SeqNr.: " + sendHeader.getSequenceNumber());
//							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		tSend.start();
	}

	// TODO ï¿½berlauf? SQN & ACK!!!!
	protected void acknowledge(int ackNumber, NetworkConnection connection) {
		int sqn;
		if (ackNumber != -1) {
			ArrayList<Message> acks = connection.getMessages();
			for (int i = 0; i < acks.size(); i++) {
				sqn = acks.get(i).getHeader().getSequenceNumber();
				if (sqn < ackNumber) {
					getConnection(connection).deleteMessageIndex(i);
					// System.out.println("Message acknowledged");
				}
			}
		} else {
			// System.out.println("Acknowledgementnumber is -1");
		}
	}

	protected boolean checkSequenceNumber(int sqnNumber, NetworkConnection connection) {
		// System.out.println("Expected Sqntnummer: " + connection.getExpectedSQN());
		// System.out.println("Sequence number: " + sqnNumber);
		if (sqnNumber != -1) {
			if (connection.getExpectedSQN() == sqnNumber) {
				// System.out.println("increase SQN");
				connection = getConnection(connection);
				if (connection != null) {
					connection.increaseExpectedSQN();
					return true;
				}
			}
		}
		return false;
	}

	public void prepareSending(byte[] sendMessage, NetworkConnection connection, int messageTyp) {
		NetworkConnection tempConnection = getConnection(connection);
		if (tempConnection != null) {
			Header header = new Header();
			header.setMessageType((byte) messageTyp);
			header.setDataSize(sendMessage.length);
			handleSequences(header, sendMessage, tempConnection);
			addMessageToQueue(Bytes.concat(buildHeader(header), sendMessage), tempConnection.getAddress());
		}

	}

	protected void addMessageToQueue(byte[] sendMessage, Address address) {
		InetAddress ipAddress;
		try {
			ipAddress = InetAddress.getByName(address.getIpAddress());
			DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, ipAddress, address.getPort());
			if (!sendMessageQueue.offer(packet)) {
				System.out.println("send messagequeue full, dumped message");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void sendHeartbeat() {
		Header heartBeatHeader = new Header();
		heartBeatHeader.setMessageType((byte) Constants.HEARTBEAT);
		tHeartbeat = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!tHeartbeat.isInterrupted()) {
					try {
						Thread.sleep(Constants.SLEEP_TIME_MS_HEARTBEAT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					NetworkConnection connection;
					for (int i = 0; i < connections.size(); i++) {
						connection = connections.get(i);
						heartBeatHeader.setAckNumber(connection.getExpectedSQN());
						byte[] sendMessage = Bytes.concat(buildHeader(heartBeatHeader),
								new byte[Constants.MAX_DATA_SIZE]);
						addMessageToQueue(sendMessage, connection.getAddress());
					}
				}
			}
		});
		tHeartbeat.start();
	}

	protected void startAging() {
		tAging = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!tAging.isInterrupted()) {
					try {
						Thread.sleep(Constants.SLEEP_TIME_MS_HEARTBEAT);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					ArrayList<Message> messages;

					for (int i = 0; i < connections.size(); i++) {
						NetworkConnection connection = connections.get(i);
						synchronized (connections) {
						
						connection.increaseHeartbeatCounter();
						if (connection.getHeartBeatCounter() >= Constants.MAX_HEARTBEAT_COUNTER) {
							connection.setConnected(false);
							connection.deleteAllMessages();
						}
						if (connection.getHeartBeatCounter() >= Constants.MAX_HEARTBEAT_CONNECTIONLOST) {
							System.out.println("remove Connection: " + connections.get(i).getAddress().getPort());
							connections.remove(i);

						}
						}
						messages = connection.getMessages();
						for (int j = 0; j < messages.size(); j++) {
							Message m = messages.get(j);
							m.decreaseCounter();
							if (m.getCounter() >= 0) {
								m.getHeader().setAckNumber(connection.getExpectedSQN());
								addMessageToQueue(Bytes.concat(buildHeader(m.getHeader()), m.getMessage()),
										connection.getAddress());
							} else {
							  System.out.println("Nachricht wurde gelöscht: " + m.getHeader().getSequenceNumber());
							  messages.remove(j);
							}
						}
					}
				}
			}
		});
		tAging.start();
	}

	protected byte[] readData(byte[] dataAndHeader, Header receivedHeader) {
		int dataSize = dataAndHeader.length - Constants.HEADER_SIZE;
		readHeader(dataAndHeader, receivedHeader);
		byte[] rawData = new byte[dataSize];
		System.arraycopy(dataAndHeader, Constants.HEADER_SIZE, rawData, 0, dataSize);

		return rawData;
	}

	protected int readHeader(byte[] data, Header header) {
		int size = 0, sqnNumber = 0, ackNumber = 0;

		// Set message type
		header.setMessageType(data[Constants.HEADER_INDEX_MESSAGE_TYPE]);

		// Set Integers
		byte[] tempBuffer = new byte[4];

		// Set data size
		System.arraycopy(data, Constants.HEADER_INDEX_DATA_SIZE, tempBuffer, 0, Constants.SIZE_OF_INT_BYTE);
		ByteBuffer wrapped = ByteBuffer.wrap(tempBuffer);
		size = wrapped.getInt();

		// Set sequence number
		System.arraycopy(data, Constants.HEADER_INDEX_SQN_NUMBER, tempBuffer, 0, Constants.SIZE_OF_INT_BYTE);
		wrapped = ByteBuffer.wrap(tempBuffer);
		sqnNumber = wrapped.getInt();

		// Set acknowledgement number
		System.arraycopy(data, Constants.HEADER_INDEX_ACK_NUMBER, tempBuffer, 0, Constants.SIZE_OF_INT_BYTE);
		wrapped = ByteBuffer.wrap(tempBuffer);
		ackNumber = wrapped.getInt();

		// Write to Header
		header.setDataSize(size);
		header.setSequenceNumber(sqnNumber);
		header.setAckNumber(ackNumber);
		return size;
	}

	protected byte[] buildHeader(Header sendHeader) {
		byte[] data = new byte[Constants.HEADER_SIZE];
		byte[] size = new byte[Constants.SIZE_OF_INT_BYTE];
		byte[] sqnNumber = new byte[Constants.SIZE_OF_INT_BYTE];
		byte[] ackNumber = new byte[Constants.SIZE_OF_INT_BYTE];

		data[Constants.HEADER_INDEX_MESSAGE_TYPE] = sendHeader.getMessageType();

		for (int i = 0; i < Constants.SIZE_OF_INT_BYTE; i++) {
			size[i] = (byte) (sendHeader.getDataSize() >> 24 - i * Byte.SIZE);
			sqnNumber[i] = (byte) (sendHeader.getSequenceNumber() >> 24 - i * Byte.SIZE);
			ackNumber[i] = (byte) (sendHeader.getAckNumber() >> 24 - i * Byte.SIZE);
		}
		System.arraycopy(size, 0, data, Constants.HEADER_INDEX_DATA_SIZE, Constants.SIZE_OF_INT_BYTE);
		System.arraycopy(sqnNumber, 0, data, Constants.HEADER_INDEX_SQN_NUMBER, Constants.SIZE_OF_INT_BYTE);
		System.arraycopy(ackNumber, 0, data, Constants.HEADER_INDEX_ACK_NUMBER, Constants.SIZE_OF_INT_BYTE);
		return data;
	}

	protected void addMessageToAcknowledgements(Header header, byte[] sendMessage, NetworkConnection connection) {
		Message message = new Message(header, sendMessage, connection.getSendSQN());
		connection.addMessage(message);
	}

	private void heartbeatHandler(NetworkConnection connection) {
		synchronized (connections) {
			
			connection.setHeartBeatCounter(0);
			connection.setConnected(true);
		}

	}

	protected NetworkConnection getConnection(NetworkConnection connection) {
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).equals(connection)) {
				return connections.get(i);
			}
		}
		return null;
	}

	public void startNewCommunication(NetworkConnection connection) {

		if (getConnection(connection) == null) {

			synchronized (connections) {
				connections.add(connection);
				System.out.println("Add Connection Port(Startup): " + connection.getAddress().getPort());
			}
			Header sendHeader = new Header();
			sendHeader.setMessageType((byte) Constants.STARTUP_CONVERSATION);
			sendHeader.setDataSize(Constants.SIZE_OF_INT_BYTE);
			byte[] data = StaticMethods.parseIntToByte(id);
			addMessageToQueue(Bytes.concat(buildHeader(sendHeader), data), connection.getAddress());
		}
	}

	public NetworkConnection getConnectionByString(String parameterList) {
		JSONObject json = new JSONObject();
		JSONParser jparser = new JSONParser();
		int racId = -1;
		try {
			json = (JSONObject) jparser.parse(parameterList);
			racId = (int) (long) json.get("raId");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).getAddress().getId() == racId) {
				return connections.get(i);
			}
		}
		return null;
	}

	public int getListeningPort() {
		return socket.getLocalPort();
	}

	protected void startupConversationHandler(NetworkConnection connection) {
		if (getConnection(connection) == null) {
			synchronized (connections) {
				connections.add(connection);

			}
			System.out.println("Add Connection Port(Handler): " + connection.getAddress().getPort());

		}
	}

	@Override
	public void invoke(String functionName, String parameterList, IStub stub) {
		JSONObject json = new JSONObject();

		json.put(Constants.FUNCTIONNAME, functionName);
		json.put(Constants.PARAMETERLIST, parameterList);
		StubConnection stubcon = new StubConnection(stub, null);
		int i = stubConnection.indexOf(stubcon);
		if (i != -1 && stubConnection.get(i).getConnection() != null) {
			json.put("StubId", i);
			prepareSending(json.toString().getBytes(), stubConnection.get(i).getConnection(), Constants.FUNCTION_CALL);
		} else {
			System.out.println("Connection Unbekannt");
		}

	}

	public boolean isConnected(IStub stub) {
		StubConnection stu = new StubConnection(stub, null);
		for (StubConnection stubbi : stubConnection) {
			if (stubbi.equals(stu) && stubbi.getConnection() != null) {
				return getConnection(stubbi.getConnection()).isConnected();
			}
		}
		return false;
	}

	public void notifyAboutListChanges() {
		ArrayList<Address> list = mwNameserverClient.getReturnListRac();
		for (int i = 0; i < stubConnection.size(); i++) {
			for (Address adr : list) {
				IStub stub = stubConnection.get(i).getStub();
				if (stub instanceof RacStub && stub.getTargetId() == adr.getId()
						&& (stubConnection.get(i).getConnection() == null
								|| stubConnection.get(i).getConnection().getAddress() != adr)) {
					NetworkConnection con = new NetworkConnection(adr);
					startNewCommunication(con);
					stubConnection.get(i).setConnection(getConnection(con));
				}
			}
		}

		list = mwNameserverClient.getReturnListPhilo();
		for (int i = 0; i < stubConnection.size(); i++) {
			for (Address adr : list) {
				IStub stub = stubConnection.get(i).getStub();
				if (stub instanceof PhiloStub && stub.getTargetId() == adr.getId()
						&& (stubConnection.get(i).getConnection() == null
								|| stubConnection.get(i).getConnection().getAddress() != adr)) {
					NetworkConnection con = new NetworkConnection(adr);
					startNewCommunication(con);
					stubConnection.get(i).setConnection(getConnection(con));
				}
			}
		}
		list = mwNameserverClient.getViewList();
		for (int i = 0; i < stubConnection.size(); i++) {
			for (Address adr : list) {
				IStub stub = stubConnection.get(i).getStub();
				if (stub instanceof ViewStub && stub.getTargetId() == adr.getId()
						&& (stubConnection.get(i).getConnection() == null
								|| stubConnection.get(i).getConnection().getAddress() != adr)) {
					NetworkConnection con = new NetworkConnection(adr);
					startNewCommunication(con);
					stubConnection.get(i).setConnection(getConnection(con));
				}
			}
		}

	}

	public void register(IStub stub) {
		stubConnection.add(new StubConnection(stub, null));
		notifyAboutListChanges();
	}
}
