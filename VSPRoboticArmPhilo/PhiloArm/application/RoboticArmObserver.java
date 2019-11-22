package application;

import java.util.concurrent.TimeUnit;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulation;
import org.cads.vs.roboticArm.hal.simulation.ICaDSRoboticArmPhilosopherSimulationWorkplaceGUISynchronisation;

public class RoboticArmObserver {
	private ICaDSRoboticArm roboticArm;
	private int oldGripperValue;
	private int oldBackForthValue;
	private int oldLeftRightValue;
	private int oldUpDownValue;
	private boolean oldHoldingResource;
	private Thread compareThread;
	private boolean isRunning;
	private RoboticArmController controller;
	private int id;
	private  CaDSRoboticArmPhilosopherSimulation simPhilo;
	private ICaDSRoboticArmPhilosopherSimulationWorkplaceGUISynchronisation synch;
	public RoboticArmObserver(int id, ICaDSRoboticArm roboticArm, RoboticArmController controller) {
		init(id,roboticArm,controller);
	}
	public void setSimPhilo(CaDSRoboticArmPhilosopherSimulation simPhilo) {
		this.simPhilo = simPhilo;
		this.oldHoldingResource=simPhilo.isHoldingResource();

	}
	private void init(int id, ICaDSRoboticArm roboticArm, RoboticArmController controller) {
		this.id = id;
		this.roboticArm = roboticArm;
		this.controller = controller;
		oldBackForthValue = this.roboticArm.getBackForthPercentage();
		oldGripperValue = this.roboticArm.getOpenClosePercentage();
		oldLeftRightValue = this.roboticArm.getLeftRightPercentage();
		oldUpDownValue = this.roboticArm.getUpDownPercentage();
		System.out.println("BackForth: " + oldBackForthValue);
		System.out.println("Gripper: " + oldGripperValue);
		System.out.println("LeftRight: " + oldLeftRightValue);
		System.out.println("UpDown: " + oldUpDownValue);
		isRunning = true;
		thread();
	}
	private void thread() {
		compareThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					if (oldBackForthValue != roboticArm.getBackForthPercentage()) {
						controller.setCurrentBackForth(roboticArm.getBackForthPercentage());
						oldBackForthValue = roboticArm.getBackForthPercentage();
					}
					if (oldGripperValue != roboticArm.getOpenClosePercentage()) {
						controller.setCurrentGripper(roboticArm.getOpenClosePercentage());
						oldGripperValue = roboticArm.getOpenClosePercentage();
					}
					if (oldLeftRightValue != roboticArm.getLeftRightPercentage()) {
						controller.setCurrentLeftRight(roboticArm.getLeftRightPercentage());
						oldLeftRightValue = roboticArm.getLeftRightPercentage();
					}
					if (oldUpDownValue != roboticArm.getUpDownPercentage()) {
						controller.setCurrentUpDown(roboticArm.getUpDownPercentage());
						oldUpDownValue = roboticArm.getUpDownPercentage();
					}
					if(simPhilo!=null && oldHoldingResource!=simPhilo.isHoldingResource()) {
						oldHoldingResource=simPhilo.isHoldingResource();
						controller.setCurrentIsHolding(oldHoldingResource);

					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		compareThread.start();
	}
	

	protected void finalize() {
		isRunning = false;
	}
}
