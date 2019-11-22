package test;

import application.PhilosophenController;

public class PhiloWest {
	public static void main(String[] args) {

		PhiloWest ptest = new PhiloWest();
		PhilosophenController west = ptest.createPhiloController(3);
	}

	public PhilosophenController createPhiloController(int id) {
		
		PhilosophenController philoController = new PhilosophenController(id, false);
		return philoController;
	}

}
