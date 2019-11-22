//package PhiloAlgorithm;
//
//import java.util.ArrayList;
//
//import structure.Constants;
//import structure.Resource;
//import stubs.PhiloSkeleton;
//
//public class PhiloController {
//  private int philoId;
//  private ArrayList<Resource> resources;
//  private int leftResourceIndex = 0;
//  private int rightResourceIndex = 1;
//  private int leftPhiloId;
//  private int rightPhiloId;
//  private int[] states;
//  private int oldState;
//  private boolean isRunning;
//  private PhiloSkeleton skeleton;
//  private boolean moveblocked = false;
//  private Thread stateMachineThread;
//  private PhiloController leftPhilo;
//  private PhiloController rightPhilo;
//  private int counter = 0;
//
//  public PhiloController(int philoId) {
//    resources = new ArrayList<Resource>();
//    this.philoId = philoId;
//    neighbourCalculation(Constants.NUMBER_OF_PHILOS);
//    states = new int[3];
//    states[Constants.OWN_PHILO] = Constants.INIT;
//    resourceDistribution();
//  }
//
//  public void setPhilos(PhiloController leftPhilo, PhiloController rightPhilo) {
//    this.leftPhilo = leftPhilo;
//    this.rightPhilo = rightPhilo;
//  }
//
//  public void createAndRunStatemachine() {
//    stateMachineThread = new Thread(new Runnable() {
//      @Override
//      public void run() {
//        stateMachine();
//      }
//    });
//    stateMachineThread.start();
//  }
//
//  private void resourceDistribution() {
//    resources.add(leftResourceIndex, new Resource(philoId));
//    resources.add(rightResourceIndex, new Resource(philoId));
//    if (leftPhiloId < philoId) {
//      resources.set(leftResourceIndex, new Resource(leftPhiloId));
//    }
//    if (rightPhiloId > philoId) {
//      resources.set(rightResourceIndex, new Resource(philoId));
//    }
//  }
//
//  private void neighbourCalculation(int anzPhilos) {
//    leftPhiloId = (philoId + 1) % anzPhilos;
//    if (philoId == 0) {
//      rightPhiloId = anzPhilos - 1;
//    } else {
//      rightPhiloId = philoId - 1;
//    }
//    System.out.println(leftPhiloId + " " + rightPhiloId);
//  }
//
//  private synchronized void stateMachine() {
//    switch (states[Constants.OWN_PHILO]) {
//    case Constants.INIT:
//      tryChangeOwnState(Constants.SLEEP);
//      break;
//
//    case Constants.SLEEP:
//      if (moveblocked) {
//        tryChangeOwnState(Constants.PUT_RESOURCE_MID);
//      }
//      try {
//        Thread.sleep(getRandomSleepTime());
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//
//      tryChangeOwnState(Constants.GET_RESOURCES);
//      break;
//    case Constants.GET_RESOURCES:
//      if (moveblocked) {
//        tryChangeOwnState(Constants.PUT_RESOURCE_MID);
//      }
//      if (!resources.get(leftResourceIndex).isClean() && !resources.get(rightResourceIndex).isClean()) {
//        tryChangeOwnState(Constants.WAIT_LEFT);
//      }
//      break;
//    case Constants.WAIT_LEFT:
//      if (moveblocked) {
//        tryChangeOwnState(Constants.PUT_RESOURCE_MID);
//      }
//      if (states[Constants.LEFT_PHILO] != Constants.PUT_RESOURCE_MID) {
//        tryChangeOwnState(Constants.MOVE_LEFT);
//      }
//      break;
//    case Constants.MOVE_LEFT:
//      if (moveblocked) {
//        tryChangeOwnState(Constants.PUT_RESOURCE_MID);
//      }
//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//      tryChangeOwnState(Constants.WAIT_RIGHT);
//      break;
//
//    case Constants.WAIT_RIGHT:
//      if (moveblocked) {
//        tryChangeOwnState(Constants.PUT_RESOURCE_MID);
//      }
//      if (states[Constants.RIGHT_PHILO] != Constants.PUT_RESOURCE_MID) {
//        tryChangeOwnState(Constants.MOVE_RIGHT);
//      }
//      break;
//    case Constants.MOVE_RIGHT:
//      if (moveblocked) {
//        tryChangeOwnState(Constants.PUT_RESOURCE_MID);
//      }
//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//      tryChangeOwnState(Constants.EAT);
//      break;
//
//    case Constants.PUT_RESOURCE_MID:
//      if (states[Constants.LEFT_PHILO] == Constants.WAIT_RIGHT) {
//        putResourceMiddle(resources.get(leftResourceIndex));
//      } else if (states[Constants.RIGHT_PHILO] == Constants.WAIT_LEFT) {
//        putResourceMiddle(resources.get(rightResourceIndex));
//      }
//
//      tryChangeOwnState(Constants.MOVE_BLOCKED);
//      break;
//
//    case Constants.MOVE_BLOCKED:
//      if (states[Constants.LEFT_PHILO] != Constants.MOVE_RIGHT
//          && states[Constants.RIGHT_PHILO] != Constants.MOVE_LEFT) {
//        tryChangeOwnState(oldState);
//        moveblocked = false;
//      }
//      break;
//
//    case Constants.EAT:
//      try {
//        Thread.sleep(getRandomSleepTime());
//      } catch (InterruptedException e) {
//        e.printStackTrace();
//      }
//      resources.get(leftResourceIndex).setClean(false);
//      resources.get(rightResourceIndex).setClean(false);
//      tryChangeOwnState(Constants.SLEEP);
//      break;
//    }
//  }
//
//  private void tryChangeOwnState(int state) {
//    boolean right = rightPhilo.askForStateTransition(philoId, state);
//    boolean left = leftPhilo.askForStateTransition(philoId, state);
//    // System.out.println("Left: "+left+"Right: "+right);
//    if (left && right) {
//      changeOwnState(state);
//    }
//  }
//
//  private void changeOwnState(int state) {
//     System.out.println("ID: " + philoId + " changed from: " +
//     states[Constants.OWN_PHILO] + " to: " + state);
//    states[Constants.OWN_PHILO] = state;
//    leftPhilo.changeState(philoId, state);
//    rightPhilo.changeState(philoId, state);
//  }
//
//  public boolean askForStateTransition(int philoId, int state) {
//    synchronized (states) {
//      if ((philoId == leftPhiloId && state == Constants.WAIT_RIGHT)
//          || philoId == rightPhiloId && state == Constants.WAIT_LEFT) {
//        if (moveblocked) {
//          return false;
//        } else {
//          moveblocked = true;
//          this.oldState = states[Constants.OWN_PHILO];
//        }
//      }
//      return true;
//    }
//  }
//
//  public void changeState(int id, int state) {
//    if (id == leftPhiloId) {
//      if (states[Constants.LEFT_PHILO] == Constants.EAT && state == Constants.SLEEP) {
//        resources.get(leftResourceIndex).setClean(false);
//      }
//      states[Constants.LEFT_PHILO] = state;
//    } else if (id == rightPhiloId) {
//      if (states[Constants.RIGHT_PHILO] == Constants.EAT && state == Constants.SLEEP) {
//        resources.get(rightResourceIndex).setClean(false);
//      }
//      states[Constants.RIGHT_PHILO] = state;
//    }
//    this.createAndRunStatemachine();
//  }
//
//  private void putResourceMiddle(Resource resource) {
//    synchronized (resources) {
//      if (resource.getOwner() == leftPhiloId) {
//        resources.get(leftResourceIndex).setClean(true);
//        resources.get(leftResourceIndex).setWorkplace(1);
//      } else if (resource.getOwner() == rightPhiloId) {
//        resources.get(rightResourceIndex).setClean(true);
//        resources.get(rightResourceIndex).setWorkplace(1);
//      }
//    }
//  }
//
//  private int getRandomSleepTime() {
//    return (int) (Math.random() * Constants.MAX_SLEEP_TIME_PHILO);
//  }
//
//  public boolean isRunning() {
//    return isRunning;
//  }
//
//  public void setRunning(boolean isRunning) {
//    this.isRunning = isRunning;
//  }
//
//  public PhiloSkeleton getPhiloSkeleton() {
//    return skeleton;
//  }
//
//  public int getCounter() {
//    return counter;
//  }
//
//  public void setCounter(int counter) {
//    this.counter = counter;
//  }
//
//  public int getState() {
//    return states[Constants.OWN_PHILO];
//  }
//}
