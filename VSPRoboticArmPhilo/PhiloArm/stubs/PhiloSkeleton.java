package stubs;
import interfaces.IPhilo;
public class PhiloSkeleton{
	private IPhilo philo; 

	
	public PhiloSkeleton(IPhilo philo){
		this.philo=philo;

	}
	public void changeState(int id, int state) {
		 philo.changeState(id, state);

    }
	public void updateResource(int sender, int workplaceOwner, int workplace, boolean isUsed) {
		 philo.updateResource(sender, workplaceOwner, workplace, isUsed);

    }
	public boolean isLeftResourceAvailable() {
		return philo.isLeftResourceAvailable();

    }
	public boolean isRightResourceAvailable() {
		return philo.isRightResourceAvailable();

    }

}
