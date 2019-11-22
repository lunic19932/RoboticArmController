/**
 * Distributed Systems
 * Robotic Arm Controller
 * 01.05.2019
 * Luis Nickel & Leo Peters
 */

package application;

import java.net.SocketException;
import java.util.concurrent.TimeUnit;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.real.CaDSRoboticArmReal;
import org.cads.vs.roboticArm.hal.simulation.CaDSPhilosopherFeedbackPrint;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulation;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulationPosition;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmSimulation;
import org.cads.vs.roboticArm.hal.simulation.ICaDSRoboticArmPhilosopherSimulationWorkplaceGUISynchronisation;

import interfaces.IRac;
import middlewareNetwork.MWNetwork;
import middlewareNetwork.MWNetworkRac;
import stubs.RacSkeleton;
import stubs.ViewStub;

public class RoboticArmController implements IRac {
	private RacSkeleton skeleton;
	private MWNetwork middleware;
	private ViewStub stub;
	private ICaDSRoboticArm roboticArm;
	private int id;
	private CaDSRoboticArmPhilosopherSimulation philo;
	private ICaDSRoboticArmPhilosopherSimulationWorkplaceGUISynchronisation roboticArmWorkplaceSynch;   
	private RoboticArmObserver ro; 

	public RoboticArmController(int id, boolean local) {
		roboticArm = new CaDSRoboticArmSimulation();
		init(id, local);
	}

	public RoboticArmController(int id, CaDSRoboticArmPhilosopherSimulationPosition pos, boolean local) {
	  try {
		philo = new CaDSRoboticArmPhilosopherSimulation(pos, new CaDSPhilosopherFeedbackPrint());
	  } catch (IllegalStateException e) {
	    e.printStackTrace();
	  }
		roboticArm = philo;
		philo.workplaceMid1Used();
		philo.workplaceLeft1Used();
		philo.workplaceRight1Used();
		philo.workplaceLeft2NotUsed();
		philo.workplaceRight0NotUsed();
		init(id, local);
		ro.setSimPhilo(philo);
		
	}

	public RoboticArmController(int id, String ipAddress, int port, boolean local) {
		roboticArm = new CaDSRoboticArmReal(ipAddress, port);
		init(id, local);
	}

	private void init(int id, boolean local) {
		roboticArm.waitUntilInitIsFinished();
		roboticArm.setBackForthPercentageTo(50);
		roboticArm.setLeftRightPercentageTo(50);
		roboticArm.setOpenClosePercentageTo(50);
		roboticArm.setUpDownPercentageTo(50);
		this.id = id;
		skeleton = new RacSkeleton(this);
		if (!local) {
			try {
				middleware = new MWNetworkRac(id, skeleton);
				stub=new ViewStub(middleware, id);
//				while(!stub.isConnected()) {
//					Thread.sleep(500);
//				}
			} catch (SocketException e) {
				e.printStackTrace();
			}
		} else {
			// middleware = new MWLocalRAC();
//			stub = new SetCurrentValuesStub(new MWLocalStub(id, (MWLocal) middleware));
		}
		System.out.println("Connected");
		ro = new RoboticArmObserver(id, roboticArm, this);
	}

	public void setRobotID(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public void setTargetBackForth(int percentage) {
		roboticArm.setBackForthPercentageTo(percentage);
	}

	@Override
	public void setTargetGripper(int percentage) {
		roboticArm.setOpenClosePercentageTo(percentage);
	}

	@Override
	public void setTargetLeftRight(int percentage) {
		roboticArm.setLeftRightPercentageTo(percentage);

	}

	@Override
	public void setTargetUpDown(int percentage) {
		roboticArm.setUpDownPercentageTo(percentage);
	}

	public void setLeftWorkplace2(boolean isUsed) {
		if (isUsed) {
			philo.workplaceLeft2Used();
		} else {
			philo.workplaceLeft2NotUsed();
		}
	}

	public void setLeftWorkplace1(boolean isUsed) {
		if (isUsed) {
			philo.workplaceLeft1Used();
		} else {
			philo.workplaceLeft1NotUsed();
		}
	}

	public void setRightWorkplace0(boolean isUsed) {
		if (isUsed) {
			philo.workplaceRight0Used();
		} else {
			philo.workplaceRight0NotUsed();
		}
	}

	public void setRightWorkplace1(boolean isUsed) {
		if (isUsed) {
			philo.workplaceRight1Used();
		} else {
			philo.workplaceRight1NotUsed();
		}
	
	}

	public void setMidWorkplace1(boolean isUsed) {
		if (isUsed) {
			philo.workplaceMid1Used();
		} else {
			philo.workplaceMid1NotUsed();
		}
		System.out.println("Hallo");
	}

	@Override
	public int getCurrentBackForth() {
		return roboticArm.getBackForthPercentage();
	}

	@Override
	public int getCurrentGripper() {
		return roboticArm.getOpenClosePercentage();
	}

	@Override
	public int getCurrentLeftRight() {
		return roboticArm.getLeftRightPercentage();
	}

	@Override
	public int getCurrentUpDown() {
		return roboticArm.getUpDownPercentage();
	}

	public void setCurrentBackForth(int percentage) {
			if (stub.isConnected()) {
				stub.setCurrentBackForth(id, percentage);
			}
	}

	public void setCurrentGripper(int percentage) {
			if (stub.isConnected()) {
				stub.setCurrentGripper(id, percentage);
			}
	}

	public void setCurrentLeftRight(int percentage) {
			if (stub.isConnected()) {
				stub.setCurrentLeftRight(id, percentage);
			}
	}

	public void setCurrentUpDown(int percentage) {
			if (stub.isConnected()) {
				stub.setCurrentUpDown(id, percentage);
			}
		}

	public RacSkeleton getSkeleton() {
		return skeleton;
	}

	public void setCurrentIsHolding(boolean oldHoldingResource) {
		if (stub.isConnected()) {
			stub.setCurrentIsHolding(id, oldHoldingResource);
		}
		
	}

	@Override
	public void setOwnWorkplace1(boolean isUsed) {
		if (isUsed) {
			philo.checkWorkplaceAndPutDownOwn1();
		} else {
			philo.checkWorkplaceAndPickUpOwn1();
		}
		
	}
}
