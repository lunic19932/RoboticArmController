package test;

import java.net.SocketException;

import middlewareNetwork.MWNameserver;

public class NameserverTest {
	public static void main(String[] args) throws SocketException {
		MWNameserver mwNServer = new MWNameserver();
		mwNServer.getMessage();
	}
}
