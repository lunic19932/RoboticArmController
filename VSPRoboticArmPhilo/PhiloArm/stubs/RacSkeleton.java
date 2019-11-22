package stubs;
import interfaces.IRac;
public class RacSkeleton{
	private IRac rac; 

	
	public RacSkeleton(IRac rac){
		this.rac=rac;

	}
	public void setTargetBackForth(int percentage) {
		 rac.setTargetBackForth(percentage);

    }
	public void setTargetGripper(int percentage) {
		 rac.setTargetGripper(percentage);

    }
	public void setTargetLeftRight(int percentage) {
		 rac.setTargetLeftRight(percentage);

    }
	public void setTargetUpDown(int percentage) {
		 rac.setTargetUpDown(percentage);

    }
	public int getCurrentBackForth() {
		return rac.getCurrentBackForth();

    }
	public int getCurrentGripper() {
		return rac.getCurrentGripper();

    }
	public int getCurrentLeftRight() {
		return rac.getCurrentLeftRight();

    }
	public int getCurrentUpDown() {
		return rac.getCurrentUpDown();

    }
	public void setLeftWorkplace2(boolean isUsed) {
		 rac.setLeftWorkplace2(isUsed);

    }
	public void setLeftWorkplace1(boolean isUsed) {
		 rac.setLeftWorkplace1(isUsed);

    }
	public void setRightWorkplace0(boolean isUsed) {
		 rac.setRightWorkplace0(isUsed);

    }
	public void setRightWorkplace1(boolean isUsed) {
		 rac.setRightWorkplace1(isUsed);

    }
	public void setMidWorkplace1(boolean isUsed) {
		 rac.setMidWorkplace1(isUsed);

    }
	public void setOwnWorkplace1(boolean isUsed) {
		 rac.setOwnWorkplace1(isUsed);

    }

}
