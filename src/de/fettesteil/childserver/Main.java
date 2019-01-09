package de.fettesteil.childserver;

import java.net.Socket;

public class Main {

	public final static String MASTER_SERVER = "localhost";
	public final static int MASTER_SERVER_PORT = 2222;
	public final static int RECONNECT_DELAY = 2000;
	
	public static Socket masterServer;

	public static void main(String[] args) {
		
		Thread reconnectThread = new Thread(new ReconnectThread(MASTER_SERVER, MASTER_SERVER_PORT));
		reconnectThread.run();
		
	}
	
}
