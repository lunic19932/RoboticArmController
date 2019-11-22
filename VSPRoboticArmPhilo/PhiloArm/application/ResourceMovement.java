package application;

import java.net.SocketException;

import interfaces.IMWPhilo;
import interfaces.IMiddleware;
import interfaces.IView;
import middlewareNetwork.MWNetworkGui;
import structure.Constants;
import stubs.RacStub;
import stubs.ViewSkeleton;

public class ResourceMovement implements IView {
	private RacStub stub;
	private ViewSkeleton skeleton;
	private int currentLeftRight = 0;
	private int currentUpDown = 0;
	private int currentBackForth = 0;
	private int currentGripper = 0;
	private boolean isholding = false;
	private MWNetworkGui mw;
	private PhilosophenController philo;

	public ResourceMovement(int id) {
		createStub(id);
	}

	public ResourceMovement(int id, PhilosophenController philo) {
		createStub(id);
		this.philo = philo;
	}

	private void createStub(int id) {
		skeleton = new ViewSkeleton(this);
		try {
			mw = new MWNetworkGui(id, skeleton);
			stub = new RacStub(mw, id);
			skeleton = new ViewSkeleton(this);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	private void moveToOwnWorkplaceLeft() {
		stub.setTargetLeftRight(100);
		stub.setTargetUpDown(25);
		stub.setTargetBackForth(40);
		finishedMovement(100, 25, 40);
	}

	private void moveToOwnWorkplaceMid() {
		stub.setTargetLeftRight(50);
		stub.setTargetUpDown(25);
		stub.setTargetBackForth(40);
		finishedMovement(50, 25, 40);
	}

	private void moveToOwnWorkplaceRight() {
		stub.setTargetLeftRight(0);
		stub.setTargetUpDown(25);
		stub.setTargetBackForth(40);
		finishedMovement(0, 25, 40);
	}

	private void moveToLeftWorkplaceMid() {
		stub.setTargetLeftRight(59);
		stub.setTargetUpDown(13);
		stub.setTargetBackForth(71);
		finishedMovement(59, 13, 71);
	}

	private void moveToRightWorkplaceMid() {
		stub.setTargetLeftRight(41);
		stub.setTargetUpDown(13);
		stub.setTargetBackForth(71);
		finishedMovement(41, 13, 71);
	}

	private void pickUpResource() {
		stub.setTargetGripper(0);
		finishedMovement(true);
		philo.pickedUpResource();

	}

	private void putDownResource() {
		stub.setTargetGripper(100);
		finishedMovement(false);
		philo.putDownResource();
		int targetUpDown = currentUpDown + 10;
		stub.setTargetUpDown(targetUpDown);
		finishedMovement(currentLeftRight, targetUpDown, currentBackForth);
	}

	public void moveToInitPosition() {
		stub.setTargetLeftRight(50);
		stub.setTargetUpDown(50);
		stub.setTargetBackForth(50);
		stub.setTargetGripper(50);
		finishedMovement(50, 50, 50);
		finishedMovement(50);
	}

	private void finishedMovement(int leftRight, int upDown, int backForth) {
		while (!(currentLeftRight == leftRight && currentUpDown == upDown && currentBackForth == backForth)) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void finishedMovement(int gripper) {
		while (currentGripper != gripper) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void finishedMovement(boolean isHolding) {
		while (isholding != isHolding) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void moveResourceToLeft() {
		moveToLeftWorkplaceMid();
		pickUpResource();
		moveToOwnWorkplaceLeft();
		putDownResource();
	}

	public void moveResourceToRight() {
		moveToRightWorkplaceMid();
		pickUpResource();
		moveToOwnWorkplaceRight();
		putDownResource();
	}

	public void moveLeftToMiddle() {
		moveToOwnWorkplaceLeft();
		pickUpResource();
		moveToOwnWorkplaceMid();
		putDownResource();
	}

	public void moveRightToMiddle() {
		moveToOwnWorkplaceRight();
		pickUpResource();
		moveToOwnWorkplaceMid();
		putDownResource();
	}

	@Override
	public void setCurrentBackForth(int id, int percentage) {
		synchronized (this) {
			currentBackForth = percentage;
			notifyAll();
		}

	}

	@Override
	public void setCurrentGripper(int id, int percentage) {
		synchronized (this) {
			currentGripper = percentage;
			notifyAll();
		}
	}

	@Override
	public void setCurrentLeftRight(int id, int percentage) {
		synchronized (this) {
			currentLeftRight = percentage;
			notifyAll();
		}
	}

	@Override
	public void setCurrentUpDown(int id, int percentage) {
		synchronized (this) {
			currentUpDown = percentage;
			notifyAll();
		}
	}

	public ViewSkeleton getSkeleton() {
		return skeleton;
	}

	public RacStub getStub() {
		return stub;
	}

	@Override
	public void setCurrentIsHolding(int id, boolean isHolding) {
		isholding = isHolding;

	}

	public void setOwnWorkplace(boolean isUsed) {
		stub.setOwnWorkplace1(isUsed);
	}

	public void setLeftWorkplace(int workplace, boolean isUsed) {
		switch (workplace) {
		case 1:
			stub.setLeftWorkplace1(isUsed);
			break;
		case 2:
			stub.setLeftWorkplace2(isUsed);
			break;
		default:
			break;
		}
	}

	public void setRightWorkplace(int workplace, boolean isUsed) {
		switch (workplace) {
		case 0:
			stub.setRightWorkplace0(isUsed);
			break;
		case 1:
			stub.setRightWorkplace1(isUsed);
			break;
		default:
			break;
		}
	}

	public void setMidWorkplace(boolean isUsed) {
		stub.setMidWorkplace1(isUsed);
	}

	public void moveMiddleToLeft() {
		moveToOwnWorkplaceMid();
		pickUpResource();
		moveToOwnWorkplaceLeft();
		putDownResource();

	}

	public void moveMiddleToRight() {
		moveToOwnWorkplaceMid();
		pickUpResource();
		moveToOwnWorkplaceRight();
		putDownResource();
	}
}
