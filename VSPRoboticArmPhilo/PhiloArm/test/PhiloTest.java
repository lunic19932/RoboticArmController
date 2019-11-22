package test;

import java.net.SocketException;
import java.util.Scanner;

import org.cads.vs.roboticArm.hal.simulation.CaDSRoboticArmPhilosopherSimulationPosition;

import application.PhilosophenController;
import application.RoboticArmController;
import middlewareNetwork.MWNetworkPhilo;
import stubs.PhiloStub;

public class PhiloTest {
	public static void main(String[] args) throws SocketException {
		PhilosophenController philoController = new PhilosophenController(2, false);
		RoboticArmController rac =RacFactory.createRac(2, CaDSRoboticArmPhilosopherSimulationPosition.SOUTH);	
		rac.setTargetUpDown(0);
		rac.setTargetBackForth(0);
		rac.setTargetLeftRight(0);
		PhiloStub stub=new PhiloStub(new MWNetworkPhilo(1, null), 2);
		Scanner scaner=new Scanner(System.in);
		String input;
		boolean e0Toogle=false;
		boolean e1Toogle=false;
		boolean s1Toogle=false;
		boolean w1Toogle=false;
		boolean w2Toogle=false;
		boolean n1Toogle=false;
		do{
			input=scaner.nextLine();
			switch (input) {
			case "e0":
				stub.updateResource(1, 1, 0, e0Toogle);
				e0Toogle=!e0Toogle;
				break;
			case "e1":
				stub.updateResource(1, 1, 1, e1Toogle);
				e1Toogle=!e1Toogle;
				break;
			case "s1":
				stub.updateResource(1, 2, 1, s1Toogle);
				s1Toogle=!s1Toogle;
				break;
			case "w1":
				stub.updateResource(3, 3, 1, w1Toogle);
				w1Toogle=!w1Toogle;
				break;
			case "w2":
				stub.updateResource(3, 3, 2, w2Toogle);
				w2Toogle=!w2Toogle;
				break;	
			case "n1":
				stub.updateResource(1, 0, 1, n1Toogle);
				n1Toogle=!n1Toogle;
				break;	

			default:
				break;
			}
		}while (!input.equals("q"));
		stub.updateResource(1, 1, 0, true);
	}
}
