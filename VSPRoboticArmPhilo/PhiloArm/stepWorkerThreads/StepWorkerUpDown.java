package stepWorkerThreads;

import structure.NetworkConnection;
import stubs.RacSkeleton;

public class StepWorkerUpDown extends StepWorker {
	public StepWorkerUpDown(NetworkConnection connection, RacSkeleton skeleton, int target) {
		super(connection, skeleton, target);
		currentPosition = skeleton.getCurrentUpDown();
	}
	
	public StepWorkerUpDown(RacSkeleton skeleton, int target) {
		super(skeleton, target);
		currentPosition = skeleton.getCurrentUpDown();
	}

	@Override
	public void makeStep() {


		
		if (skeleton.getCurrentUpDown()< targetPosition) {
			currentPosition+=stepDistance;
			skeleton.setTargetUpDown(currentPosition);

		} else if ( skeleton.getCurrentUpDown() > targetPosition) {
			currentPosition-=stepDistance;
			skeleton.setTargetUpDown(currentPosition);
		}

	}
}
