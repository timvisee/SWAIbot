package com.timvisee.swaibot;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.timvisee.swaibot.protocol.Protocol;
import com.timvisee.swaibot.protocol.packet.Packet;
import com.timvisee.swaibot.protocol.packet.PacketHandler;
import com.timvisee.swaibot.protocol.packet.PacketListener;
import com.timvisee.swaibot.protocol.packet.PacketType;

public class ConnectionThread implements Runnable {
	
	public int packetsSend = 0;
	public int packetsReceived = 0;
	public boolean connected = false;
	
	protected PrintWriter out;
	
	public int fps = 0;
	private int frameCount = 0;
	private long countStart = 0;
	
	private ServerSocket serverSock;
	private Socket link;
	
	@Override
	public void run() {
		try {
			serverSock = new ServerSocket(SWAIbot.APP_PORT);
			
			while(true) {
				Core.instance.log.log("Waiting for a connection...");
				link = serverSock.accept();
				connected = true;
				Core.instance.log.log("Simulator successfully connected!");
				
				out = new PrintWriter(link.getOutputStream(), true);
				
				PacketHandler ph = new PacketHandler();

				ph.registerListener(new PacketListener() {
					@Override
					public void onPacketReceived(Packet p) {
						packetsReceived++;
						
						if(p.getPacketId() == PacketType.HEARTHBEAT.getId())
							Core.instance.packetLog.log("Received packet! (HEARTBEAT)");
						else
							Core.instance.packetLog.log("Received packet! (ID: " + p.getPacketId() + ")" +
									" (Ints: " + p.getIntegers().size() + ", Bools: " + p.getBooleans().size() +
									", Strings: " + p.getStrings().size() + ")");
						
						if(p.getPacketId() == PacketType.MOTOR_STATUS.getId()) {
							MainFrame.r.setTopLeftLegAngle(p.getIntegers().get(0));
							MainFrame.r.setTopRightLegAngle(p.getIntegers().get(1));
							MainFrame.r.setBottomLeftLegAngle(p.getIntegers().get(2));
							MainFrame.r.setBottomRightLegAngle(p.getIntegers().get(3));
						}
					}
				});
				
				Core.instance.log.log("Waiting for simulator packets...");
				//System.out.println("Received: " + input.nextLine());
				
				while(true) {
					frameCount++;
					if(countStart + 1000 <= System.currentTimeMillis()) {
						countStart = System.currentTimeMillis();
						fps = frameCount;
						frameCount = 0;
					}
					
					while(link.getInputStream().available() > 0)
						ph.received(link.getInputStream().read());
					
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void sendPacket(Packet p) {
		if(!connected)
			return;
		
		if(out == null)
			return;
		
		Core.instance.packetLog.log("Send packet! (ID : " + p.getPacketId() + ")" +
				" (Ints: " + p.getIntegers().size() + ", Bools: " + p.getBooleans().size() +
				", Strings: " + p.getStrings().size() + ")");
		packetsSend++;
		
		out.print(Protocol.serialize(p));
		out.flush();
	}
	
	public void disconnect() {
		try {
			if(connected)
				link.close();
			serverSock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isConnected() {
		return this.connected;
	}
}
