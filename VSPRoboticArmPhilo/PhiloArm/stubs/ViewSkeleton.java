package stubs;
import interfaces.IView;
public class ViewSkeleton{
	private IView view; 

	
	public ViewSkeleton(IView view){
		this.view=view;

	}
	public void setCurrentBackForth(int id, int percentage) {
		 view.setCurrentBackForth(id, percentage);

    }
	public void setCurrentGripper(int id, int percentage) {
		 view.setCurrentGripper(id, percentage);

    }
	public void setCurrentLeftRight(int id, int percentage) {
		 view.setCurrentLeftRight(id, percentage);

    }
	public void setCurrentUpDown(int id, int percentage) {
		 view.setCurrentUpDown(id, percentage);

    }
	public void setCurrentIsHolding(int id, boolean isHolding) {
		 view.setCurrentIsHolding(id, isHolding);

    }

}
