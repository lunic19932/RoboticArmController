package statemachine;

import structure.Constants;

public class WaitRightState extends PhiloState {


	public WaitRightState(PhiloStateMachine statemachine) {
		super(statemachine);

	}



	@Override
	public int getIntState() {
		return Constants.WAIT_RIGHT;
	}


	
	@Override
	public String toString() {
		return "WaitRight";
	}



	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(rightState instanceof MoveBlockedRightState) {
			return new MoveRightState(statemachine);
		}
		return this;
	}



	@Override
	protected void runState() {
		
	}
}
