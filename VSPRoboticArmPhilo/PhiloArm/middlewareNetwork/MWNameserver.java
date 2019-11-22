package middlewareNetwork;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import nameserver.Nameserver;
import staticMethods.StaticMethods;
import structure.Address;
import structure.Constants;

public class MWNameserver {
	private boolean isRunning = false;
	private DatagramSocket socket;
	private Nameserver nServer;

	public MWNameserver() throws SocketException {
		socket = new DatagramSocket(Constants.NAMESERVER_PORT);
		nServer = new Nameserver();
	}

	public void getMessage() throws SocketException {
		byte[] receiveBuffer = new byte[Constants.MAX_PACKET_SIZE_NAMESERVER];
		DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		DatagramPacket sendPacket;
		byte[] sendBuffer;
		isRunning = true;
		while (isRunning) {
			try {
				socket.receive(receivePacket);
				int size = StaticMethods.readNServerHeader(receivePacket);
				byte[] buffer = new byte[size];
				System.arraycopy(receivePacket.getData(), Constants.HEADER_SIZE_NAMESERVER, buffer, 0, size);
				sendBuffer = StaticMethods.jsonToByteMessageNServer((handlePacket(buffer)));
				sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, receivePacket.getAddress(),
						receivePacket.getPort());
				socket.send(sendPacket);
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings("unchecked")
	private JSONObject handlePacket(byte[] data) throws ParseException {
		JSONObject json = new JSONObject();
		JSONObject jsonReturn = new JSONObject();
		JSONParser jparser = new JSONParser();
		json = (JSONObject) jparser.parse(new String(data));
		switch ((String) json.get(Constants.FUNCTIONNAME)) {
		case Constants.REGISTER_RAC_FUNCTION:
			jsonReturn = register(json, "RAC");
			break;
		case Constants.GETRACLIST_FUNCTION:
			jsonReturn = getJsonList(nServer.getRacList(), "RAC");
			break;
		case Constants.REGISTER_PHILO_FUNCTION:
			jsonReturn = register(json, "PHILO");
			break;
		case Constants.GETPHILOLIST_FUNCTION:
			jsonReturn = getJsonList(nServer.getPhiloList(), "PHILO");
			break;
		case Constants.REGISTER_VIEW_FUNCTION:
			jsonReturn = register(json, "VIEW");
			break;
		case Constants.GETVIEWLIST_FUNCTION:
			jsonReturn = getJsonList(nServer.getViewList(), "VIEW");
			break;
		}
		return jsonReturn;
	}

	@SuppressWarnings("unchecked")
	private JSONObject getJsonList(LinkedList<Address> returnList, String typ) {
		JSONObject jsonReturn = new JSONObject();
		JSONArray jArray = new JSONArray();
		for (Address i : returnList) {
			JSONObject jsonO = new JSONObject();
			jsonO.put(Constants.IDOFREGISTERED, i.getId());
			jsonO.put(Constants.IPOFREGISTERED, i.getIpAddress());
			jsonO.put(Constants.LISTENINGPORTOFREGISTERED, i.getPort());
			jArray.add(jsonO);
		}
		switch (typ) {
		case "RAC":
			jsonReturn.put(Constants.FUNCTIONNAME, Constants.GETRACLIST_FUNCTION);

			break;
		case "PHILO":
			jsonReturn.put(Constants.FUNCTIONNAME, Constants.GETPHILOLIST_FUNCTION);

			break;
		case "VIEW":
			jsonReturn.put(Constants.FUNCTIONNAME, Constants.GETVIEWLIST_FUNCTION);
			break;
		}
		jsonReturn.put("List", jArray);
		return jsonReturn;
	}

	@SuppressWarnings("unchecked")
	private JSONObject register(JSONObject json, String typ) {
		JSONObject jsonReturn = new JSONObject();
		int id;
		String address;
		int port;
		id = (int) (long) json.get(Constants.IDOFREGISTERED);
		address = (String) json.get(Constants.IPOFREGISTERED);
		port = (int) ((long) json.get(Constants.LISTENINGPORTOFREGISTERED));

		switch (typ) {
		case "RAC":
			jsonReturn.put(Constants.FUNCTIONNAME, Constants.REGISTER_RAC_FUNCTION);
			jsonReturn.put(Constants.RETURNVALUE, nServer.registerRac(id, address, port));
			break;
		case "PHILO":
			jsonReturn.put(Constants.FUNCTIONNAME, Constants.REGISTER_PHILO_FUNCTION);
			jsonReturn.put(Constants.RETURNVALUE, nServer.registerPhilo(id, address, port));
			break;
		
		case "VIEW":
			jsonReturn.put(Constants.FUNCTIONNAME, Constants.REGISTER_VIEW_FUNCTION);
			jsonReturn.put(Constants.RETURNVALUE, nServer.registerView(id, address, port));
		break;
		}
		return jsonReturn;
	}

	@Override
	protected void finalize() {
		socket.close();
	}

	public static void main(String[] args) throws SocketException {
		MWNameserver mwNServer = new MWNameserver();
		mwNServer.getMessage();
	}
}
