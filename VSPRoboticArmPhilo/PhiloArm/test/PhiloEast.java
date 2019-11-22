package test;

import application.PhilosophenController;

public class PhiloEast {
	public static void main(String[] args) {
		
		PhiloEast ptest = new PhiloEast();
//		PhilosophenController north = ptest.createPhiloController(0);
		PhilosophenController east = ptest.createPhiloController(1);
//		PhilosophenController south = ptest.createPhiloController(2);
//		PhilosophenController west = ptest.createPhiloController(3);

	}

	public PhilosophenController createPhiloController(int id) {
		
		PhilosophenController philoController = new PhilosophenController(id, false);
		return philoController;
	}

}
