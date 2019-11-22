package statemachine;

public abstract class PhiloState {
	protected boolean isRunning = false;
	protected PhiloStateMachine statemachine;

	public PhiloState(PhiloStateMachine statemachine) {
		this.statemachine = statemachine;
	}


	public abstract PhiloState updateState(PhiloState leftState,PhiloState rightState);

	protected abstract void runState();

	public abstract int getIntState();

	public boolean getIsRunning() {
		return isRunning;
	}
}
