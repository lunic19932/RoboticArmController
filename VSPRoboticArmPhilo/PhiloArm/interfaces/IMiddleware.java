package interfaces;
	
public interface IMiddleware{
	public  void invoke(String functionName, String parameterList, IStub stub);
	public  boolean isConnected(IStub stub);
	public  void register(IStub stub);

}
