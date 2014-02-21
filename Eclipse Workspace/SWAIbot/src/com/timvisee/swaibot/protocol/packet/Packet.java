package com.timvisee.swaibot.protocol.packet;

import java.util.ArrayList;
import java.util.List;

public class Packet {

	private int targetId = 0;
	private int packetId = 0;

	private List<Integer> intList = new ArrayList<Integer>();
	private List<Boolean> boolList = new ArrayList<Boolean>();
	private List<String> strList = new ArrayList<String>();
	
	/**
	 * Constructor
	 * @param targetId Target device ID
	 * @param packetId Packet type ID
	 */
	public Packet(int targetId, int packetId) {
		this.targetId = targetId;
		this.packetId = packetId;
	}
	
	/**
	 * Get the target ID
	 * @return Target ID
	 */
	public int getTargetId() {
		return this.targetId;
	}
	
	/**
	 * Set the target ID
	 * @param targetId Target ID
	 */
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	
	/**
	 * Get the packet type ID
	 * @return Packet type ID
	 */
	public int getPacketId() {
		return this.packetId;
	}
	
	/**
	 * Set the packet type ID
	 * @param packetId Packet type ID
	 */
	public void setPacketId(int packetId) {
		this.packetId = packetId;
	}
	
	/**
	 * Get the list of integers in this packet
	 * @return List of integers
	 */
	public List<Integer> getIntegers() {
		return this.intList;
	}
	
	/**
	 * Append a new integer to the integer list in this packet
	 * @param i Integer to append
	 */
	public void appendInteger(Integer i) {
		intList.add(i);
	}
	
	/**
	 * Check whether this packet has any integers
	 * @return True if this packet had any integers
	 */
	public boolean hasIntegers() {
		if(intList == null)
			return false;
		
		return (intList.size() > 0);
	}
	
	/**
	 * Set the list of integers in this packet
	 * @param intList
	 */
	public void setIntegers(List<Integer> intList) {
		this.intList = intList;
	}
	
	/**
	 * Clear the list of integers
	 * @return Amount of integers cleared
	 */
	public int clearIntegers() {
		// Count the integer count
		int count =- this.intList.size();
		
		// Clear the integer list
		this.intList.clear();
		
		// Return element count
		return count;
	}
	
	/**
	 * Get the list of booleans in this packet
	 * @return List of booleans
	 */
	public List<Boolean> getBooleans() {
		return this.boolList;
	}
	
	/**
	 * Append a boolean to the booelan list
	 * @param b Boolean to append
	 */
	public void appendBoolean(Boolean b) {
		boolList.add(b);
	}
	
	/**
	 * Check whether this packet has any booleans
	 * @return True if this packet has any booleans
	 */
	public boolean hasBooleans() {
		if(boolList == null)
			return false;
		
		return (boolList.size() > 0);
	}
	
	/**
	 * Set the list of booleans
	 * @param boolList List of booleans
	 */
	public void setBooleans(List<Boolean> boolList) {
		this.boolList = boolList;
	}
	
	/**
	 * Clear the list of booleans
	 * @return Amount of booleans cleared
	 */
	public int clearBooleans() {
		// Get the boolean count
		int count = this.boolList.size();
		
		// Clear the booleans
		this.boolList.clear();
		
		// Return the amount of booleans cleared
		return count;
	}
	
	/**
	 * Get the list of strings in this packet
	 * @return List of strings
	 */
	public List<String> getStrings() {
		return this.strList;
	}
	
	/**
	 * Append a string to the string list
	 * @param str String to append
	 */
	public void appendString(String str) {
		strList.add(str);
	}
	
	/**
	 * Check whether this packet has any strings
	 * @return True if this packet has any strings
	 */
	public boolean hasStrings() {
		if(strList == null)
			return false;
		
		return (strList.size() > 0);
	}
	
	/**
	 * Set the list of strings in this packet
	 * @param strList String list
	 */
	public void setStrings(List<String> strList) {
		this.strList = strList;
	}
	
	/**
	 * Clear the list of strings
	 * @return Amount of strings cleared
	 */
	public int clearStrings() {
		// Count the strings
		int count = this.strList.size();
		
		// Clear the string list
		this.strList.clear();
		
		// Return the amount of strings cleared
		return count;
	}
}
