package statemachine;

import structure.Constants;

public class MoveBlockedRightState extends PhiloState {
	private PhiloState oldState;
	public MoveBlockedRightState(PhiloState oldState,PhiloStateMachine statemachine) {
		super(statemachine);
		this.oldState=oldState;
		
	}

	
	@Override
	public int getIntState() {
		return Constants.MOVE_BLOCKED_RIGHT;
	}

	@Override
	public String toString() {
		return "MoveBlockedRight";
	}


	@Override
	protected void runState() {
		
	}


	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(leftState instanceof MoveRightState || leftState instanceof WaitRightState) {
			return this;
		}
		return oldState;
	}

}
