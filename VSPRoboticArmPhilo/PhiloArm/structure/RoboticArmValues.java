/**
 * Distributed Systems
 * Robotic Arm Controller
 * 01.05.2019
 * Luis Nickel & Leo Peters
 */

package structure;

public class RoboticArmValues {
	private int currentGripper = 50;
	private int currentBackForth = 50;
	private int currentLeftRight = 50;
	private int currentUpDown = 50;

	public int getCurrentGripper() {
		return currentGripper;
	}

	public void setCurrentGripper(int currentGripper) {
		this.currentGripper = currentGripper;
	}

	public int getCurrentBackForth() {
		return currentBackForth;
	}

	public void setCurrentBackForth(int currentBackForth) {
		this.currentBackForth = currentBackForth;
	}

	public int getCurrentLeftRight() {
		return currentLeftRight;
	}

	public void setCurrentLeftRight(int currentLeftRight) {
		this.currentLeftRight = currentLeftRight;
	}

	public int getCurrentUpDown() {
		return currentUpDown;
	}

	public void setCurrentUpDown(int currentUpDown) {
		this.currentUpDown = currentUpDown;
	}
}
