package de.fettesteil.childserver.packets;

public class LoginPacket extends Packet{

	public LoginPacket(String KEY) {
		super(Packet.LOGINPACKET);
		put("key", KEY);
	}

}
