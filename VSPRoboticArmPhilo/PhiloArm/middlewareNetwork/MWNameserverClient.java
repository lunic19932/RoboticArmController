package middlewareNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import staticMethods.StaticMethods;
import structure.Address;
import structure.Constants;

public class MWNameserverClient {
	private String ipAdressNameserver;
	private int portNameserver;
	private DatagramSocket socket;
	private boolean sequentlyGetRacList;
	private boolean sequentlyRegisterRAC;
	private boolean sequentlyRegisterPhilo;
	private boolean sequentlyGetPhiloList;
	private boolean sequentlyRegisterView;
	private boolean sequentlyGetViewList;
	private boolean nameserverIpSetted = false;
	private boolean isReceiving = false;
	private int id;
	private String ipAdress;
	private int listeningPort;
	private boolean isRunning;
	private int isRacRegistered = 0;
	private int isPhiloRegistered = 0;
	private int isViewRegistered = 0;
	private ArrayList<Address> racList = new ArrayList<Address>();
	private ArrayList<Address> philoList = new ArrayList<Address>();
	private ArrayList<Address> viewList = new ArrayList<Address>();
	private Thread receiveThread, handleMessageThread, sendThread, trySendingMessages;
	private LinkedBlockingQueue<DatagramPacket> receiveMessageQueue = new LinkedBlockingQueue<DatagramPacket>(
			Constants.QUEUE_CAPACITY);
	private LinkedBlockingQueue<DatagramPacket> sendMessageQueue = new LinkedBlockingQueue<DatagramPacket>(
			Constants.QUEUE_CAPACITY);
	MWNetwork mw;
	public MWNameserverClient(MWNetwork mw,int id, int listingPort) throws SocketException {
		this.mw=mw;
		isRunning = true;
		ipAdressNameserver = Constants.BROADCAST_ADDRESS;
		portNameserver = Constants.NAMESERVER_PORT;
		socket = new DatagramSocket();
		socket.setBroadcast(true);
		this.id = id;

		try {
			this.ipAdress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		receiveThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					receiveMessages();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		receiveThread.start();

		handleMessageThread = new Thread(new Runnable() {
			@Override
			public void run() {
				handleMessages();
			}
		});
		handleMessageThread.start();

		sendThread = new Thread(new Runnable() {
			@Override
			public void run() {
				sendMessages();
			}
		});
		sendThread.start();

		trySendingMessages = new Thread(new Runnable() {
			@Override
			public void run() {
				trySendingMessages();
			}
		});
		trySendingMessages.start();

		this.listeningPort = listingPort;
	}

	public void receiveMessages() throws IOException, ParseException {
		isReceiving = true;
		while (isReceiving) {
			byte[] receiveBuffer = new byte[Constants.MAX_PACKET_SIZE_NAMESERVER];
			DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
			socket.receive(receivePacket);
			if (!receiveMessageQueue.offer(receivePacket)) {
				System.out.println("receive message queue full, dumped message");
			}
		}
	}

	private void handleMessages() {
		while (isRunning) {
			try {
				DatagramPacket receivePacket = receiveMessageQueue.take();
				if (receivePacket != null) {
					if (!nameserverIpSetted) {
						ipAdressNameserver = receivePacket.getAddress().toString().replaceAll("/", "");
						socket.setBroadcast(false);
						nameserverIpSetted = true;
					}
					int size = StaticMethods.readNServerHeader(receivePacket);
					byte[] buffer = new byte[size];
					System.arraycopy(receivePacket.getData(), Constants.HEADER_SIZE_NAMESERVER, buffer, 0, size);
					handlePacket(buffer);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void handlePacket(byte[] data) throws ParseException {
		JSONObject json = new JSONObject();
		JSONObject jsonReturn = new JSONObject();
		JSONParser jparser = new JSONParser();
		String string = new String(data).trim();
		json = (JSONObject) jparser.parse(string);
		switch ((String) json.get(Constants.FUNCTIONNAME)) {
		case Constants.REGISTER_RAC_FUNCTION:
			if (isRacRegistered != getIsRegistered(json)) {
				isRacRegistered = getIsRegistered(json);
				
			}
			break;
		case Constants.GETRACLIST_FUNCTION:
			if (isListChanged(racList, getList(json))) {
				racList = getList(json);
				mw.notifyAboutListChanges();
			}
			break;
		case Constants.REGISTER_PHILO_FUNCTION:
			if (isPhiloRegistered != getIsRegistered(json)) {
				isPhiloRegistered = getIsRegistered(json);
			}
			break;
		case Constants.GETPHILOLIST_FUNCTION:
	
			
			if (isListChanged(philoList,  getList(json))) {
				philoList =  getList(json);
				mw.notifyAboutListChanges();
			}
			break;
			
		case Constants.REGISTER_VIEW_FUNCTION:
			if (isViewRegistered != getIsRegistered(json)) {
				isViewRegistered = getIsRegistered(json);
			}
			break;
		case Constants.GETVIEWLIST_FUNCTION:
			
			if (isListChanged(viewList,  getList(json))) {
				viewList =  getList(json);
				mw.notifyAboutListChanges();
			}
			break;
		}

	}

	private boolean isListChanged(ArrayList<Address> oldList, ArrayList<Address> newList) {
		for (Address address : oldList) {
			if (!newList.contains(address)) {
				return true;
			}
		}
		for (Address address : newList) {
			if (!oldList.contains(address)) {
				return true;
			}
		}
		return false;
	}

	private JSONObject createRegisterRACJson() {
		JSONObject json = new JSONObject();
		json.put(Constants.FUNCTIONNAME, Constants.REGISTER_RAC_FUNCTION);
		json.put(Constants.IDOFREGISTERED, id);
		json.put(Constants.IPOFREGISTERED, ipAdress);
		json.put(Constants.LISTENINGPORTOFREGISTERED, listeningPort);
		return json;
	}

	private JSONObject createRegisterViewJson() {
		JSONObject json = new JSONObject();
		json.put(Constants.FUNCTIONNAME, Constants.REGISTER_VIEW_FUNCTION);
		json.put(Constants.IDOFREGISTERED, id);
		json.put(Constants.IPOFREGISTERED, ipAdress);
		json.put(Constants.LISTENINGPORTOFREGISTERED, listeningPort);
		return json;
	}
	private JSONObject createRegisterPhiloJson() {
		JSONObject json = new JSONObject();
		json.put(Constants.FUNCTIONNAME, Constants.REGISTER_PHILO_FUNCTION);
		json.put(Constants.IDOFREGISTERED, id);
		json.put(Constants.IPOFREGISTERED, ipAdress);
		json.put(Constants.LISTENINGPORTOFREGISTERED, listeningPort);
		return json;
	}

	private JSONObject createGetRacListJson() {
		JSONObject json = new JSONObject();
		json.put(Constants.FUNCTIONNAME, Constants.GETRACLIST_FUNCTION);
		return json;
	}

	private JSONObject createGetPhiloListJson() {
		JSONObject json = new JSONObject();
		json.put(Constants.FUNCTIONNAME, Constants.GETPHILOLIST_FUNCTION);
		return json;
	}
	private JSONObject createGetViewListJson() {
		JSONObject json = new JSONObject();
		json.put(Constants.FUNCTIONNAME, Constants.GETVIEWLIST_FUNCTION);
		return json;
	}
	private ArrayList<Address> getList(JSONObject jsonReturn) throws ParseException {
		ArrayList<Address> returnvalue = new ArrayList<Address>();
		if (jsonReturn != null) {
			JSONArray jArray = (JSONArray) jsonReturn.get("List");
			int id;
			String ipAddress;
			int port;
			if (jArray != null) {
				for (int i = 0; i < jArray.size(); i++) {
					id = (int) (long) ((JSONObject) jArray.get(i)).get(Constants.IDOFREGISTERED);
					ipAddress = (String) ((JSONObject) jArray.get(i)).get(Constants.IPOFREGISTERED);
					port = (int) ((long) ((JSONObject) jArray.get(i)).get(Constants.LISTENINGPORTOFREGISTERED));
					returnvalue.add(new Address(id, ipAddress, port));
				}
			}
		}
		return returnvalue;
	}

	private int getIsRegistered(JSONObject jsonReturn) {
		return (int) ((long) jsonReturn.get(Constants.RETURNVALUE));
	}

	private void trySendingMessages() {
		while (isRunning) {
			try {
				if (sequentlyRegisterRAC) {
					addRegisterRacToSendQueue();
				}
				if (sequentlyGetRacList) {
					addRacListToSendQueue();
				}
				if (sequentlyRegisterPhilo) {
					addRegisterPhiloToSendQueue();
				}
				if (sequentlyGetPhiloList) {
					addPhiloListToSendQueue();
				}
				if (sequentlyRegisterView) {
					addRegisterViewToSendQueue();
				}
				if (sequentlyGetViewList) {
					addViewListToSendQueue();
				}
				Thread.sleep(Constants.SLEEP_TIME_MS_NAMESERVER);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void addRegisterPhiloToSendQueue() throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAdressNameserver);
		byte[] sendMessage = StaticMethods.jsonToByteMessageNServer(createRegisterPhiloJson());
		DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, portNameserver);
		if (!sendMessageQueue.offer(packet)) {
			System.out.println("send messagequeue full, dumped message");
		}
	}

	private void addPhiloListToSendQueue() throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAdressNameserver);
		byte[] sendMessage = StaticMethods.jsonToByteMessageNServer(createGetPhiloListJson());
		DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, portNameserver);
		if (!sendMessageQueue.offer(packet)) {
			System.out.println("send messagequeue full, dumped message");
		}
	}

	private void addRacListToSendQueue() throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAdressNameserver);
		byte[] sendMessage = StaticMethods.jsonToByteMessageNServer(createGetRacListJson());
		DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, portNameserver);
		if (!sendMessageQueue.offer(packet)) {
			System.out.println("send messagequeue full, dumped message");
		}
	}

	private void addRegisterRacToSendQueue() throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAdressNameserver);
		byte[] sendMessage = StaticMethods.jsonToByteMessageNServer(createRegisterRACJson());
		DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, portNameserver);
		if (!sendMessageQueue.offer(packet)) {
			System.out.println("send messagequeue full, dumped message");
		}
	}
	private void addViewListToSendQueue() throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAdressNameserver);
		byte[] sendMessage = StaticMethods.jsonToByteMessageNServer(createGetViewListJson());
		DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, portNameserver);
		if (!sendMessageQueue.offer(packet)) {
			System.out.println("send messagequeue full, dumped message");
		}
	}

	private void addRegisterViewToSendQueue() throws UnknownHostException {
		InetAddress address = InetAddress.getByName(ipAdressNameserver);
		byte[] sendMessage = StaticMethods.jsonToByteMessageNServer(createRegisterViewJson());
		DatagramPacket packet = new DatagramPacket(sendMessage, sendMessage.length, address, portNameserver);
		if (!sendMessageQueue.offer(packet)) {
			System.out.println("send messagequeue full, dumped message");
		}
	}

	private void sendMessages() {
		DatagramPacket packet;
		while (isRunning) {
			try {
				packet = sendMessageQueue.take();
				if (packet != null) {
					socket.send(packet);
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

	public boolean isSequentlyGetRacList() {
		return sequentlyGetRacList;
	}

	public synchronized void setSequentlyGetRacList(boolean sequentlyGetRacList) {
		this.sequentlyGetRacList = sequentlyGetRacList;
	}

	public boolean isSequentlyRegisterRAC() {
		return sequentlyRegisterRAC;
	}

	public synchronized void setSequentlyRegisterRAC(boolean sequentlyRegisterRAC) {
		this.sequentlyRegisterRAC = sequentlyRegisterRAC;
	}

	public boolean isSequentlyRegisterPhilo() {
		return sequentlyRegisterPhilo;
	}

	public synchronized void setSequentlyRegisterPhilo(boolean sequentlyRegisterPhilo) {
		this.sequentlyRegisterPhilo = sequentlyRegisterPhilo;
	}

	public synchronized void setSequentlyGetPhiloList(boolean sequentlyGetPhiloList) {
		this.sequentlyGetPhiloList = sequentlyGetPhiloList;
	}

	public int getReturnRegisteredRac() {
		return isRacRegistered;
	}

	public int getReturnRegisteredPhilo() {
		return isPhiloRegistered;
	}

	public ArrayList<Address> getReturnListRac() {
		return racList;
	}

	public ArrayList<Address> getReturnListPhilo() {
//		System.out.println("listsize: " + philoList.size());
		return philoList;
	}
	

	public ArrayList<Address> getViewList() {
		return viewList;
	}

	public void setViewList(ArrayList<Address> viewList) {
		this.viewList = viewList;
	}

	protected void finalize() {
		isRunning = false;
		sequentlyGetPhiloList = false;
		sequentlyGetRacList = false;
		sequentlyRegisterPhilo = false;
		sequentlyRegisterRAC = false;
		sequentlyGetViewList = false;
		sequentlyRegisterView = false;

	}

	public void setSequentlyRegisterView(boolean sequentlyRegisterView) {
		this.sequentlyRegisterView=sequentlyRegisterView;
	}

	public void setSequentlyGetViewList(boolean sequentlyGetViewList) {
		this.sequentlyGetViewList = sequentlyGetViewList;
	}
	

}
