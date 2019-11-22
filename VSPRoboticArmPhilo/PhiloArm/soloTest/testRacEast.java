package soloTest;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.simulation.CaDSPhilosopherFeedbackPrint;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulation;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulationPosition;
import org.cads.vs.roboticArm.hal.simulation.ICaDSRoboticArmPhilosopherSimulationWorkplaceGUISynchronisation;

import application.RoboticArmController;
import test.RacFactory;

public class testRacEast {	
	
	public static void main(String[] args) {
//		RacFactory.createRac(1, CaDSRoboticArmPhilosopherSimulationPosition.EAST);
		CaDSRoboticArmPhilosopherSimulation philo=new CaDSRoboticArmPhilosopherSimulation(CaDSRoboticArmPhilosopherSimulationPosition.SOUTH,  new CaDSPhilosopherFeedbackPrint());
		ICaDSRoboticArm roboticArm=philo;
		ICaDSRoboticArmPhilosopherSimulationWorkplaceGUISynchronisation roboticArmWorkplaceSynch = philo;
		
		philo.checkWorkplaceAndPickUpOwn1();
		roboticArmWorkplaceSynch.workplacesLeftNotSynchronized();
		roboticArmWorkplaceSynch.workplacesMidNotSynchronized();
		roboticArmWorkplaceSynch.workplacesRightNotSynchronized();
		roboticArmWorkplaceSynch.workplaceLeft1NotUsed();
		philo.setLeftRightPercentageTo(59);
		philo.setUpDownPercentageTo(13);
		philo.setBackForthPercentageTo(71);
		philo.setOpenClosePercentageTo(0);
	}
	
}
