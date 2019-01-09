package de.fettesteil.childserver;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.fettesteil.childserver.packets.LoginResponse;
import de.fettesteil.childserver.packets.Packet;

public class ReadThread extends Thread {

	@Override
	public void run() {
		while (!interrupted()) {
			synchronized (this) {
				try {
					if (Main.connected) {
						String ln = Main.br.readLine();
						if (ln != null) {
							JSONObject json = (JSONObject) new JSONParser().parse(ln);
							int packetid = Integer.valueOf((String) json.get("packetid"));
							switch (packetid) {
							case Packet.LOGINRESPONSE:
								int status = Integer.valueOf((String) json.get("status"));
								if (status == LoginResponse.LOGIN_SUCCESS) {
									System.out.println("Logged In!");
								} else {
									System.out.println("Authentication failure! Retry in 10 Seconds");
									Thread.currentThread().sleep(10000);
								}
								break;
							}
						}
					} else {
						Thread.currentThread().sleep(1000);
					}
				} catch (Exception e) {
					// e.printStackTrace();
					Main.disconnect();
				}
			}
		}
	}

}
