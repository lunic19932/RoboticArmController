package soloTest;

import java.util.Scanner;

import application.ResourceMovement;

public class ResourceMovementTest {	
	

	public static void main(String[] args) {
		ResourceMovement rm=new ResourceMovement(1);
		Scanner scanner=new Scanner(System.in);
		String s="";
		do {
			s=scanner.nextLine();
			switch (s) {
			case "l2m":
				System.out.println("Start MoveLeftToMiddle");
				rm.moveLeftToMiddle();
				break;
			case "r2l":
				System.out.println("Start MoveResourceToLeft");
				rm.moveResourceToLeft();
				break;
			case "r2m":
				System.out.println("Start MoveRightToMiddle");
				rm.moveRightToMiddle();
				break;
			case "r2r":
				System.out.println("Start MoveResourceToRight");
				rm.moveResourceToRight();
				break;
			case "reml1":
				System.out.println("Start MoveResourceToRight");
				rm.setLeftWorkplace(1, false);
				break;
			default:
				break;
			}
		}while(!s.equals("q"));
		
		
	}
}
