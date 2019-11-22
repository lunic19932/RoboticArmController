package test;

import org.cads.vs.roboticArm.hal.ICaDSRoboticArm;
import org.cads.vs.roboticArm.hal.real.CaDSRoboticArmReal;
import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulationPosition;


public class SimRacEast{
	public static void main(String[] args){
		RacFactory.createRac(1, CaDSRoboticArmPhilosopherSimulationPosition.EAST); // ,"172.16.1.62",50055

		
		
	}
}
