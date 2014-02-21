package com.timvisee.swaibot.ai;

import java.util.ArrayList;
import java.util.List;

import com.timvisee.swaibot.protocol.packet.Packet;
import com.timvisee.swaibot.protocol.packet.PacketType;
import com.timvisee.swaibot.robot.Robot;

public class AIMovement {
	
	private int servoTlPos = 0;
	private int servoTrPos = 0;
	private int servoBlPos = 0;
	private int servoBrPos = 0;
	
	private static int LEG_TOP_LEFT_OFFSET_ANGLE = 90;
	private static int LEG_TOP_RIGHT_OFFSET_ANGLE = 80;
	private static int LEG_BOTTOM_LEFT_OFFSET_ANGLE = 96;
	private static int LEG_BOTTOM_RIGHT_OFFSET_ANGLE = 90;
	
	/**
	 * Constructors
	 */
	public AIMovement() { }
	
	/**
	 * Constructors
	 * @param r Use robot as current position
	 */
	public AIMovement(Robot r) {
		this(
				r.getTopLeftLegAngle(), r.getTopRightLegAngle(),
				r.getBottomLeftLegAngle(), r.getBottomRightLegAngle()
				);
	}
	
	/**
	 * Constructor
	 * @param servoTlPos Top left servo position
	 * @param servoTrPos Top right servo position
	 * @param servoBlPos Bottom left servo position
	 * @param servoBrPos Bottom right servo position
	 */
	public AIMovement(int servoTlPos, int servoTrPos, int servoBlPos, int servoBrPos) {
		this.servoTlPos = servoTlPos;
		this.servoTrPos = servoTrPos;
		this.servoBlPos = servoBlPos;
		this.servoBrPos = servoBrPos;
	}
	
	/**
	 * Get top left servo position
	 * @return Top left servo position
	 */
	public int getServoTlPos() {
		return servoTlPos;
	}
	
	/**
	 * Set top left servo position
	 * @param servoTlPos Top left servo position
	 */
	public void setServoTlPos(int servoTlPos) {
		this.servoTlPos = servoTlPos;
	}
	
	/**
	 * Get top right servo position
	 * @return Top right servo position
	 */
	public int getServoTrPos() {
		return servoTrPos;
	}
	
	/**
	 * Set top right servo position
	 * @param servoTrPos Top right servo position
	 */
	public void setServoTrPos(int servoTrPos) {
		this.servoTrPos = servoTrPos;
	}
	
	/**
	 * Get bottom left servo position
	 * @return Bottom left servo position
	 */
	public int getServoBlPos() {
		return servoBlPos;
	}
	
	/**
	 * Set bottom left servo potition
	 * @param servoBlPos Bottom left servo position
	 */
	public void setServoBlPos(int servoBlPos) {
		this.servoBlPos = servoBlPos;
	}
	
	/**
	 * Get bottom right servo position
	 * @return Bottom right servo position
	 */
	public int getServoBrPos() {
		return servoBrPos;
	}
	
	/**
	 * Set bottom right servo position
	 * @param servoBrPos Bottom right servo position
	 */
	public void setServoBrPos(int servoBrPos) {
		this.servoBrPos = servoBrPos;
	}
	
	/**
	 * Return position of a servo
	 * @param i Index of the servo
	 * @return Servo position
	 */
	public int getServo(int i) {
		switch(i) {
		case 0:
			return this.servoTlPos;
			
		case 1:
			return this.servoTrPos;
			
		case 2:
			return this.servoBlPos;
			
		case 3:
			return this.servoBrPos;
			
		default:
			return 0;
		}
	}
	
	/**
	 * Set the position of a servo
	 * @param i Inex of the servo
	 * @param pos Servo position
	 */
	public void setServo(int i, int pos) {
		switch(i) {
		case 0:
			this.servoTlPos = pos;
			break;
			
		case 1:
			this.servoTrPos = pos;
			break;
			
		case 2:
			this.servoBlPos = pos;
			break;
			
		case 3:
			this.servoBrPos = pos;
			break;
			
		default:
		}
	}
	
	/**
	 * Calculate the score of this movement for a target movement
	 * @param tm Target movement to calculate the score for
	 * @return Score
	 */
	public int calculateScore(AITargetMovement tm) {
		int s = 0;
		
		// Calculate the score
		for(int i = 0; i < 4; i++)
			s += Math.abs(getServo(i) - tm.getServo(i));
		
		// Return the score
		return s;
	}

	/**
	 * Validate the movement
	 */
	public void validate() {
		for(int i = 0; i < 4; i++)
			if(getServo(i) < 0 || getServo(i) > 0)
				setServo(i, Math.min(Math.max(getServo(i), 0), 179));
	}
	
	/**
	 * Create a movement packet
	 * @return Movement packet
	 */
	public Packet createMovementPacket() {
		// Create the ints list
		List<Integer> ints = new ArrayList<Integer>();
		
		// Add each servo movement pos into the ints list
		for(int i = 0; i < 4; i++)
			ints.add(getServo(i));
		
		// Create a packet
		Packet p = new Packet(0, PacketType.SET_MOTOR.getId());
		p.setIntegers(ints);
		
		// Return the packet
		return p;
	}
}
