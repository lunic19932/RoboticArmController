package statemachine;

import structure.Constants;

public class PutLeftResourceMidState extends PhiloState {
	boolean isResourceMid = false;
	private PhiloState oldState;

	public PutLeftResourceMidState(PhiloState oldState, PhiloStateMachine statemachine) {
		super(statemachine);
		this.oldState = oldState;
	}

	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if (isResourceMid) {
			return new MoveBlockedRightState(oldState, statemachine);
		}
		return this;
	}

	@Override
	protected void runState() {
		if (!isResourceMid) {
			statemachine.getPhiloController().putLeftResourceMid();
			isResourceMid = true;
		}
	}

	@Override
	public int getIntState() {
		return Constants.PUT_LEFT_RESOURCE_MID;
	}

	@Override
	public String toString() {
		return "PutLeftResourceMid";
	}

}
