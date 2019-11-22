package test;

import application.PhilosophenController;

public class PhiloNorth {
	public static void main(String[] args) {

		PhiloNorth ptest = new PhiloNorth();
		PhilosophenController north = ptest.createPhiloController(0);
	}

	public PhilosophenController createPhiloController(int id) {
		
		PhilosophenController philoController = new PhilosophenController(id, false);
		return philoController;
	}

}
