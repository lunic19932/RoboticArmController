package interfaces;
	
public interface IRac{
	public  void setTargetBackForth(int percentage);
	public  void setTargetGripper(int percentage);
	public  void setTargetLeftRight(int percentage);
	public  void setTargetUpDown(int percentage);
	public  int getCurrentBackForth();
	public  int getCurrentGripper();
	public  int getCurrentLeftRight();
	public  int getCurrentUpDown();
	public  void setLeftWorkplace2(boolean isUsed);
	public  void setLeftWorkplace1(boolean isUsed);
	public  void setRightWorkplace0(boolean isUsed);
	public  void setRightWorkplace1(boolean isUsed);
	public  void setMidWorkplace1(boolean isUsed);
	public  void setOwnWorkplace1(boolean isUsed);

}
