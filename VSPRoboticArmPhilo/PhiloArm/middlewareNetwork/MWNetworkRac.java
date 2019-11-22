package middlewareNetwork;

import java.net.SocketException;

import org.json.simple.JSONObject;
import interfaces.IMWRac;
import stepWorkerThreads.StepWorker;
import stepWorkerThreads.StepWorkerBackForth;
import stepWorkerThreads.StepWorkerGripper;
import stepWorkerThreads.StepWorkerLeftRight;
import stepWorkerThreads.StepWorkerUpDown;
import structure.NetworkConnection;
import structure.Constants;
import stubs.RacSkeleton;

public class MWNetworkRac extends MWNetwork implements IMWRac {

	private RacSkeleton skeleton;

	public MWNetworkRac(int id, RacSkeleton skeleton) throws SocketException {
		super(id);
		this.skeleton = skeleton;
		mwNameserverClient.setSequentlyRegisterRAC(true);
		mwNameserverClient.setSequentlyGetViewList(true);
	}

	@Override
	protected void functionCallHandler(JSONObject json, JSONObject parameter, NetworkConnection connection) {
		StepWorker stepworkerBackForth;
		StepWorker stepworkerGripper;
		StepWorker stepworkerLeftRight;
		StepWorker stepworkerUpDown;
		switch ((String) json.get(Constants.FUNCTIONNAME)) {
		case "setTargetBackForth":
			stepworkerBackForth = new StepWorkerBackForth(connection, skeleton,
					(int) ((long) parameter.get("percentage")));
			stepworkerBackForth.start();
			break;
		case "setTargetGripper":
			stepworkerGripper = new StepWorkerGripper(connection, skeleton, (int) ((long) parameter.get("percentage")));
			stepworkerGripper.start();
			break;
		case "setTargetLeftRight":
			stepworkerLeftRight = new StepWorkerLeftRight(connection, skeleton,
					(int) ((long) parameter.get("percentage")));
			stepworkerLeftRight.start();
			break;
		case "setTargetUpDown":
			stepworkerUpDown = new StepWorkerUpDown(connection, skeleton, (int) ((long) parameter.get("percentage")));
			stepworkerUpDown.start();
			break;
		case "getCurrentBackForth":
			json.put(Constants.RETURNVALUE, skeleton.getCurrentBackForth());
			prepareSending(json.toString().getBytes(), connection, Constants.RETURN_VALUE);
			break;
		case "getCurrentGripper":
			json.put(Constants.RETURNVALUE, skeleton.getCurrentGripper());
			prepareSending(json.toString().getBytes(), connection, Constants.RETURN_VALUE);
			break;
		case "getCurrentLeftRight":
			json.put(Constants.RETURNVALUE, skeleton.getCurrentLeftRight());
			prepareSending(json.toString().getBytes(), connection, Constants.RETURN_VALUE);
			break;
		case "setLeftWorkplace2":
			skeleton.setLeftWorkplace2((boolean)parameter.get("isUsed"));
			break;
		case "setLeftWorkplace1":
			skeleton.setLeftWorkplace1((boolean)parameter.get("isUsed"));
			break;
		case "setRightWorkplace0":
			skeleton.setRightWorkplace0((boolean)parameter.get("isUsed"));
			break;
		case "setRightWorkplace1":
			skeleton.setRightWorkplace1((boolean)parameter.get("isUsed"));
			break;
		case "setMidWorkplace1":
			skeleton.setMidWorkplace1((boolean)parameter.get("isUsed"));
			break;
		case "setOwnWorkplace1":
			skeleton.setOwnWorkplace1((boolean)parameter.get("isUsed"));
			break;
	
		}
	}
}
