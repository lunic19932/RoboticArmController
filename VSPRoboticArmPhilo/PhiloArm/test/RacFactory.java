package test;


import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulationPosition;

import application.RoboticArmController;

public class RacFactory {
	public static boolean isSimulation;
	
	public static RoboticArmController createRac(int id, CaDSRoboticArmPhilosopherSimulationPosition pos) {

			isSimulation = true;
			RoboticArmController rac = new RoboticArmController( id, pos,false);

			return rac;

	}
	
	public static RoboticArmController createRac(int id) {
			isSimulation = true;
			RoboticArmController rac = new RoboticArmController(id,false);
			return rac;
	
	}
	
	public static RoboticArmController createRac(int id,String ipAddress,int port) {
			isSimulation = false;
			RoboticArmController rac = new RoboticArmController(id,ipAddress,port,false);
			return rac;
	
	}
}
