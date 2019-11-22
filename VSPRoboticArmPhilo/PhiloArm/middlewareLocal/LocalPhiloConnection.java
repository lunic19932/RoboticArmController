package middlewareLocal;

public class LocalPhiloConnection {
	
	private MWLocalPhilo leftMW;
	private MWLocalPhilo rightMW;
	
	public LocalPhiloConnection(MWLocalPhilo leftMW,MWLocalPhilo rightMW) {
		this.leftMW=leftMW;
		this.rightMW=rightMW;
	}
	
	public void sendLeft(String functionName, String parameterList) {
		leftMW.functionCallHandler(functionName,parameterList);
	}
	
	public void sendRight(String functionName, String parameterList) {
		rightMW.functionCallHandler(functionName,parameterList);
	}
	
}
