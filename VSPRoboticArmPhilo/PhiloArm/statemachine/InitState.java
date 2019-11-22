package statemachine;

import structure.Constants;

public class InitState extends PhiloState {
	private boolean isConnected=false;
	public InitState(PhiloStateMachine statemachine) {
		super(statemachine);
		
	}

	@Override
	public void runState() {
	
		isConnected=statemachine.getPhiloController().isConnected();
		
	}
	
	@Override
	public int getIntState() {
		return Constants.INIT;
	}

	@Override
	public String toString() {
		return "Init";
	}

	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(isConnected) {
			return new SleepState(statemachine);
		}
		return this;
	}
}
