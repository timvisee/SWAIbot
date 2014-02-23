using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

public class PacketHandler {
	
	private bool escapeNextChar = false;
	private bool packetReceived = false;
	private StringBuilder buff = new StringBuilder();

	public void received(string str) {
		foreach(char c in str)
			received((int) c);
	}

	public void received(int c) {
		if(c == ((char) '\\')) {
			escapeNextChar = true;
			buff.Append((char) c);
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
			Packet p = Protocol.deserialize(buff.ToString());
			escapeNextChar = false;
			packetReceived = false;

			// TODO: Call packet listeners
			/*foreach(PacketListener pl in this.listeners)
				pl.onPacketReceived(p);*/

			SocketController.OnReceived(p);
			
			return;
		}
		
		buff.Append((char) c);
		escapeNextChar = false;
	}
}
