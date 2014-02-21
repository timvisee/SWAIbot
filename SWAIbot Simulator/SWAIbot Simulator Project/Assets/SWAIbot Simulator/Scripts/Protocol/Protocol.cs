using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Text;

public class Protocol {
	
	private static char PACKET_HEADER = (char) 1;
	private static char PACKET_FOOTER = (char) 2;
	private static char PACKET_PART_SEPARATOR = (char) 3;
	private static char PACKET_DATA_PART_SEPARATOR= (char) 4;
	private static char PACKET_DATA_PART_ARRAY_SEPARATOR = (char) 5;
	
	private static int DATA_ARRAY_UNKNOWN = 0;
	private static int DATA_ARRAY_INTEGER = 1;
	private static int DATA_ARRAY_BOOLEAN = 2;
	private static int DATA_ARRAY_STRING = 3;
	private static int DATA_ARRAY_SHORT = 4;
	
	public static string serialize(Packet p) {
		StringBuilder sb = new StringBuilder();
		
		// Add the packet header
		sb.Append(PACKET_HEADER);
		
		// Send the target device ID
		sb.Append(serializeInt(p.getTargetId()));
		
		// Separate the packet data
		sb.Append(PACKET_PART_SEPARATOR);
		
		// Send the packet id
		sb.Append(serializeInt(p.getPacketId()));
		
		// Separate the packet data
		sb.Append(PACKET_PART_SEPARATOR);
		
		// Serialize the integer arrays
		if(p.hasIntegers()) {
			// Add the array type
			sb.Append(DATA_ARRAY_INTEGER);
			
			foreach(int entry in p.getIntegers()) {
				// Separate the array data
				sb.Append(PACKET_DATA_PART_ARRAY_SEPARATOR);
				
				sb.Append(serializeInt(entry));
			}
			
			// Separate the arrays
			sb.Append(PACKET_DATA_PART_SEPARATOR);
		}
		
		// Serialize the string arrays
		if(p.hasBooleans()) {
			// Add the array type
			sb.Append(DATA_ARRAY_BOOLEAN);
			
			foreach(bool entry in p.getBooleans()) {
				// Separate the array data
				sb.Append(PACKET_DATA_PART_ARRAY_SEPARATOR);
				
				sb.Append(serializeBoolean(entry));
			}
			
			// Separate the arrays
			sb.Append(PACKET_DATA_PART_SEPARATOR);
		}
		
		// Serialize the string arrays
		if(p.hasStrings()) {
			// Add the array type
			sb.Append(DATA_ARRAY_STRING);
			
			foreach(string entry in p.getStrings()) {
				// Separate the array data
				sb.Append(PACKET_DATA_PART_ARRAY_SEPARATOR);
				
				sb.Append(serializeString(entry));
			}
			
			// Separate the arrays
			sb.Append(PACKET_DATA_PART_SEPARATOR);
		}
		
		// Serialize the string arrays
		if(p.hasShorts()) {
			// Add the array type
			sb.Append(DATA_ARRAY_SHORT);
			
			foreach(short entry in p.getShorts()) {
				// Separate the array data
				sb.Append(PACKET_DATA_PART_ARRAY_SEPARATOR);
				
				sb.Append(serializeShort(entry));
			}
			
			// Separate the arrays
			sb.Append(PACKET_DATA_PART_SEPARATOR);
		}
		
		// Add the packet footer
		sb.Append(PACKET_FOOTER);
		
		return sb.ToString();
	}
	
	public static Packet deserialize(string str) {
		// Split the packet
		string[] parts = str.Split(new char[] { PACKET_PART_SEPARATOR }, StringSplitOptions.None);

		// Make sure either two to three parts are available
		if(parts.Length < 2 || parts.Length > 3) {
			Debug.Log("ERROR! INVALID PART COUNT!");
			return null;
		}

		// Get the packet target device ID
		int targetId = deserializeInt(parts[0]);
		
		// Get the packet ID
		int pId = deserializeInt(parts[1]);
		
		// Create a new packet
		Packet p = new Packet(targetId, pId);
		
		// Parse the data if available
		if(parts.Length >= 3) {
			string dataStr = parts[2];
			
			string[] dataParts = dataStr.Split(new char[] { PACKET_DATA_PART_SEPARATOR }, StringSplitOptions.None);
			// Parse each array
			foreach(string dataArrStr in dataParts) {
				// Make sure this array string contains anything
				if(dataArrStr.Trim().Length == 0)
					continue;
				
				// Split the array string
				string[] arrParts = dataArrStr.Split(new char[] { PACKET_DATA_PART_ARRAY_SEPARATOR }, StringSplitOptions.None);
				
				string arrTypeStr = arrParts[0];
				int arrType = deserializeInt(arrTypeStr);
				
				// Parse the array
				if(arrType == DATA_ARRAY_INTEGER) {
					List<int> buff = new List<int>();
					
					for(int i = 1; i < arrParts.Length; i++)
						buff.Add(deserializeInt(arrParts[i]));
					
					p.setIntegers(buff);
					
				} else if(arrType == DATA_ARRAY_BOOLEAN) {
					List<bool> buff = new List<bool>();
					
					for(int i = 1; i < arrParts.Length; i++)
						buff.Add(deserializeBoolean(arrParts[i]));
					
					p.setBooleans(buff);
					
				} else if(arrType == DATA_ARRAY_STRING) {
					List<string> buff = new List<string>();
					
					for(int i = 1; i < arrParts.Length; i++)
						buff.Add(deserializeString(arrParts[i]));
					
					p.setStrings(buff);
					
				} else if(arrType == DATA_ARRAY_SHORT) {
					List<short> buff = new List<short>();
					
					for(int i = 1; i < arrParts.Length; i++)
						buff.Add(deserializeShort(arrParts[i]));
					
					p.setShorts(buff);
				}
			}
		}
		
		// Return the packet
		return p;
	}
	
	private static string serializeInt(int i) {
		return ("" + i);
	}
	
	private static int deserializeInt(string str) {
		return Convert.ToInt32(str);
	}
	
	private static string serializeShort(short s) {
		return ("" + s);
	}
	
	private static short deserializeShort(string str) {
		return Convert.ToInt16(str);
	}
	
	private static string serializeBoolean(bool b) {
		return serializeString(b ? "1" : "0");
	}
	
	private static bool deserializeBoolean(string str) {
		return (str.Equals("1"));
	}
	
	private static string serializeString(string str) {
		str = str.Replace("\\\\", "\\\\\\\\");
		
		for(int i = 0; i < 32; i++) {
			char c = (char) i;
			str = str.Replace(new string(c, 1), "\\\\" + new string(c, 1));
		}
		
		return str;
	}
	
	private static string deserializeString(string str) {
		str = str.Replace("\\\\\\\\", "\\\\");
		
		for(int i = 0; i < 32; i++) {
			char c = (char) i;
			str = str.Replace("\\\\" + new string(c, 1), new string(c, 1));
		}
		
		return str;
	}
}
