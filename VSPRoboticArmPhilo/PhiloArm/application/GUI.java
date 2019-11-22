/**
 * Distributed Systems
 * Robotic Arm Controller
 * 01.05.2019
 * Luis Nickel & Leo Peters
 */

package application;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;

import org.cads.vs.roboticArm.gui.CaDSRoboticArmControlGUI;
import org.cads.vs.roboticArm.gui.ICaDSRoboticArmControlGUIManipulator;
import org.cads.vs.roboticArm.gui.ICaDSRoboticArmControlGUIOutput;
import org.cads.vs.roboticArm.gui.ICaDSRoboticArmControlGUIRefresh;

import interfaces.IView;
import middlewareNetwork.MWNetworkGui;
import structure.Constants;
import structure.RoboticArmValues;

import stubs.RacStub;
import stubs.ViewSkeleton;

public class GUI implements ICaDSRoboticArmControlGUIRefresh, ICaDSRoboticArmControlGUIOutput, IView{
	private HashMap<String, RoboticArmValues> roboticArmMap;
	private HashMap<String, Integer> racStringNumber;
	private ICaDSRoboticArmControlGUIManipulator scene;
	private String selectedArm;
	private MWNetworkGui middleware;
	private ViewSkeleton skeleton;
	private ArrayList<RacStub> stub;
	private String clientName;
	private int id;
	public GUI(int id) throws SocketException {
		this.id=id;
		roboticArmMap = new HashMap<String, RoboticArmValues>();
		scene = new CaDSRoboticArmControlGUI(this, this);
		scene.startGUIRefresh(Constants.REFRESH_START_VALUE);
		skeleton = new ViewSkeleton(this);
		middleware = new MWNetworkGui(id, skeleton);
		stub=new ArrayList<RacStub>();
		selectedArm="Rac1";
		racStringNumber=new HashMap<String, Integer>();
		init();
		
	}
	private void init() throws SocketException {
		for(int i=0;i<3;i++) {
			scene.addRoboticArmService("Rac"+i);
			racStringNumber.put("Rac"+i,i);
			roboticArmMap.put("Rac"+i, new RoboticArmValues());
			stub.add(new RacStub(middleware,i));
		}
		scene.setChoosenRoboticArmService("Rac1");
	}

	@Override
	public void newBackForthPercentage(int percentage) {
		if (selectedArm != null) {
			stub.get(racStringNumber.get(selectedArm)).setTargetBackForth(percentage);
		}
	}

	@Override
	public void newGripperPercentage(int percentage) {
		if (selectedArm != null) {
			stub.get(racStringNumber.get(selectedArm)).setTargetGripper( percentage);
		}
	}

	@Override
	public void newLeftRightPercentage(int percentage) {
		if (selectedArm != null) {
			stub.get(racStringNumber.get(selectedArm)).setTargetLeftRight( percentage);
			System.out.println("Hallo");
		}
	}

	@Override
	public void newUpDownPercentage(int percentage) {
		if (selectedArm != null) {
			stub.get(racStringNumber.get(selectedArm)).setTargetUpDown( percentage);
		}
	}

	@Override
	public void update(String comboBoxText) {
		selectedArm = comboBoxText;
	}

	@Override
	public int getCurrentBackForthPercentage() {
		if (selectedArm != null) {
			return roboticArmMap.get(selectedArm).getCurrentBackForth();
		}
		return 0;
	}

	@Override
	public int getCurrentGripperPercentage() {
		if (selectedArm != null) {
			return roboticArmMap.get(selectedArm).getCurrentGripper();
		}
		return 0;
	}

	@Override
	public int getCurrentLeftRightPercentage() {
		if (selectedArm != null) {
			return roboticArmMap.get(selectedArm).getCurrentLeftRight();
		}
		return 0;
	}

	@Override
	public int getCurrentUpDownPercentage() {
		if (selectedArm != null) {
			return roboticArmMap.get(selectedArm).getCurrentUpDown();
		}
		return 0;
	}



	@Override
	public void setCurrentBackForth(int id, int percentage) {
		System.out.println(percentage);
		roboticArmMap.get("Rac"+id).setCurrentBackForth(percentage);
	}

	@Override
	public void setCurrentGripper(int id, int percentage) {
		roboticArmMap.get("Rac"+id).setCurrentGripper(percentage);
	}

	@Override
	public void setCurrentLeftRight(int id, int percentage) {
		roboticArmMap.get("Rac"+id).setCurrentLeftRight(percentage);
	}

	@Override
	public void setCurrentUpDown(int id, int percentage) {
		roboticArmMap.get("Rac"+id).setCurrentUpDown(percentage);
	}
	@Override
	public void setCurrentIsHolding(int id, boolean isHolding) {
		// TODO Auto-generated method stub
		
	}

}
