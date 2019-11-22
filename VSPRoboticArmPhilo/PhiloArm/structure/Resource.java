package structure;

public class Resource {
	private int owner;
	private int workplace;
	private boolean isClean;
	private String direction;

	public Resource(int owner, boolean left) {
		if (left) {
			direction = "left";
		} else {
			direction = "right";
		}
		this.setOwner(owner);
		setClean(false);
		setWorkplace(1);
	}

	public boolean isClean() {
		return isClean;
	}

	public synchronized void setClean(boolean isClean) {
		this.isClean = isClean;
		this.notify();
	}

	public int getOwner() {
		return owner;
	}

	public synchronized void setOwner(int owner) {
		System.out.println(direction + " setOwnerTo: " + owner);
		this.owner = owner;
	}

	public int getWorkplace() {
		return workplace;
	}

	public void setWorkplace(int workplace) {
		System.out.println(direction + " SetWorkplaceTO: " + workplace);
		this.workplace = workplace;
	}
}
