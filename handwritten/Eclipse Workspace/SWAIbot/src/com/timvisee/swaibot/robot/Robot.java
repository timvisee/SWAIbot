package com.timvisee.swaibot.robot;

public class Robot {
	
	private int legTl = 0;
	private int legTr = 0;
	private int legBl = 0;
	private int legBr = 0;
	
	private static int LEG_TOP_LEFT_DEF_ANGLE = 90;
	private static int LEG_TOP_RIGHT_DEF_ANGLE = 80;
	private static int LEG_BOTTOM_LEFT_DEF_ANGLE = 96;
	private static int LEG_BOTTOM_RIGHT_DEF_ANGLE = 90;
	
	/**
	 * Constructor
	 */
	public Robot() {
		// Reset the leg angles
		reset();
	}
	
	/**
	 * Get the left leg angle of the robot
	 * @return Leg angle
	 */
	public int getTopLeftLegAngle() {
		return this.legTl;
	}
	
	/**
	 * Set the left leg angle of the robot
	 * @param angle Leg angle
	 */
	public void setTopLeftLegAngle(int angle) {
		this.legTl = angle;
		
		// TODO: Limit leg angle
	}
	
	/**
	 * Get the right leg angle of the robot
	 * @return Leg angel
	 */
	public int getTopRightLegAngle() {
		return this.legTr;
	}
	
	/**
	 * Set the right leg angle of the robot
	 * @param angle Leg angel
	 */
	public void setTopRightLegAngle(int angle) {
		this.legTr = angle;
		
		// TODO: Limit leg angle
	}
	
	/**
	 * Get the left bottom leg angle of the robot
	 * @return Leg angel
	 */
	public int getBottomLeftLegAngle() {
		return this.legBl;
	}
	
	/**
	 * Set the left bottom leg angle of the robot
	 * @param angle Leg angel
	 */
	public void setBottomLeftLegAngle(int angle) {
		this.legBl = angle;
		
		// TODO: Limit leg angle
	}
	
	/**
	 * Get ther right bottom leg angle of the robot
	 * @return Leg angel
	 */
	public int getBottomRightLegAngle() {
		return this.legBr;
	}
	
	/**
	 * Set the right bottom leg angle of the robot
	 * @param angle Leg angel
	 */
	public void setBottomRightLegAngle(int angle) {
		this.legBr = angle;
		
		// TODO: Limit leg angle
	}
	
	/**
	 * Reset the angles of the robot
	 */
	public void reset() {
		this.legTl = LEG_TOP_LEFT_DEF_ANGLE;
		this.legTr = LEG_TOP_RIGHT_DEF_ANGLE;
		this.legBl = LEG_BOTTOM_LEFT_DEF_ANGLE;
		this.legBr = LEG_BOTTOM_RIGHT_DEF_ANGLE;
		
		// TODO: Limit leg angle
	}
}
