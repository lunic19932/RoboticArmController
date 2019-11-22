package statemachine;

import structure.Constants;

public class MoveBlockedLeftState extends PhiloState {
	private PhiloState oldState;

	public MoveBlockedLeftState(PhiloState oldState,PhiloStateMachine statemachine) {
		super(statemachine);
		this.oldState=oldState;
		
	}

	@Override
	public void runState() {

	}
	@Override
	public int getIntState() {
		return Constants.MOVE_BLOCKED_LEFT;
	}

	@Override
	public String toString() {
		return "MoveBlockedLeft";
	}

	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(rightState instanceof MoveLeftState || rightState instanceof WaitLeftState) {
			return this;
		}
		return oldState;
	}

}
