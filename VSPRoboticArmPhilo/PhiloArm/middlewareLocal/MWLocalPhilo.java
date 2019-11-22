package middlewareLocal;


import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import interfaces.IMWPhilo;
import interfaces.IStub;
import structure.NetworkConnection;
import structure.Constants;
import structure.Header;
import stubs.PhiloSkeleton;

public class MWLocalPhilo extends MWLocal implements IMWPhilo {
	private ArrayList<MWLocalPhilo> philoMWList;
	private int id;
	private PhiloSkeleton skeleton;

	public MWLocalPhilo(int id) {
		this.id = id;

	}

	public void setSkeleton(PhiloSkeleton skeleton) {
		this.skeleton = skeleton;
	}



	public void setMWS(ArrayList<MWLocalPhilo> philoMWList) {
		this.philoMWList = philoMWList;
	}




	@Override
	public void functionCallHandler(String functionName, String parameterList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void invoke(String functionName, String parameterList, IStub stub) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isConnected(IStub stub) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void register(IStub stub) {
		// TODO Auto-generated method stub
		
	}
}
