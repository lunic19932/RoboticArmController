package interfaces;
	
public interface IPhilo{
	public  void changeState(int id, int state);
	public  void updateResource(int sender, int workplaceOwner, int workplace, boolean isUsed);
	public  boolean isLeftResourceAvailable();
	public  boolean isRightResourceAvailable();

}
