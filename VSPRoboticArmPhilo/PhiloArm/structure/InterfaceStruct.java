package structure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import staticMethods.StaticMethods;

public class InterfaceStruct {
	
	private ArrayList<String> methodeNames;
	private String objectName;
	private Map<String, String> methodeReturnMap;
	private  Map<String, Map<Integer, Map<String, String>>> methodeParameterMap;
	private String fileName ;
	public InterfaceStruct(String interfaceFile) throws FileNotFoundException, IOException, ParseException {
		fileName = interfaceFile;
		methodeNames=new ArrayList<String>();
		methodeReturnMap=new HashMap<String, String>();
		methodeParameterMap=new HashMap<String, Map<Integer, Map<String, String>>>();
		readInterface();
	}
	void readInterface() throws FileNotFoundException, IOException, ParseException {
		
        String jsonText = StaticMethods.readEntirefile(fileName);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonText);
       objectName = (String) json.get("ServiceName");
        JSONArray methodes = (JSONArray) json.get("Functions");
        System.out.println(json.get("Function"));

        for (Object obj : methodes) {
            JSONObject jsonObj = (JSONObject) obj;
            String methodeName = (String) jsonObj.get("name");
            methodeNames.add(methodeName);
         
            JSONArray parameterArray = (JSONArray) jsonObj.get("parameters");
            Map<Integer, Map<String, String>> parameterPositionMap = new HashMap<>();

            for (Object parameterObj : parameterArray) {
                JSONObject jsonParameterObj = (JSONObject) parameterObj;
                HashMap<String, String> parameterDescriptionMap = new HashMap<>();

                Integer positionInteger = Integer.parseInt(jsonParameterObj.get("position").toString());
                parameterDescriptionMap.put("type", (String) jsonParameterObj.get("type"));
                parameterDescriptionMap.put("name", (String) jsonParameterObj.get("name"));

                parameterPositionMap.put(positionInteger, parameterDescriptionMap);
            }
            methodeParameterMap.put(methodeName, parameterPositionMap);
            methodeReturnMap.put(methodeName, (String) jsonObj.get("returnType"));	
        }
	}

	public ArrayList<String> getMethodeNames() {
		return methodeNames;
	}

	public String getObjectName() {
		return objectName;
	}

	public Map<String, String> getMethodeReturnMap() {
		return methodeReturnMap;
	}

	public Map<String, Map<Integer, Map<String, String>>> getMethodeParameterMap() {
		return methodeParameterMap;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		InterfaceStruct str=new InterfaceStruct("resources/Idl_Interfaces/ServerInterface.json");
		str.readInterface();
	}
}
