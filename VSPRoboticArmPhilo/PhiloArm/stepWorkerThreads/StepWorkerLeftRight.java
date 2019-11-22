package stepWorkerThreads;

import structure.NetworkConnection;
import stubs.RacSkeleton;

public class StepWorkerLeftRight extends StepWorker {

	public StepWorkerLeftRight(NetworkConnection connection, RacSkeleton skeleton, int target) {
		super(connection, skeleton, target);
		currentPosition = skeleton.getCurrentLeftRight();
	}
	
	public StepWorkerLeftRight(RacSkeleton skeleton, int target) {
		super(skeleton, target);
		currentPosition = skeleton.getCurrentLeftRight();
	}

	@Override
	public void makeStep() {
		if (skeleton.getCurrentLeftRight()< targetPosition) {
			currentPosition+=stepDistance;
			skeleton.setTargetLeftRight(currentPosition);
			

		} else if ( skeleton.getCurrentLeftRight() > targetPosition) {
			currentPosition-=stepDistance;
			skeleton.setTargetLeftRight(currentPosition);
		}

	}

}
