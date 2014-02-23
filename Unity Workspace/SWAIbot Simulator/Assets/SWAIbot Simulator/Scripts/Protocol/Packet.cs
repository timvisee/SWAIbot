using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Packet {
	
	private int targetId = 0;
	private int packetId = 0;
	
	private List<int> intList = new List<int>();
	private List<bool> boolList = new List<bool>();
	private List<string> strList = new List<string>();
	private List<short> shortList = new List<short>();
	
	public Packet(int targetId, int packetId) {
		this.targetId = targetId;
		this.packetId = packetId;
	}
	
	public int getTargetId() {
		return this.targetId;
	}
	
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	
	public int getPacketId() {
		return this.packetId;
	}
	
	public void setPacketId(int packetId) {
		this.packetId = packetId;
	}
	
	public List<int> getIntegers() {
		return this.intList;
	}
	
	public void appendInteger(int i) {
		intList.Add(i);
	}
	
	public bool hasIntegers() {
		if(intList == null)
			return false;
		
		return (intList.Count > 0);
	}
	
	public void setIntegers(List<int> intList) {
		this.intList = intList;
	}
	
	public void clearIntegers() {
		this.intList.Clear();
	}
	
	public List<bool> getBooleans() {
		return this.boolList;
	}
	
	public void appendBoolean(bool b) {
		boolList.Add(b);
	}
	
	public bool hasBooleans() {
		if(boolList == null)
			return false;
		
		return (boolList.Count > 0);
	}
	
	public void setBooleans(List<bool> boolList) {
		this.boolList = boolList;
	}
	
	public void clearBooleans() {
		this.boolList.Clear();
	}
	
	public List<string> getStrings() {
		return this.strList;
	}
	
	public void appendString(string str) {
		strList.Add(str);
	}
	
	public bool hasStrings() {
		if(strList == null)
			return false;
		
		return (strList.Count > 0);
	}
	
	public void setStrings(List<string> strList) {
		this.strList = strList;
	}
	
	public void clearStrings() {
		this.strList.Clear();
	}
	
	public List<short> getShorts() {
		return this.shortList;
	}
	
	public void appendShort(short s) {
		shortList.Add(s);
	}
	
	public bool hasShorts() {
		if(shortList == null)
			return false;
		
		return (shortList.Count > 0);
	}
	
	public void setShorts(List<short> shortList) {
		this.shortList = shortList;
	}
	
	public void clearShorts() {
		this.shortList.Clear();
	}
}
