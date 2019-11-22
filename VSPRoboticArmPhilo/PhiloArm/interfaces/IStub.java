package interfaces;
import org.json.simple.JSONObject;	
public interface IStub{
	public  boolean isConnected();
	public  int getTargetId();
	public  void notifyReturn(JSONObject json);

}
