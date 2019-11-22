package statemachine;

import structure.Constants;

public class EatState extends PhiloState {
	private boolean ate=false;
	
	public EatState(PhiloStateMachine statemachine) {
		super(statemachine);
		
	}
	@Override
	public void runState() {
		if(!isRunning) {
			isRunning=true;
			try {
				Thread.sleep((int) (Math.random() * Constants.MAX_SLEEP_TIME_PHILO));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			statemachine.getPhiloController().eaten();
			isRunning=false;
			ate=true;
		}
	}
	@Override
	public int getIntState() {
		return Constants.EAT;
	}


	@Override
	public String toString() {
		return "Eat";
	}
	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(ate) {
			return new SleepState(statemachine);
		}
		return this;
	}
}
