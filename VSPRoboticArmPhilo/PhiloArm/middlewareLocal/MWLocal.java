package middlewareLocal;

import interfaces.IMiddleware;

public abstract class MWLocal implements IMiddleware {
	
	public abstract void functionCallHandler(String functionName, String parameterList);
	

}
