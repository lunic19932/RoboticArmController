package stepWorkerThreads;

import structure.NetworkConnection;
import stubs.RacSkeleton;

public class StepWorkerGripper extends StepWorker {

	public StepWorkerGripper(NetworkConnection connection, RacSkeleton skeleton, int target) {
		super(connection, skeleton, target);
		currentPosition = skeleton.getCurrentGripper();
	}
	
	public StepWorkerGripper(RacSkeleton skeleton, int target) {
		super( skeleton, target);
		currentPosition = skeleton.getCurrentGripper();
	}

	@Override
	public void makeStep() {

		if (skeleton.getCurrentGripper()< targetPosition) {
			currentPosition+=stepDistance;
			skeleton.setTargetGripper(currentPosition);

		} else if ( skeleton.getCurrentGripper() > targetPosition) {
			currentPosition-=stepDistance;
			skeleton.setTargetGripper(currentPosition);
		}
	}

}
