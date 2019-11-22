package statemachine;

import structure.Constants;

public class WaitLeftState extends PhiloState {

	public WaitLeftState(PhiloStateMachine statemachine) {
		super(statemachine);
		
	}


	@Override
	public int getIntState() {
		return Constants.WAIT_LEFT;
	}


	@Override
	public String toString() {
		return "WaitLeft";
	}

	@Override
	protected void runState() {
	}


	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(leftState instanceof MoveBlockedLeftState) {
			return new MoveLeftState(statemachine);
		}
		return this;
	}
}
