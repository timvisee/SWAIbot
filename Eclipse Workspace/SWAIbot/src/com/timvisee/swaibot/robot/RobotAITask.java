package com.timvisee.swaibot.robot;

import java.util.Random;

import com.timvisee.swaibot.ConnectionThread;
import com.timvisee.swaibot.MainFrame;
import com.timvisee.swaibot.protocol.packet.Packet;
import com.timvisee.swaibot.protocol.packet.PacketType;

public class RobotAITask implements Runnable {
	
	/**
	 * Fake movement, used to test the robot!
	 */
	
	public long lastHeartbeat = 0;
	public long lastMotorUpdate = 0;
	public int moves = 0;
	public int step = -1;
	public boolean smart = false;
	
	public ConnectionThread ct;
	
	public int fps = 0;
	private int frameCount = 0;
	private long countStart = 0;
	
	@Override
	public void run() {
		while(true) {
			try {
				frameCount++;
				if(countStart + 1000 <= System.currentTimeMillis()) {
					countStart = System.currentTimeMillis();
					fps = frameCount;
					frameCount = 0;
				}
				
				if(ct == null) {
					Thread.sleep(16);
					continue;
				}
				if(!ct.connected) {
					Thread.sleep(16);
					continue;
				}
				
				if(lastHeartbeat + 1000 <= System.currentTimeMillis()) {
					lastHeartbeat = System.currentTimeMillis();
					
					Packet p = new Packet(0, PacketType.HEARTHBEAT.getId());
					ct.sendPacket(p);
				}
				
				if(lastMotorUpdate + 2500 <= System.currentTimeMillis()) {
					lastMotorUpdate = System.currentTimeMillis();
					moves++;
					
					Random rand = new Random();
					Packet p = new Packet(0, PacketType.SET_MOTOR.getId());
					
					if(smart) {
						if(step >= 0) {
							switch(step % 10) {
							case 0:
								p.appendInteger(1);
								p.appendInteger(45);
								p.appendInteger(3);
								p.appendInteger(-40);
								break;
							case 1:
								p.appendInteger(2);
								p.appendInteger(-30);
								break;
							case 2:
								p.appendInteger(1);
								p.appendInteger(0);
								p.appendInteger(3);
								p.appendInteger(0);
								break;
							case 3:
								p.appendInteger(4);
								p.appendInteger(-40);
								break;
							case 4:
								p.appendInteger(2);
								p.appendInteger(0);
								p.appendInteger(4);
								p.appendInteger(0);
								break;
								
							case 5:
								p.appendInteger(2);
								p.appendInteger(45);
								p.appendInteger(4);
								p.appendInteger(-40);
								break;
							case 6:
								p.appendInteger(1);
								p.appendInteger(-30);
								break;
							case 7:
								p.appendInteger(2);
								p.appendInteger(0);
								p.appendInteger(4);
								p.appendInteger(0);
								break;
							case 8:
								p.appendInteger(3);
								p.appendInteger(-40);
								break;
							case 9:
								p.appendInteger(1);
								p.appendInteger(0);
								p.appendInteger(3);
								p.appendInteger(0);
								break;
							default:
							}
						}
						step++;
					} else {
						int moveDif = 0;
						int leg = 0;
						int newAngle = 0;
						
						while(moveDif < 25) {
							if(rand.nextInt(2) == 0) {
								newAngle = rand.nextInt(45 + 90) - 90;
								if(rand.nextInt(2) == 0) {
									leg = 1;
									moveDif = Math.abs(MainFrame.instance.r.getTopLeftLegAngle() - newAngle);
								} else {
									leg = 2;
									moveDif = Math.abs(MainFrame.instance.r.getTopRightLegAngle() - newAngle);
								}
							} else {
								newAngle = rand.nextInt(120) - 120;
								if(rand.nextInt(2) == 0) {
									leg = 3;
									moveDif = Math.abs(MainFrame.instance.r.getBottomLeftLegAngle() - newAngle);
								} else {
									leg = 4;
									moveDif = Math.abs(MainFrame.instance.r.getBottomRightLegAngle() - newAngle);
								}
							}
						}
						p.appendInteger(leg);
						p.appendInteger(newAngle);	
					}
						
					ct.sendPacket(p);
				}
				
				Thread.sleep(16);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
