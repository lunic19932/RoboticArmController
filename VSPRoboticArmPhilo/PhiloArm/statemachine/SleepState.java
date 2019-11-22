package statemachine;

import structure.Constants;

public class SleepState extends PhiloState {
	private boolean slept=false;
	public SleepState(PhiloStateMachine statemachine) {
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
			slept=true;
		}
	}

	@Override
	public int getIntState() {
		return Constants.SLEEP;
	}

	@Override
	public String toString() {
		return "Sleep";
	}

	@Override
	public PhiloState updateState(PhiloState leftState, PhiloState rightState) {
		if(slept) {
			return new GetResourceState(statemachine);
		}else if(rightState instanceof WaitLeftState) {
			return new PutRightResourceMidState( this,statemachine);
		}else if(leftState instanceof WaitRightState) {
			return new PutLeftResourceMidState(this, statemachine);
		}
		return this;
	}

}
