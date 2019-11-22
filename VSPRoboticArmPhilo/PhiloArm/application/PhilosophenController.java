package application;

import java.net.SocketException;
import java.util.LinkedList;

import interfaces.IMWPhilo;
import interfaces.IMiddleware;
import interfaces.IPhilo;
import middlewareNetwork.MWNetworkPhilo;
import statemachine.EatState;
import statemachine.InitState;
import statemachine.MoveBlockedLeftState;
import statemachine.MoveBlockedRightState;
import statemachine.MoveLeftState;
import statemachine.MoveRightState;
import statemachine.PhiloState;
import statemachine.PhiloStateMachine;
import statemachine.PutLeftResourceMidState;
import statemachine.PutRightResourceMidState;
import statemachine.WaitLeftState;
import statemachine.WaitRightState;
import structure.Constants;
import structure.Resource;
import stubs.PhiloSkeleton;
import stubs.PhiloStub;
import stubs.ViewSkeleton;

public class PhilosophenController implements IPhilo {
	private int philoId;

	private int leftPhiloId;
	private int rightPhiloId;
	private ResourceMovement resourceMovement;
	private IMWPhilo mw;
	private boolean local;
	private PhiloSkeleton skeleton;
	private PhiloStub leftPhiloStub;
	private PhiloStub rightPhiloStub;
	private Resource leftResource;
	private Resource rightResource;
	private PhiloState leftState;
	private PhiloState rightState;
	private PhiloStateMachine statemachine;
	private int transNumber = 0;

	private LinkedList<Integer> ackState = new LinkedList<Integer>();

	public PhilosophenController(int philoId, boolean local) {
		this.philoId = philoId;
		neighbourCalculation(Constants.NUMBER_OF_PHILOS);
		createMWAndStubs();
		resourceDistribution();
		resourceMovement = new ResourceMovement(philoId, this);
		statemachine = new PhiloStateMachine(this);

	}

	private void createMWAndStubs() {
		skeleton = new PhiloSkeleton(this);
		if (!local) {
			try {
				mw = new MWNetworkPhilo(philoId, skeleton);
				leftPhiloStub = new PhiloStub((IMiddleware) mw, leftPhiloId);
				rightPhiloStub = new PhiloStub((IMiddleware) mw, rightPhiloId);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		} else {
			// mw = new MWLocalPhilo(philoId);
		}

	}

	public void resourceDistribution() {
		leftResource = new Resource(leftPhiloId, true);
		rightResource = new Resource(philoId, false);
	}

	private void neighbourCalculation(int anzPhilos) {
		leftPhiloId = (philoId + 1) % anzPhilos;
		if (philoId == 0) {
			rightPhiloId = anzPhilos - 1;
		} else {
			rightPhiloId = philoId - 1;
		}
	}

	public boolean isConnected() {
		return leftPhiloStub.isConnected() && rightPhiloStub.isConnected() && resourceMovement.getStub().isConnected();
	}

	@Override
	public synchronized void changeState(int id, int stateInt) {
//		System.out.println("philoId:"+id+"Incomming State: "+stateInt);

		PhiloState state = statemachine.getStateFromInt(stateInt);
		if (id == leftPhiloId) {
			leftState = state;
		} else if (id == rightPhiloId) {
			rightState = state;
		}
		statemachine.updateState(leftState, rightState);

	}

//	public void pickedUpResource() {
//		switch (statemachine.getState().getIntState()) {
//		case Constants.MOVE_LEFT:
//			leftPhiloStub.updateResource(philoId, leftPhiloId,1,false);
//			break;
//		case Constants.MOVE_RIGHT:
//			leftPhiloStub.updateResource(philoId, rightPhiloId, 1,false);
//			break;
//		case Constants.PUT_LEFT_RESOURCE_MID:
//			leftPhiloStub.updateResource(philoId, philoId, 0,false);
//			break;
//		case Constants.PUT_RIGHT_RESOURCE_MID:
//			leftPhiloStub.updateResource(philoId, philoId, 2,false);
//			break;
//
//		}
//
//	}
//
//	public void putDownResource() {
//		switch (statemachine.getState().getIntState()) {
//		case Constants.MOVE_LEFT:
//			leftPhiloStub.updateResource(philoId, philoId, 0,true);
//			break;
//		case Constants.MOVE_RIGHT:
//			leftPhiloStub.updateResource(philoId, philoId, 2,true);
//			break;
//		case Constants.PUT_LEFT_RESOURCE_MID:
//			leftPhiloStub.updateResource(philoId, philoId, 1,true);
//			break;
//		case Constants.PUT_RIGHT_RESOURCE_MID:
//			leftPhiloStub.updateResource(philoId, philoId, 1,true);
//			break;
//		}
//	}
	public void pickedUpResource() {
		switch (statemachine.getState().getIntState()) {
		case Constants.PUT_LEFT_RESOURCE_MID:
			leftPhiloStub.updateResource(philoId, leftResource.getOwner(), leftResource.getWorkplace(), false);
			leftResource.setWorkplace(1);
			leftResource.setOwner(philoId);
			break;
		case Constants.MOVE_LEFT:
			leftPhiloStub.updateResource(philoId, leftResource.getOwner(), leftResource.getWorkplace(), false);
			leftResource.setWorkplace(0);
			leftResource.setOwner(philoId);
			break;
		case Constants.PUT_RIGHT_RESOURCE_MID:
			leftPhiloStub.updateResource(philoId, rightResource.getOwner(), rightResource.getWorkplace(), false);
			rightResource.setWorkplace(1);
			rightResource.setOwner(philoId);
			break;
		case Constants.MOVE_RIGHT:
			leftPhiloStub.updateResource(philoId, rightResource.getOwner(), rightResource.getWorkplace(), false);
			rightResource.setWorkplace(2);
			rightResource.setOwner(philoId);
			break;
		}
	}

	public void putDownResource() {
		switch (statemachine.getState().getIntState()) {
		case Constants.PUT_LEFT_RESOURCE_MID:
			leftPhiloStub.updateResource(philoId, leftResource.getOwner(), leftResource.getWorkplace(), true);
			leftResource.setOwner(philoId);
			leftResource.setWorkplace(1);
			break;
		case Constants.MOVE_LEFT:
			leftPhiloStub.updateResource(philoId, leftResource.getOwner(), leftResource.getWorkplace(), true);
			leftResource.setOwner(philoId);
			leftResource.setWorkplace(0);
			break;
		case Constants.PUT_RIGHT_RESOURCE_MID:
			leftPhiloStub.updateResource(philoId, rightResource.getOwner(), rightResource.getWorkplace(), true);
			rightResource.setOwner(philoId);
			rightResource.setWorkplace(1);
			break;
		case Constants.MOVE_RIGHT:
			leftPhiloStub.updateResource(philoId, rightResource.getOwner(), rightResource.getWorkplace(), true);
			rightResource.setOwner(philoId);
			rightResource.setWorkplace(2);
			break;
		}
	}

	public void updateResource(int sender, int workplaceOwner, int workplace, boolean isUsed) {
		if (sender != philoId) {
			Thread updateThread = new Thread(new Runnable() {
				@Override
				public void run() {
					leftPhiloStub.updateResource(sender, workplaceOwner, workplace, isUsed);
				}
			});
			setResource(sender, workplaceOwner, workplace, isUsed);
			updateThread.start();
		}
	}

	private void setResource(int sender, int workplaceOwner, int workplace, boolean isUsed) {
		System.out.println("Sender: " + sender + " WorkplaceOwner: " + workplaceOwner + " workplace: " + workplace
				+ " isUsed: " + isUsed);

		if (workplaceOwner == philoId) {
			if (sender == leftPhiloId && !isUsed) {
				leftResource.setOwner(sender);
				leftResource.setWorkplace(2);
			} else if (sender == leftPhiloId && isUsed) {
				leftResource.setOwner(philoId);
				leftResource.setWorkplace(1);
			} else if (sender == rightPhiloId && !isUsed) {
				rightResource.setOwner(sender);
				rightResource.setWorkplace(0);
			} else if (sender == rightPhiloId && isUsed) {
				rightResource.setOwner(philoId);
				rightResource.setWorkplace(1);
			}
			resourceMovement.setOwnWorkplace(isUsed);
		} else if (workplaceOwner == leftPhiloId) {
			setLeftResourcePosition(sender, workplace, isUsed);
		} else if (workplaceOwner == rightPhiloId) {
			setRightResourcePosition(sender, workplace, isUsed);
		} else if (workplace == 1) {
			resourceMovement.setMidWorkplace(isUsed);
		}
	}

	private void setRightResourcePosition(int sender, int workplace, boolean isUsed) {
		resourceMovement.setRightWorkplace(workplace, isUsed);
		if (workplace == 1 && !isUsed) {
			if (sender == rightPhiloId) {
				rightResource.setOwner(sender);
				rightResource.setWorkplace(0);
			}
			resourceMovement.setRightWorkplace(1, isUsed);
		} else if (workplace == 1 && isUsed) {
			if (sender == rightPhiloId) {
				rightResource.setOwner(sender);
				rightResource.setWorkplace(1);
			}
			resourceMovement.setRightWorkplace(1, isUsed);
		} else if (workplace == 0 && !isUsed) {
			if (sender == rightPhiloId) {
				rightResource.setWorkplace(1);
				rightResource.setOwner(sender);
			}
			resourceMovement.setRightWorkplace(0, isUsed);
		} else if (workplace == 0 && isUsed) {
			if (sender == rightPhiloId) {
				rightResource.setWorkplace(0);
				rightResource.setOwner(sender);
			}
			resourceMovement.setRightWorkplace(0, isUsed);
		}
	}

	private void setLeftResourcePosition(int sender, int workplace, boolean isUsed) {
		if (workplace == 1 && !isUsed) {
			if (sender == leftPhiloId) {
				leftResource.setOwner(sender);
				leftResource.setWorkplace(2);
			}
			resourceMovement.setLeftWorkplace(1, isUsed);
		} else if (workplace == 1 && isUsed) {
			if (sender == leftPhiloId) {
				leftResource.setOwner(sender);
				leftResource.setWorkplace(1);
			}
			resourceMovement.setLeftWorkplace(1, isUsed);
		} else if (workplace == 2 && !isUsed) {
			if (sender == leftPhiloId) {
				leftResource.setWorkplace(1);
				leftResource.setOwner(sender);
			}
			resourceMovement.setLeftWorkplace(2, isUsed);
		} else if (workplace == 2 && isUsed) {
			if (sender == leftPhiloId) {
				leftResource.setWorkplace(2);
				leftResource.setOwner(sender);
			}
			resourceMovement.setLeftWorkplace(2, isUsed);
		}
	}

//	private void setResourceAttributes(Resource resource, int owner, int workplace) {
//		resource.setOwner(owner);
//		resource.setWorkplace(workplace);
//	}

	public PhiloSkeleton getPhiloSkeleton() {
		return skeleton;
	}

	public ViewSkeleton getViewSkeleton() {
		return resourceMovement.getSkeleton();
	}

	public Resource getLeftResource() {
		return leftResource;
	}

	public void setLeftResource(Resource leftResource) {
		this.leftResource = leftResource;
	}

	public Resource getRightResource() {
		return rightResource;
	}

	public void setRightResource(Resource rightResource) {
		this.rightResource = rightResource;
	}

	public boolean areResourcesAvailable() {
		boolean leftAvailable = leftPhiloStub.isRightResourceAvailable();
		boolean rightAvailable = rightPhiloStub.isLeftResourceAvailable();
		if (leftAvailable) {
			leftResource.setClean(true);
		}
		if (rightAvailable) {
			rightResource.setClean(true);
		}
		return leftAvailable && rightAvailable;
	}

	public boolean isMoveBlocked() {

		if (statemachine.getState() instanceof MoveBlockedRightState) {

			return leftState instanceof MoveRightState;
		} else if (statemachine.getState() instanceof MoveBlockedRightState) {
			return rightState instanceof MoveLeftState;
		}
		return (leftState instanceof MoveRightState || rightState instanceof MoveLeftState);
	}

	public void moveResourceToLeft() {
		if (leftResource.getOwner() != philoId) {
			resourceMovement.moveResourceToLeft();
		} else if (leftResource.getWorkplace() == 1) {
			resourceMovement.moveMiddleToLeft();
		}

	}

	public void moveResourceToRight() {
		if (rightResource.getOwner() != philoId) {
			resourceMovement.moveResourceToRight();
		} else if (rightResource.getWorkplace() == 1) {
			resourceMovement.moveMiddleToRight();
		}
	}

	public void putLeftResourceMid() {
		if (leftResource.getOwner() == philoId && leftResource.getWorkplace() != 1) {
			resourceMovement.moveLeftToMiddle();
		}
	}

	public void putRightResourceMid() {
		if (rightResource.getOwner() == philoId && rightResource.getWorkplace() != 1) {
			resourceMovement.moveRightToMiddle();
		}
	}

	public void eaten() {
		leftResource.setClean(false);
		rightResource.setClean(false);
	}

	public void cleanLeftResource() {
		leftResource.setClean(true);

	}

	public void cleanRightResource() {
		rightResource.setClean(true);
	}

	public void notifyNeighbours() {
		leftPhiloStub.changeState(philoId, statemachine.getState().getIntState());
		rightPhiloStub.changeState(philoId, statemachine.getState().getIntState());
	}

	public PhiloState getLeftState() {
		return leftState;
	}

	public PhiloState getRightState() {
		return rightState;
	}

	@Override
	public boolean isLeftResourceAvailable() {
		return !leftResource.isClean();// ||checkStateForAvailableResource();
	}

	private boolean checkStateForAvailableResource() {
		boolean availableState = (statemachine.getState() instanceof MoveBlockedRightState);
		availableState |= (statemachine.getState() instanceof MoveBlockedLeftState);
		availableState |= (statemachine.getState() instanceof PutLeftResourceMidState);
		availableState |= (statemachine.getState() instanceof PutRightResourceMidState);
		return availableState;
	}

	@Override
	public boolean isRightResourceAvailable() {
		return !rightResource.isClean();// ||checkStateForAvailableResource();
	}

	public int getTransNummer() {
		return transNumber++;
	}

}
