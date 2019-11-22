package statemachine;

import application.PhilosophenController;
import structure.Constants;

public class PhiloStateMachine {

	private PhiloState state;
	private PhilosophenController philoController;
	private PhiloState oldState;
	private boolean isRunning = false;
	private PhiloState leftState, rightState;
	private Thread watchDog = new Thread(new Runnable() {
		@Override
		public void run() {
			isRunning = true;
			while (isRunning) {

				runState();
				synchronized (state) {
					updateState(leftState, rightState);
				}
				if (oldState != state) {
					System.out.println("UpdateState TO: " + state);
					philoController.notifyNeighbours();
					oldState = state;
				}
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	});

	public PhiloStateMachine(PhilosophenController philoController) {
		state = new InitState(this);
		oldState = state;
		this.philoController = philoController;
		watchDog.start();
	}

	public PhiloState getState() {
		return state;
	}

	public void setState(PhiloState state) {
		this.state = state;
	}

	public void updateState(PhiloState leftState, PhiloState rightState) {
		synchronized (state) {
			this.leftState = leftState;
			this.rightState = rightState;
			state = state.updateState(leftState, rightState);
		}
	}

	public void runState() {
		state.runState();
	}

	public PhilosophenController getPhiloController() {
		return philoController;
	}

	public void setPhiloController(PhilosophenController philoController) {
		this.philoController = philoController;
	}

	public PhiloState getStateFromInt(int state) {
		switch (state) {
		case Constants.INIT:
			return new InitState(null);
		case Constants.SLEEP:
			return new SleepState(null);
		case Constants.GET_RESOURCES:
			return new GetResourceState(null);
		case Constants.WAIT_LEFT:
			return new WaitLeftState(null);
		case Constants.MOVE_LEFT:
			return new MoveLeftState(null);
		case Constants.WAIT_RIGHT:
			return new WaitRightState(null);
		case Constants.MOVE_RIGHT:
			return new MoveRightState(null);
		case Constants.MOVE_BLOCKED_RIGHT:
			return new MoveBlockedRightState(null, null);
		case Constants.MOVE_BLOCKED_LEFT:
			return new MoveBlockedLeftState(null, null);
		case Constants.EAT:
			return new EatState(null);
		default:
			return null;
		}
	}
}
