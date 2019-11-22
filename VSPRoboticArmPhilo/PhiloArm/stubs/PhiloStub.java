package stubs;
import interfaces.IMiddleware;
import interfaces.IStub;
import structure.Constants;
import org.json.simple.JSONObject;
public class PhiloStub implements IStub{
	private IMiddleware middleware; 
	private int targetId; 
	private boolean  isLeftResourceAvailableReturn=false; 
	private boolean isLeftResourceAvailable; 
	private boolean  isRightResourceAvailableReturn=false; 
	private boolean isRightResourceAvailable; 

	
	public PhiloStub(IMiddleware middleware, int targetId){
		this.middleware=middleware;
		this.targetId=targetId;
		middleware.register(this);

	}
	public void changeState(int id, int state) {
		JSONObject json=new JSONObject();
		json.put("id",id);
		json.put("state",state);
		middleware.invoke("changeState",json.toString(),this);

    }
	public void updateResource(int sender, int workplaceOwner, int workplace, boolean isUsed) {
		JSONObject json=new JSONObject();
		json.put("sender",sender);
		json.put("workplaceOwner",workplaceOwner);
		json.put("workplace",workplace);
		json.put("isUsed",isUsed);
		middleware.invoke("updateResource",json.toString(),this);

    }
	public boolean isLeftResourceAvailable() {
		synchronized(this){
JSONObject json=new JSONObject();
		middleware.invoke("isLeftResourceAvailable",json.toString(),this);

		while(!isLeftResourceAvailableReturn){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isLeftResourceAvailableReturn=false;
		
		return isLeftResourceAvailable;
}
    }
	public boolean isRightResourceAvailable() {
		synchronized(this){
JSONObject json=new JSONObject();
		middleware.invoke("isRightResourceAvailable",json.toString(),this);

		while(!isRightResourceAvailableReturn){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		isRightResourceAvailableReturn=false;
		
		return isRightResourceAvailable;
}
    }
	public boolean isConnected() {
		return middleware.isConnected(this);

    }
	public void notifyReturn(JSONObject json) {
		switch((String)json.get(Constants.FUNCTIONNAME)){
			case "isLeftResourceAvailable":
					isLeftResourceAvailable=(boolean)json.get(Constants.RETURNVALUE);
					isLeftResourceAvailableReturn=true;
			break;
			case "isRightResourceAvailable":
					isRightResourceAvailable=(boolean)json.get(Constants.RETURNVALUE);
					isRightResourceAvailableReturn=true;
			break;

		}
synchronized(this){
		notifyAll();
}

    }
	public int getTargetId() {
		return targetId;
    }

}
