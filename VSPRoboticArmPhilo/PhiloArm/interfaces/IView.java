package interfaces;
	
public interface IView{
	public  void setCurrentBackForth(int id, int percentage);
	public  void setCurrentGripper(int id, int percentage);
	public  void setCurrentLeftRight(int id, int percentage);
	public  void setCurrentUpDown(int id, int percentage);
	public  void setCurrentIsHolding(int id, boolean isHolding);

}
