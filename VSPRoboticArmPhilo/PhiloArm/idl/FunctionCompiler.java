package idl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import staticMethods.StaticMethods;

public class FunctionCompiler {
	private String methodeDeclarationPlain;
	private String methodePlain;
	private String setTargetBody;
	private String invokeFunctionCall;
	private String constructorBody;
	private String constructorMethodePlain;
	private String invokeAddJson;
	private String isConnected;
	private String waitPlant;
	private String casePlant;
	private String switchPlant;
	public FunctionCompiler() throws FileNotFoundException, IOException {
		methodeDeclarationPlain = StaticMethods.readEntirefile("resources/Idl_Plants/methode_Declaration_plain.txt");
		methodePlain = StaticMethods.readEntirefile("resources/Idl_Plants/methode_plain.txt");
		setTargetBody = StaticMethods.readEntirefile("resources/Idl_Plants/setTargetBody_plain.txt");
		invokeFunctionCall = StaticMethods.readEntirefile("resources/Idl_Plants/invokeBody_plain.txt");
		constructorBody = StaticMethods.readEntirefile("resources/Idl_Plants/constructorBody.txt");
		constructorMethodePlain = StaticMethods.readEntirefile("resources/Idl_Plants/constructorMethod_plain.txt");
		invokeAddJson = StaticMethods.readEntirefile("resources/Idl_Plants/invokeAddJsonPlain.txt");
		isConnected =StaticMethods.readEntirefile("resources/Idl_Plants/isConnectedMethod_Plant.txt");
		waitPlant =StaticMethods.readEntirefile("resources/Idl_Plants/waitPlant.txt");
		casePlant =StaticMethods.readEntirefile("resources/Idl_Plants/casePlant.txt");
		switchPlant =StaticMethods.readEntirefile("resources/Idl_Plants/switchPlant.txt");
	}

	public String createMethodeDeclaration(String returnTyp, String functionName,
			Map<Integer, Map<String, String>> parameterList) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(String.format(methodeDeclarationPlain, returnTyp, functionName,
				getParameterNameList(parameterList, true)));
		return buffer.toString();
	}

	public String createSetTargetMethode(String returnTyp, String functionName,
			Map<Integer, Map<String, String>> parameterList, String sName) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer bufferBody = new StringBuffer();
		if (returnTyp.equals("void")) {
			bufferBody.append(
					String.format(setTargetBody, "", sName, functionName, getParameterNameList(parameterList, false)));
		} else {
			bufferBody.append(String.format(setTargetBody, "return", sName, functionName,
					getParameterNameList(parameterList, false)));
		}

		buffer.append(String.format(methodePlain, returnTyp, functionName, getParameterNameList(parameterList, true),
				bufferBody.toString()));
		return buffer.toString();
	}
	public String createGetFunction(String returnTyp,String variableName) {
		StringBuffer buffer=new StringBuffer();
		String methodName="get"+(""+variableName.charAt(0)).toUpperCase()+variableName.substring(1);
		buffer.append(String.format(methodePlain, returnTyp,methodName,"","return "+variableName+";"));
		return  buffer.toString();
	}
	public String createInvokeFunction(String returnTyp, String functionName,
			Map<Integer, Map<String, String>> parameterlist, String mwName) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer bufferBody = new StringBuffer();
		String nameJson = "json";
		String functionNameReturn=functionName+"Return";
		if(!returnTyp.equals("void")) {
			bufferBody.append("synchronized(this){\n");
		}
		
		bufferBody.append("JSONObject " + nameJson + "=new JSONObject();\n");
		int i = 1;
		Map<String, String> parameter = parameterlist.get(i);

		while (parameter != null) {
			if (getParameterType(parameter).equals("String")) {
				bufferBody.append(String.format(invokeAddJson, nameJson, getParameterName(parameter),
						getParameterName(parameter) + ".trim()"));
			} else {
				bufferBody.append(String.format(invokeAddJson, nameJson, getParameterName(parameter),
						getParameterName(parameter)));
			}
			parameter = parameterlist.get(++i);
		} 
		bufferBody.append(String.format(invokeFunctionCall, mwName, functionName, nameJson,"this"));

		if(!returnTyp.equals("void")) {
			bufferBody.append(String.format(waitPlant,functionNameReturn,functionNameReturn));
			bufferBody.append("		return "+ functionName+";\n");
			bufferBody.append("}");
			buffer.append(String.format(methodePlain,returnTyp, functionName, getParameterNameList(parameterlist, true),
					bufferBody.toString()));
		}else {
			buffer.append(String.format(methodePlain, returnTyp, functionName, getParameterNameList(parameterlist, true),
					bufferBody.toString()));	
		}
		
		

		
		return buffer.toString();
	}

	public String createConstructor(String functionName, ArrayList<String> parameterList, ArrayList<String> parameterNameList,String otherValue) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer bufferBody = new StringBuffer();
		StringBuffer parameterBuffer=new StringBuffer();
		int i=0;
		for(String parameter:parameterList) {
			parameterBuffer.append(parameter);
			if(i%2==0 && i!=(parameterList.size()-1)){
				parameterBuffer.append(", ");
			}
			i++;
		}
		for (String parameter : parameterNameList) {
			bufferBody.append(String.format(constructorBody, parameter, parameter));
		}
		bufferBody.append(otherValue);
		buffer.append(String.format(constructorMethodePlain, functionName, parameterBuffer.toString(), bufferBody.toString()));
		return buffer.toString();
	}

	private String getParameterNameList(Map<Integer, Map<String, String>> parameterList, boolean withType) {

		StringBuffer nameBuffer = new StringBuffer();
		Map<String, String> parameter;
		int i = 1;
		do {
			// Map<Integer, Map<String, String>> m=methodeParameterMap.get(methode);
			parameter = parameterList.get(i);
			if (parameter == null) {
				break;
			}
			if (withType) {
				nameBuffer.append(getParameterTypAndName(parameter));
			} else {
				nameBuffer.append(getParameterName(parameter));

			}
			if (parameterList.get(++i) != null) {
				nameBuffer.append(", ");
			}
		} while (true);
		return nameBuffer.toString();
	}

	private String getParameterType(Map<String, String> parameter) {
		return parameter.get("type");
	}

	private String getParameterName(Map<String, String> parameter) {
		return parameter.get("name");
	}

	private String getParameterTypAndName(Map<String, String> parameter) {

		return parameter.get("type") + " " + parameter.get("name");
	}


	public String createIsConnectedMethode(String mwName) {
		StringBuffer buffer = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		buffer2.append(String.format(isConnected, mwName,"isConnected"));
		buffer.append(String.format(methodePlain, "boolean","isConnected","",buffer2.toString()));
		return buffer.toString();
	}

	public String createNotifyFunction(ArrayList<String> methodeNames, Map<String, String> methodeReturnMap) {
		StringBuffer buffer=new StringBuffer();
		StringBuffer caseBuffer=new StringBuffer();
		StringBuffer switchBuffer=new StringBuffer();
		for (String method:methodeNames) {
			switch(methodeReturnMap.get(method)) {
			case "int" :
				caseBuffer.append(String.format(casePlant, method,"		"+method+"=Integer.parseInt((String)json.get(Constants.RETURNVALUE));\n					"+method+"Return=true;"));
				break;
			case "String" :
				caseBuffer.append(String.format(casePlant, method,"		"+method+"=(String)json.get(Constants.RETURNVALUE));\n									"+method+"Return=true;"));
				break;
			case "boolean" :
				caseBuffer.append(String.format(casePlant, method,"		"+method+"=(boolean)json.get(Constants.RETURNVALUE);\n					"+method+"Return=true;"));
				break;
			}
			
		}
		switchBuffer.append(String.format(switchPlant, "(String)json.get(Constants.FUNCTIONNAME)",caseBuffer.toString()));
		switchBuffer.append("synchronized(this){\n");
		switchBuffer.append("		notifyAll();\n");
		switchBuffer.append("}\n");
		buffer.append(String.format(methodePlain,"void", "notifyReturn", "JSONObject json",
				switchBuffer.toString()));
		

		return buffer.toString();
	}
}
