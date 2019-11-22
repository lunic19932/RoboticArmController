package stubs;
import interfaces.IMiddleware;
import interfaces.IStub;
import structure.Constants;
import org.json.simple.JSONObject;
public class RacStub implements IStub{
	private IMiddleware middleware; 
	private int targetId; 
	private boolean  getCurrentBackForthReturn=false; 
	private int getCurrentBackForth; 
	private boolean  getCurrentGripperReturn=false; 
	private int getCurrentGripper; 
	private boolean  getCurrentLeftRightReturn=false; 
	private int getCurrentLeftRight; 
	private boolean  getCurrentUpDownReturn=false; 
	private int getCurrentUpDown; 

	
	public RacStub(IMiddleware middleware, int targetId){
		this.middleware=middleware;
		this.targetId=targetId;
		middleware.register(this);

	}
	public void setTargetBackForth(int percentage) {
		JSONObject json=new JSONObject();
		json.put("percentage",percentage);
		middleware.invoke("setTargetBackForth",json.toString(),this);

    }
	public void setTargetGripper(int percentage) {
		JSONObject json=new JSONObject();
		json.put("percentage",percentage);
		middleware.invoke("setTargetGripper",json.toString(),this);

    }
	public void setTargetLeftRight(int percentage) {
		JSONObject json=new JSONObject();
		json.put("percentage",percentage);
		middleware.invoke("setTargetLeftRight",json.toString(),this);

    }
	public void setTargetUpDown(int percentage) {
		JSONObject json=new JSONObject();
		json.put("percentage",percentage);
		middleware.invoke("setTargetUpDown",json.toString(),this);

    }
	public int getCurrentBackForth() {
		synchronized(this){
JSONObject json=new JSONObject();
		middleware.invoke("getCurrentBackForth",json.toString(),this);

		while(!getCurrentBackForthReturn){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getCurrentBackForthReturn=false;
		
		return getCurrentBackForth;
}
    }
	public int getCurrentGripper() {
		synchronized(this){
JSONObject json=new JSONObject();
		middleware.invoke("getCurrentGripper",json.toString(),this);

		while(!getCurrentGripperReturn){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getCurrentGripperReturn=false;
		
		return getCurrentGripper;
}
    }
	public int getCurrentLeftRight() {
		synchronized(this){
JSONObject json=new JSONObject();
		middleware.invoke("getCurrentLeftRight",json.toString(),this);

		while(!getCurrentLeftRightReturn){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getCurrentLeftRightReturn=false;
		
		return getCurrentLeftRight;
}
    }
	public int getCurrentUpDown() {
		synchronized(this){
JSONObject json=new JSONObject();
		middleware.invoke("getCurrentUpDown",json.toString(),this);

		while(!getCurrentUpDownReturn){
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		getCurrentUpDownReturn=false;
		
		return getCurrentUpDown;
}
    }
	public void setLeftWorkplace2(boolean isUsed) {
		JSONObject json=new JSONObject();
		json.put("isUsed",isUsed);
		middleware.invoke("setLeftWorkplace2",json.toString(),this);

    }
	public void setLeftWorkplace1(boolean isUsed) {
		JSONObject json=new JSONObject();
		json.put("isUsed",isUsed);
		middleware.invoke("setLeftWorkplace1",json.toString(),this);

    }
	public void setRightWorkplace0(boolean isUsed) {
		JSONObject json=new JSONObject();
		json.put("isUsed",isUsed);
		middleware.invoke("setRightWorkplace0",json.toString(),this);

    }
	public void setRightWorkplace1(boolean isUsed) {
		JSONObject json=new JSONObject();
		json.put("isUsed",isUsed);
		middleware.invoke("setRightWorkplace1",json.toString(),this);

    }
	public void setMidWorkplace1(boolean isUsed) {
		JSONObject json=new JSONObject();
		json.put("isUsed",isUsed);
		middleware.invoke("setMidWorkplace1",json.toString(),this);

    }
	public void setOwnWorkplace1(boolean isUsed) {
		JSONObject json=new JSONObject();
		json.put("isUsed",isUsed);
		middleware.invoke("setOwnWorkplace1",json.toString(),this);

    }
	public boolean isConnected() {
		return middleware.isConnected(this);

    }
	public void notifyReturn(JSONObject json) {
		switch((String)json.get(Constants.FUNCTIONNAME)){
			case "getCurrentBackForth":
					getCurrentBackForth=Integer.parseInt((String)json.get(Constants.RETURNVALUE));
					getCurrentBackForthReturn=true;
			break;
			case "getCurrentGripper":
					getCurrentGripper=Integer.parseInt((String)json.get(Constants.RETURNVALUE));
					getCurrentGripperReturn=true;
			break;
			case "getCurrentLeftRight":
					getCurrentLeftRight=Integer.parseInt((String)json.get(Constants.RETURNVALUE));
					getCurrentLeftRightReturn=true;
			break;
			case "getCurrentUpDown":
					getCurrentUpDown=Integer.parseInt((String)json.get(Constants.RETURNVALUE));
					getCurrentUpDownReturn=true;
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
