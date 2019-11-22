package test;



import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulationPosition;


public class SimRacWest {

	public static void main(String[] args) {
		RacFactory.createRac(3, CaDSRoboticArmPhilosopherSimulationPosition.WEST);

	}
}
