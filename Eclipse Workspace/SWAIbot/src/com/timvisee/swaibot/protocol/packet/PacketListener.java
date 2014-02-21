package com.timvisee.swaibot.protocol.packet;

public abstract class PacketListener {
	
	/**
	 * Called when a packet is received
	 * @param p Received packet
	 */
	public abstract void onPacketReceived(Packet p);
	
}
