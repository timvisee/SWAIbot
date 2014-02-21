package com.timvisee.swaibot.protocol.packet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import com.timvisee.swaibot.protocol.Protocol;

public class PacketHandler {

	private List<PacketListener> listeners = new ArrayList<PacketListener>();
	
	private boolean escapeNextChar = false;
	private boolean packetReceived = false;
	private StringBuilder buff = new StringBuilder();
	
	public static void send(OutputStreamWriter osw, Packet p) {
		try {
			osw.write(Protocol.serialize(p));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void received(int c) {
		if(c == ((char) '\\')) {
			escapeNextChar = true;
			buff.append((char) c);
			return;
		}
		
		if(!escapeNextChar) {
			if(c == 1) {
				escapeNextChar = false;
				packetReceived = false;
				buff = new StringBuilder();
				return;
				
			} else if(c == 2) {
				packetReceived = true;
				escapeNextChar = false;
			}
		}
		
		if(packetReceived) {
			Packet p = Protocol.deserialize(buff.toString());
			escapeNextChar = false;
			packetReceived = false;
			
			for(PacketListener pl : this.listeners)
				pl.onPacketReceived(p);
			
			return;
		}
		
		buff.append((char) c);
		escapeNextChar = false;
	}
	
	public void registerListener(PacketListener pl) {
		this.listeners.add(pl);
	}
	
	public void unregisterListener(PacketListener pl) {
		this.listeners.remove(pl);
	}
}
