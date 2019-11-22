/**
 * Distributed Systems
 * Robotic Arm Controller
 * 01.05.2019
 * Luis Nickel & Leo Peters
 */

package idl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.parser.ParseException;

import staticMethods.StaticMethods;
import structure.InterfaceStruct;

public class IdlCompiler {
	private FunctionCompiler fCompiler;
	private ArrayList<String> methodeNames;
	private String objectName;
	private Map<String, String> methodeReturnMap;
	private Map<String, Map<Integer, Map<String, String>>> methodeParameterMap;
	private String mwName;
	private String interfaceFile;
	private String mwinterfaceFile;
	private String registPlant;

	public IdlCompiler(String interfaceFile) throws FileNotFoundException, IOException, ParseException {
		this.interfaceFile = interfaceFile;
		mwinterfaceFile = "resources/Idl_Interfaces/MiddlewareInterface.json";
		fCompiler = new FunctionCompiler();
		mwName = "middleware";
		registPlant =StaticMethods.readEntirefile("resources/Idl_Plants/constuctorStubRegister.txt");
	}

	private void loadInterface(String filename) throws FileNotFoundException, IOException, ParseException {
		InterfaceStruct iStruct = new InterfaceStruct(filename);
		methodeNames = iStruct.getMethodeNames();
		objectName = iStruct.getObjectName();
		methodeReturnMap = iStruct.getMethodeReturnMap();
		methodeParameterMap = iStruct.getMethodeParameterMap();
	}

	public void createJavaMiddlewareInterface(String targetFolder, String packageName)
			throws FileNotFoundException, IOException, ParseException {
		loadInterface(mwinterfaceFile);
		String interfacePlain = StaticMethods.readEntirefile("resources/Idl_Plants/InterfacePlain.txt");
		StringBuffer bodyBuffer = new StringBuffer();

		for (String methode : methodeNames) {
			bodyBuffer.append(fCompiler.createMethodeDeclaration(methodeReturnMap.get(methode), methode,
					methodeParameterMap.get(methode)));
		}
		String string = String.format(interfacePlain, packageName,"", objectName, bodyBuffer.toString());
		StaticMethods.writeToFile(targetFolder, "I", string, objectName);
	}

	public void createJavaServerInterface(String targetFolder, String packageName,String imports)
			throws FileNotFoundException, IOException, ParseException {
		loadInterface(interfaceFile);
		String interfacePlain = StaticMethods.readEntirefile("resources/Idl_Plants/InterfacePlain.txt");
		StringBuffer bodyBuffer = new StringBuffer();
		for (String methode : methodeNames) {
			bodyBuffer.append(fCompiler.createMethodeDeclaration(methodeReturnMap.get(methode), methode,
					methodeParameterMap.get(methode)));
		}
		String string = String.format(interfacePlain, packageName,imports, objectName, bodyBuffer.toString());
		StaticMethods.writeToFile(targetFolder, "I", string, objectName);
	}

	public void createStub(String targetFolder, String packageName)
			throws FileNotFoundException, IOException, ParseException {
		loadInterface(interfaceFile);
		ArrayList<String> pList = new ArrayList<>();
		
		pList.add("IMiddleware" + " " + mwName);
		String target="targetId";
		pList.add("int" + " " + target);
	
		String register=String.format(registPlant,mwName);
		String imports="import interfaces.IMiddleware;\nimport interfaces.IStub;\nimport structure.Constants;\nimport org.json.simple.JSONObject;";
		ArrayList<String> pNameList = new ArrayList<>();
		StringBuffer parameterBuffer=new StringBuffer();
		pNameList.add(mwName);
		pNameList.add(target);
	
		String classPlain = StaticMethods.readEntirefile("resources/Idl_Plants/ClassPlain.txt");
		StringBuffer bodyBuffer = new StringBuffer();
		bodyBuffer.append(fCompiler.createConstructor(objectName + "Stub", pList, pNameList,register));

		
		for (String methode : methodeNames) {
			bodyBuffer.append(fCompiler.createInvokeFunction(methodeReturnMap.get(methode), methode,
					methodeParameterMap.get(methode), mwName));
			if(!methodeReturnMap.get(methode).equals("void")) {
				pList.add("boolean " + " " +methode+"Return=false");
				pList.add(methodeReturnMap.get(methode) + " " +methode);
			}
			
		}
		for(String parameter:pList) {
			parameterBuffer.append("	private "+parameter);
				parameterBuffer.append("; \n");
		}
		bodyBuffer.append(fCompiler.createIsConnectedMethode(mwName));
		bodyBuffer.append(fCompiler.createNotifyFunction(methodeNames, methodeReturnMap));
		bodyBuffer.append(fCompiler.createGetFunction("int", "targetId"));
		String string = String.format(classPlain, packageName,imports, objectName + "Stub" + " implements IStub", parameterBuffer.toString(),
				bodyBuffer.toString());
		StaticMethods.writeToFile(targetFolder, objectName, string, "Stub");
	}

	public void createSkeleton(String targetFolder, String packageName)
			throws FileNotFoundException, IOException, ParseException {
		String imports="import interfaces.I"+objectName+";";
		StringBuffer parameterBuffer=new StringBuffer();
		loadInterface(interfaceFile);
		ArrayList<String> pList = new ArrayList<>();
		pList.add( "I" + objectName + " " + objectName.toLowerCase());
		ArrayList<String> pNameList = new ArrayList<>();
		pNameList.add(objectName.toLowerCase());
		String classPlain = StaticMethods.readEntirefile("resources/Idl_Plants/ClassPlain.txt");
		StringBuffer bodyBuffer = new StringBuffer();
		bodyBuffer.append(fCompiler.createConstructor(objectName + "Skeleton", pList, pNameList,""));
		for (String methode : methodeNames) {
			bodyBuffer.append(fCompiler.createSetTargetMethode(methodeReturnMap.get(methode), methode,
					methodeParameterMap.get(methode), objectName.toLowerCase()));
		}
		for(String parameter:pList) {
			parameterBuffer.append("	private "+parameter);
				parameterBuffer.append("; \n");
		}
		String string = String.format(classPlain, packageName, imports, objectName + "Skeleton", parameterBuffer.toString(),
				bodyBuffer.toString());
		StaticMethods.writeToFile(targetFolder, objectName, string, "Skeleton");
	}

	public void setInterfaceFile(String interfaceFile) {
		this.interfaceFile = interfaceFile;
	}

	public static void main(String... args) throws IOException, ParseException {
		IdlCompiler idlc = new IdlCompiler("resources/Idl_Interfaces/RacInterface.json");
		String targetDirectory = "PhiloArm/stubs/";
		String targetDirectoryInterfaces = "PhiloArm/interfaces/";
		String packageNameInterfaces = "interfaces";
		String packageName = "stubs";
		idlc.createJavaServerInterface(targetDirectoryInterfaces, packageNameInterfaces,"");
		idlc.createSkeleton(targetDirectory, packageName);
		idlc.createStub(targetDirectory, packageName);
		idlc.createJavaMiddlewareInterface(targetDirectoryInterfaces, packageNameInterfaces);

		idlc.setInterfaceFile("resources/Idl_Interfaces/PhilosophenInterface.json");
		idlc.createJavaServerInterface(targetDirectoryInterfaces, packageNameInterfaces,"");
		idlc.createSkeleton(targetDirectory, packageName);
		idlc.createStub(targetDirectory, packageName);

		idlc.setInterfaceFile("resources/Idl_Interfaces/ViewInterface.json");
		idlc.createJavaServerInterface(targetDirectoryInterfaces, packageNameInterfaces,"");
		idlc.createSkeleton(targetDirectory, packageName);
		idlc.createStub(targetDirectory, packageName);
		
		idlc.setInterfaceFile("resources/Idl_Interfaces/StubInterface.json");
		idlc.createJavaServerInterface(targetDirectoryInterfaces, packageNameInterfaces,"import org.json.simple.JSONObject;");
	}
}
