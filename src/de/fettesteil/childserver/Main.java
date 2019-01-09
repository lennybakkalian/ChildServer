package de.fettesteil.childserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import de.fettesteil.childserver.packets.Packet;

public class Main {

	public final static String MASTER_SERVER = "localhost";
	public final static int MASTER_SERVER_PORT = 2222;
	public final static int RECONNECT_DELAY = 2000;

	public static Socket masterServer;
	public static BufferedReader br;
	public static PrintWriter pw;
	public static boolean connected = false;

	public static JSONObject config;

	public static void main(String[] args) {

		// generate config if not exist
		try {
			File configFile = new File("childserver.json");
			if (configFile.exists()) {
				// load existing uuid
				BufferedReader br = new BufferedReader(new FileReader(configFile));
				config = (JSONObject) new JSONParser().parse(br.readLine());
				br.close();
				System.out.println("Config: " + config.toJSONString());
			} else {
				// create new config
				config = new JSONObject();
				Scanner s = new Scanner(System.in);
				String ln;
				System.out.print("UUID (default empty): ");
				ln = s.nextLine();
				if (ln.length() == 0) {
					// create new uuid
					config.put("uuid", UUID.randomUUID().toString());
				} else {
					config.put("uuid", UUID.fromString(ln).toString());
				}
				System.out.print("MasterServer IP: ");
				config.put("ip", s.nextLine());
				System.out.print("MasterServer PORT: ");
				// convert to int to check if port is a number, then back to
				// string
				config.put("port", String.valueOf(Integer.valueOf(s.nextLine())));
				System.out.print("MasterServer Key: ");
				config.put("key", s.nextLine());
				FileWriter wf = new FileWriter(configFile);
				wf.write(config.toJSONString());
				wf.close();
				System.out.println("-- SAVED --");
				s.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: Can't create config. Reason: " + e.getMessage());
		}

		new ReadThread().start();

		Thread reconnectThread = new Thread(new ReconnectThread(MASTER_SERVER, MASTER_SERVER_PORT));
		reconnectThread.run();
		
		

	}

	public static void send(Packet p) {
		if (connected && pw != null) {
			pw.println(p.getData().toJSONString());
		}
	}

	public static void disconnect() {
		if (connected) {
			System.out.println("Disconnected");
		}
	}

}
