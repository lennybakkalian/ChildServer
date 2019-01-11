package de.fettesteil.childserver.packets;

import java.util.UUID;

public class LoginPacket extends Packet{

	public LoginPacket(String KEY, boolean isServer, UUID uuid) {
		super(Packet.LOGINPACKET);
		put("key", KEY);
		put("isServer", isServer);
		put("uuid", uuid.toString());
	}

}
