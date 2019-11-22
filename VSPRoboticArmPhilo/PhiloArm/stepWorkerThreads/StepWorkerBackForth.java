package stepWorkerThreads;

import structure.NetworkConnection;
import stubs.RacSkeleton;

public class StepWorkerBackForth extends StepWorker {
	public StepWorkerBackForth(NetworkConnection connection, RacSkeleton skeleton, int target) {
		super(connection, skeleton, target);
		currentPosition = skeleton.getCurrentBackForth();
	}
	public StepWorkerBackForth(RacSkeleton skeleton, int target, int id) {
		super(skeleton, target);
		currentPosition = skeleton.getCurrentBackForth();
	}

	@Override
	public void makeStep() {

		if (skeleton.getCurrentBackForth()< targetPosition) {
			currentPosition+=stepDistance;
			skeleton.setTargetBackForth(currentPosition);

		} else if ( skeleton.getCurrentBackForth() > targetPosition) {
			currentPosition-=stepDistance;
			skeleton.setTargetBackForth(currentPosition);
		}
	}

}
