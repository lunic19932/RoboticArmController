package stepWorkerThreads;

import structure.NetworkConnection;
import structure.Constants;
import stubs.RacSkeleton;
import test.RacFactory;

public abstract class StepWorker extends Thread {

	protected RacSkeleton skeleton;
	protected NetworkConnection connection;
	protected int targetPosition;
	protected int stepDistance;
	protected int currentPosition;

	public abstract void makeStep();

	public StepWorker(NetworkConnection connection, RacSkeleton skeleton, int target) {
		this.connection = connection;
		this.skeleton = skeleton;
		this.targetPosition = target;
		this.stepDistance = Constants.STEPDISTANCE;

	}

	public StepWorker(RacSkeleton skeleton, int target) {
		this.connection = new NetworkConnection(null);
		connection.setConnected(true);
		this.skeleton = skeleton;
		this.targetPosition = target;

	}

	@Override
	public void run() {
		if (RacFactory.isSimulation) {
			stepAndWaitSimulation();
		} else {
			stepAndWaitReal();
		}
	}

	private void stepAndWaitSimulation() {
		stepDistance = 1;
		while (targetPosition != currentPosition) {
			if (!connection.isConnected()) {
				System.out.println("Connection Lost: Stop Movement");
				break;
			}

			if (currentPosition >= 0 && currentPosition <= 100) {
				makeStep();
			} else {
				System.out.println("current: " + currentPosition);
				System.out.println("invalide Position:exit Thread");
				break;
			}
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void stepAndWaitReal() {
		while (!(targetPosition + (stepDistance / 2) >= currentPosition
				&& targetPosition - (stepDistance / 2) <= currentPosition)) {
			if (!connection.isConnected()) {
				System.out.println("Connection Lost: Stop Movement");
				break;
			}

			if (currentPosition >= 0 && currentPosition <= 100) {
				if (Math.abs(currentPosition - targetPosition) < stepDistance) {
					stepDistance = Math.abs(currentPosition - targetPosition);

				}
				makeStep();
			} else {
				System.out.println("current: " + currentPosition);
				System.out.println("invalide Position:exit Thread");
				break;
			}
			try {
				Thread.sleep(4);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
