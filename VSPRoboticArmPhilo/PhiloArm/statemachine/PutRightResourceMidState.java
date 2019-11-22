package statemachine;

import structure.Constants;

public class PutRightResourceMidState extends PhiloState {
	private PhiloState oldstate;
	private boolean isResourceMid=false;

	public PutRightResourceMidState(PhiloState oldstate,PhiloStateMachine statemachine) {
		super(statemachine);
		this.oldstate=oldstate;
	}

	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(isResourceMid) {
			return new MoveBlockedLeftState(oldstate, statemachine);
		}
		return this;
	}

	@Override
	protected void runState() {
		if(!isResourceMid) {
		statemachine.getPhiloController().putRightResourceMid();
		isResourceMid=true;
		}
	}

	@Override
	public int getIntState() {
		return Constants.PUT_RIGHT_RESOURCE_MID;
	}
	@Override
	public String toString() {
		return "PutRightResourceMid";
	}


}
