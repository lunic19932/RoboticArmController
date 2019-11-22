package test;



import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulationPosition;


public class SimRacSouth {

	public static void main(String[] args) {
		RacFactory.createRac(2, CaDSRoboticArmPhilosopherSimulationPosition.SOUTH);

	}
}
