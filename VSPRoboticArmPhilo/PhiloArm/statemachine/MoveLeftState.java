package statemachine;

import structure.Constants;

public class MoveLeftState extends PhiloState {
	private boolean isResourceLeft=false;
	public MoveLeftState(PhiloStateMachine statemachine) {
		super(statemachine);

	}

	@Override
	public int getIntState() {
		return Constants.MOVE_LEFT;
	}

	@Override
	public String toString() {
		return "MoveLeft";
	}

	@Override
	protected void runState() {
		if (!isRunning) {
			isRunning = true;
			statemachine.getPhiloController().moveResourceToLeft();
			isResourceLeft = true;

		}
	}

	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(isResourceLeft) {
			return new WaitRightState(statemachine);
		}
		return this;
	}

}
