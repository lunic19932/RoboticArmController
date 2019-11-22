package statemachine;

import structure.Constants;

public class MoveRightState extends PhiloState {

	private boolean isResourceRight=false;
	public MoveRightState(PhiloStateMachine statemachine) {
		super(statemachine);
	}
	@Override
	public void runState() {

		if(!isRunning) {
			isRunning=true;
			statemachine.getPhiloController().moveResourceToRight();
			isResourceRight=true;
		}
	}
	@Override
	public int getIntState() {
		return Constants.MOVE_RIGHT;
	}

	@Override
	public String toString() {
		return "MoveRight";
	}
	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(isResourceRight) {
			return new EatState(statemachine);
		}
		return this;
	}
}
