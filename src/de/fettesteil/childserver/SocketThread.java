package de.fettesteil.childserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.fettesteil.childserver.packets.Packet;
import de.fettesteil.childserver.packets.PingTestPacket;

public class SocketThread implements Runnable {

	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private boolean connected = false;

	public SocketThread(Socket socket) throws Exception {
		this.socket = socket;
		this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.pw = new PrintWriter(socket.getOutputStream(), true);
	}

	public void send(Packet p) {
		try {
			pw.println(p.getData().toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (connected) {
			synchronized (this) {
				try {
					String raw = br.readLine();
					if (raw == null || raw.length() == 0)
						break;
					JSONObject json = (JSONObject) new JSONParser().parse(raw);
					int packetid = Integer.valueOf((String) json.get("packetid"));
					switch (packetid) {
					case Packet.PINGTEST_SEND:
						// send ping back
						send(new PingTestPacket(Packet.PINGTEST_RECV));
						break;
					}
				} catch (Exception e) {
					break;
				}
			}
		}
		disconnect();
	}

	public void disconnect() {
		if (isConnected()) {
			try {
				connected = false;
				if (!socket.isClosed())
					socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean isConnected() {
		return connected;
	}
}
