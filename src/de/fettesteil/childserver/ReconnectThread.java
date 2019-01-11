package de.fettesteil.childserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

import de.fettesteil.childserver.packets.LoginPacket;

public class ReconnectThread implements Runnable {

	private String host;
	private int port;

	public ReconnectThread(String host, int port) {
		this.host = host;
		this.port = port;
	}

	@Override
	public void run() {
		while (!Thread.currentThread().interrupted()) {
			synchronized (this) {
				try {
					if (!Main.connected) {
						if (Main.masterServer != null)
							System.out.println("[ReconnectThread] Trying to reconnect");
						connect();
					}
					Thread.currentThread().sleep(5000);
				} catch (Exception e) {
					// e.printStackTrace();
				}
			}
		}
	}

	private void connect() throws Exception {
		Main.masterServer = new Socket(host, port);
		Main.connected = true;
		Main.br = new BufferedReader(new InputStreamReader(Main.masterServer.getInputStream()));
		Main.pw = new PrintWriter(Main.masterServer.getOutputStream(), true);
		Main.send(new LoginPacket((String) Main.config.get("key"), true,
				UUID.fromString((String) Main.config.get("uuid"))));
		System.out.println("[ReconnectThread] connected to " + host);
	}

}
