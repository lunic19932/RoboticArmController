package middlewareNetwork;

import java.net.SocketException;

import org.json.simple.JSONObject;

import interfaces.IMWPhilo;
import structure.NetworkConnection;
import structure.Constants;
import stubs.PhiloSkeleton;

public class MWNetworkPhilo extends MWNetwork implements IMWPhilo {

	private PhiloSkeleton skeleton;

	public MWNetworkPhilo(int id, PhiloSkeleton skeleton) throws SocketException {
		super(id);
		this.skeleton = skeleton;
		
		mwNameserverClient.setSequentlyRegisterPhilo(true);
		mwNameserverClient.setSequentlyGetPhiloList(true);
	}

	@Override
	protected void functionCallHandler(JSONObject json, JSONObject parameter, NetworkConnection connection) {
		switch ((String) json.get(Constants.FUNCTIONNAME)) {

		case "changeState":
			skeleton.changeState((int) (long) parameter.get("id"), (int) (long) parameter.get("state"));
			break;
		case "updateResource":
			skeleton.updateResource((int) (long) parameter.get("sender"), (int) (long) parameter.get("workplaceOwner"), (int) (long) parameter.get("workplace"),(boolean)parameter.get("isUsed"));
			break;
		case "isLeftResourceAvailable":
			json.put(Constants.RETURNVALUE, skeleton.isLeftResourceAvailable());
			prepareSending(json.toString().getBytes(), connection, Constants.RETURN_VALUE);
			break;
		case "isRightResourceAvailable":
			json.put(Constants.RETURNVALUE, skeleton.isRightResourceAvailable());
			prepareSending(json.toString().getBytes(), connection, Constants.RETURN_VALUE);
			break;
		}
	}

}
