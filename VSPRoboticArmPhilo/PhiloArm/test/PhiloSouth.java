package test;

import application.PhilosophenController;

public class PhiloSouth {
	public static void main(String[] args) {

		PhiloSouth ptest = new PhiloSouth();
		PhilosophenController south = ptest.createPhiloController(2);

	}

	public PhilosophenController createPhiloController(int id) {
		
		PhilosophenController philoController = new PhilosophenController(id, false);
		return philoController;
	}

}
