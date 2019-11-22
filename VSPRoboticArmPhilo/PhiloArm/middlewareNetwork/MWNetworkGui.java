package middlewareNetwork;

import java.net.SocketException;

import org.json.simple.JSONObject;

import structure.Constants;
import structure.NetworkConnection;
import stubs.ViewSkeleton;

public class MWNetworkGui extends MWNetwork  {
	ViewSkeleton skeleton;

	public MWNetworkGui(int id, ViewSkeleton skeleton) throws SocketException {
		super(id);
		this.skeleton = skeleton;
		mwNameserverClient.setSequentlyRegisterView(true);
		mwNameserverClient.setSequentlyGetRacList(true);
	}

	@Override
	protected void functionCallHandler(JSONObject json, JSONObject jParameterliste, NetworkConnection connection) {
		switch ((String) json.get(Constants.FUNCTIONNAME)) {
		case "setCurrentBackForth":
			skeleton.setCurrentBackForth((int) (long) jParameterliste.get("id"),(int) (long) jParameterliste.get("percentage"));
			break;
		case "setCurrentGripper":
			skeleton.setCurrentGripper((int) (long) jParameterliste.get("id"),(int) (long) jParameterliste.get("percentage"));
			break;
		case "setCurrentLeftRight":
			skeleton.setCurrentLeftRight((int) (long) jParameterliste.get("id"),(int) (long) jParameterliste.get("percentage"));
			break;
		case "setCurrentUpDown":
			skeleton.setCurrentUpDown((int) (long) jParameterliste.get("id"),(int) (long) jParameterliste.get("percentage"));
			break;
		case "setCurrentIsHolding":
			skeleton.setCurrentIsHolding((int) (long) jParameterliste.get("id"),(boolean) jParameterliste.get("isHolding"));
			break;
		}

	}

}
