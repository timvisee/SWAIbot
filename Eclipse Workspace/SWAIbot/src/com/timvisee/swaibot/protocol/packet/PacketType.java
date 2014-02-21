package com.timvisee.swaibot.protocol.packet;

public enum PacketType {
	
	UNKNOWN(0),
	HEARTHBEAT(1),
	PING(2),
	SET_MOTOR(4),
	MOTOR_STATUS(5);
	
	int id;
	
	/**
	 * Constructor
	 * @param id Packet type ID
	 */
	PacketType(int id) {
		this.id = id;
	}
	
	/**
	 * Get the packet type ID
	 * @return Packet type ID
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Get a packet type by ID (Function might be heavy!)
	 * @param id Id of the packet type to get
	 * @return Packet type, or unknown packet type if no match was found
	 */
	public PacketType fromId(int id) {
		// Try to find a match with any of the available packet types
		for(PacketType pt : values())
			if(pt.getId() == id)
				return pt;
		
		// Unknown packet type, return unknown type
		return UNKNOWN;
	}
}
