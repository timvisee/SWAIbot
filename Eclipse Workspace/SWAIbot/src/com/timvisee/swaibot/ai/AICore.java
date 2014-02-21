package com.timvisee.swaibot.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.timvisee.swaibot.Core;

public class AICore {
	
	private List<AITargetMovement> t = new ArrayList<AITargetMovement>();
	private int ti = 0;

	private List<AIMovement> m = new ArrayList<AIMovement>();
	
	private Random rand = new Random();
	
	private static final float CALC_PERSC = 90.0f;
	private static final int CALC_DELAY = 3000;
	
	public long lastMov = -1;
	
	/**
	 * Constructor
	 */
	public AICore() {
		// Add default (hardcoded) targets sequence
		t.add(new AITargetMovement(45, -45, -20, 20));
		t.add(new AITargetMovement(0, 0, 0, 0));
		t.add(new AITargetMovement(-45, 45, 20, -20));
		t.add(new AITargetMovement(0, 0, 0, 0));
		
		// Assign the first target
		setTarget(0);
	}
	
	/**
	 * Initialize
	 */
	public void init() {
		// Re instantiate the random object
		rand = new Random();
		
		// Start the movement task
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// Create an infinite loop
				while(true) {
					// Ensure the robot is connected
					try {
						if(Core.instance.getMainFrame().ct.isConnected()) {
							// Calculate and send a new movement packet
							if(lastMov + CALC_DELAY <= System.currentTimeMillis() || lastMov < 0) {
								// Update the last move time
								lastMov = System.currentTimeMillis();
								
								// Calculate a new move
								AIMovement mov = calculateNew();
								
								// Send the move to the robot
								Core.instance.getMainFrame().ct.sendPacket(mov.createMovementPacket());
							}
						}
					} catch(Exception ex) { }	
				}
 			}
		});
		t.start();
	}
	
	/**
	 * Calculate a new movement
 	 * @return New movement
	 */
	public AIMovement calculateNew() {
		// Define the calculated movement variable
		AIMovement newMov = new AIMovement(Core.instance.getRobot());
		
		// Check whether there isn't a previous movements available, if so do a random movement
		if(m.size() == 0) {
			newMov.setServo(rand.nextInt(4), rand.nextInt(179) - (179 / 2));
			return newMov;
		}
		
		// Get the target movement
		AITargetMovement target = getTarget();
		
		// Buffer variable to store the current best movement in
		AIMovement templMov = null;
		int templMovScore = -1;
		
		// Try to determine the best past movement
		for(AIMovement entry : m) {
			// Calculate the movement's score
			int score = entry.calculateScore(target);
			
			// Determine whether this movement is better than the current selected one
			if((templMovScore < 0 || score < templMovScore) ||
					(templMovScore == score && rand.nextInt(2) == 0)) {
				templMov = entry;
				templMovScore = score;
			}
		}
		
		// Determine what servo to move
		int servoIndex = -1;
		int servoScore = -1;
		for(int i = 0; i < 4; i++) {
			int curScore = Math.abs(templMov.getServo(i) - target.getServo(i));
			
			if((curScore > servoScore) || (servoIndex == -1 || servoScore == -1)) {
				servoIndex = i;
				servoScore = curScore;
			}
		}
		
		// Gather the servo target
		int servoTarget = templMov.getServo(servoIndex);
		
		// Modify the movement a little
		int maxDiff = (int) Math.max(((float) CALC_PERSC - Math.pow(m.size(), 0.06f)) / 1.8f, 1);
		int diff = rand.nextInt(maxDiff) - (maxDiff / 2);
		servoTarget += diff;
		
		// Clip the target
		servoTarget += Math.min(Math.max(servoTarget, 0), 179);
		
		// Update the new movement
		newMov.setServo(servoIndex, servoTarget);
		
		// Validate the new movement
		newMov.validate();
		
		// Add the new movement to the history
		m.add(newMov);
		
		// Check whether the next target should be assigned
		if(templMovScore <= 8)
			nextTarget();
		
		// Return the new movement
		return newMov;
	}
	
	/**
	 * Get the current target movement
	 */
	public AITargetMovement getTarget() {
		return t.get(ti);
	}
	
	/**
	 * Assign the next target
	 */
	public void nextTarget() {
		// Increase the target index by one
		ti++;
		
		// Clip the target index
		if(ti >= t.size())
			ti = 0;
	}
	
	/**
	 * Set the target index
	 * @param ti Target index
	 */
	public void setTarget(int ti) {
		this.ti = ti;
	}

	/**
	 * Get the moves count
	 * @return Moves count
	 */
	public int getMovesCount() {
		return m.size();
	}
}
