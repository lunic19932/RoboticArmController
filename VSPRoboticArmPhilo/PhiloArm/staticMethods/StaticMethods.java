package staticMethods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import org.json.simple.JSONObject;

import com.google.common.primitives.Bytes;

import structure.Constants;

public class StaticMethods {

	public static int readNServerHeader(DatagramPacket packet) {
		byte[] tempBuffer = new byte[4];
    	System.arraycopy(packet.getData(), 0, tempBuffer, 0, 4);
    	ByteBuffer wrapped = ByteBuffer.wrap(tempBuffer);
    	int size = wrapped.getInt();
    	
		return size;
	}
	
	public static byte[] jsonToByteMessageNServer(JSONObject json) {
		byte[] header=new byte[Constants.HEADER_SIZE_NAMESERVER];
		
		int bodySize=json.toString().getBytes().length;
		for(int i =0;i<header.length;i++) {
			header[i]=(byte) (bodySize>>(8*(3-i)));
		}
		byte[] body=json.toString().getBytes();
		byte[] message=Bytes.concat(header,body);
	
		return message;
	}
	
	public static void writeToFile(String targetDirectory, String objectName, String classString,String type) throws IOException {
        String fileName;
        fileName = targetDirectory + objectName +type+ ".java";
        PrintWriter writer = new PrintWriter(new FileWriter(new File(fileName)));

        writer.print(classString);
        writer.flush();
        writer.close();
    }

	public static String readEntirefile(String fileName) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

        String line = "";
        StringBuffer buffer = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append("\n");
        }

        reader.close();
        String jsonText = buffer.toString();
        return jsonText;
    }
	
	public static int parseByteToInt(byte[] byteArray) {
		byte[] tempBuffer = new byte[4];
		System.arraycopy(byteArray, 0, tempBuffer, 0, Constants.SIZE_OF_INT_BYTE);
		ByteBuffer wrapped = ByteBuffer.wrap(tempBuffer);
		return wrapped.getInt();
	}
	
	public static byte[] parseIntToByte(int integer) {
		byte[] byteArray =new byte[Constants.SIZE_OF_INT_BYTE];
		for (int i = 0; i < Constants.SIZE_OF_INT_BYTE; i++) {
			byteArray[i] = (byte) (integer>> 24 - i * Byte.SIZE);
		}
		return byteArray;
	}
}
