package test;

import java.net.SocketException;

import application.GUI;

public class GuiTest {
public static void main(String[] args) {
	try {
		GUI gui=new GUI(1);
	} catch (SocketException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
	
}
