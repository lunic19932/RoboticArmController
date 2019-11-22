package stubs;
import interfaces.IMiddleware;
import interfaces.IStub;
import structure.Constants;
import org.json.simple.JSONObject;
public class ViewStub implements IStub{
	private IMiddleware middleware; 
	private int targetId; 

	
	public ViewStub(IMiddleware middleware, int targetId){
		this.middleware=middleware;
		this.targetId=targetId;
		middleware.register(this);

	}
	public void setCurrentBackForth(int id, int percentage) {
		JSONObject json=new JSONObject();
		json.put("id",id);
		json.put("percentage",percentage);
		middleware.invoke("setCurrentBackForth",json.toString(),this);

    }
	public void setCurrentGripper(int id, int percentage) {
		JSONObject json=new JSONObject();
		json.put("id",id);
		json.put("percentage",percentage);
		middleware.invoke("setCurrentGripper",json.toString(),this);

    }
	public void setCurrentLeftRight(int id, int percentage) {
		JSONObject json=new JSONObject();
		json.put("id",id);
		json.put("percentage",percentage);
		middleware.invoke("setCurrentLeftRight",json.toString(),this);

    }
	public void setCurrentUpDown(int id, int percentage) {
		JSONObject json=new JSONObject();
		json.put("id",id);
		json.put("percentage",percentage);
		middleware.invoke("setCurrentUpDown",json.toString(),this);

    }
	public void setCurrentIsHolding(int id, boolean isHolding) {
		JSONObject json=new JSONObject();
		json.put("id",id);
		json.put("isHolding",isHolding);
		middleware.invoke("setCurrentIsHolding",json.toString(),this);

    }
	public boolean isConnected() {
		return middleware.isConnected(this);

    }
	public void notifyReturn(JSONObject json) {
		switch((String)json.get(Constants.FUNCTIONNAME)){

		}
synchronized(this){
		notifyAll();
}

    }
	public int getTargetId() {
		return targetId;
    }

}
