package statemachine;

import structure.Constants;

public class GetResourceState extends PhiloState {
	private boolean resourcesAvailable=false;
	public GetResourceState(PhiloStateMachine statemachine) {
		super(statemachine);
		
		
	}


	@Override
	public int getIntState() {
		return Constants.GET_RESOURCES;
	}


	@Override
	public String toString() {
		return "GetResource";
	}

	@Override
	public void runState() {

		resourcesAvailable=statemachine.getPhiloController().areResourcesAvailable();		
	}


	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(resourcesAvailable) {
			return new WaitLeftState(statemachine);
		}else if(rightState instanceof WaitLeftState) {
			return new PutRightResourceMidState( this,statemachine);
		}else if(leftState instanceof WaitRightState) {
			return new PutLeftResourceMidState(this, statemachine);
		}
		return this;
	}
}
