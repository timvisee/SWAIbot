#include "SWAIPacketHandler.h"

// Initialize class members
Stream& SWAIPacketHandler::compStream = Serial;
Stream& SWAIPacketHandler::robotStream = Serial;
bool SWAIPacketHandler::compSkipNext = false;
String SWAIPacketHandler::compBuff = "";
bool SWAIPacketHandler::robotSkipNext = false;
String SWAIPacketHandler::robotBuff = "";

void SWAIPacketHandler::setComputerStream(Stream& s) {
	SWAIPacketHandler::compStream = s;
}

void SWAIPacketHandler::setRobotStream(Stream& s) {
	SWAIPacketHandler::robotStream = s;
}

void SWAIPacketHandler::computerSendPacket(SWAIPacket p) {
	// Serialize and send the packet
	SWAIPacketHandler::computerSendPacket(SWAIProtocol::serialize(p));
}

void SWAIPacketHandler::computerSendPacket(String p) {
    // Set the status of the activity LED
	sm.setActivityStatus(SWAIStatusManager::ACTIVITY_BLINK);

	// Convert the serialized packet into a char array
	char *charArr=new char[p.length()+1];
	charArr[p.length()]=0;
	memcpy(charArr,p.c_str(),p.length());

	// Serialize and send the packet
	SWAIPacketHandler::compStream.write(charArr);
	
    // Disable the activity LED
	sm.setActivityStatus(SWAIStatusManager::ACTIVITY_OFF);
}

void SWAIPacketHandler::computerReceive(char c) {  
    if(SWAIPacketHandler::compSkipNext) {
        SWAIPacketHandler::compBuff += c;
        SWAIPacketHandler::compSkipNext = false;
                
    } else {
		// Check whether the next character should be skipped due to a backslash
        if(c == '\\' && !SWAIPacketHandler::compSkipNext)
            SWAIPacketHandler::compSkipNext = true;
                   
		// Check whether the current packet is ending
        if(c == SWAIProtocol::CHAR_PACKET_END && !SWAIPacketHandler::compSkipNext) {
            if(SWAIPacketHandler::computerReceivedPacket(SWAIProtocol::deserialize(compBuff)))
				SWAIPacketHandler::robotSendPacket(SWAIPacketHandler::compBuff);
            SWAIPacketHandler::compBuff = "";
        }
                
		// Check whether a new packet is beginning
        if(c == SWAIProtocol::CHAR_PACKET_BEGIN && !SWAIPacketHandler::compSkipNext)
            SWAIPacketHandler::compBuff = "";
    }
}

bool SWAIPacketHandler::computerReceivedPacket(SWAIPacket p) {
	switch(p.getPacketType()) {
	case SWAIProtocol::PACKET_TYPE_CONNECT_REQ:
		// Send a connection confirm packet
		SWAIPacketHandler::computerSendPacket(SWAIPacket(0, SWAIProtocol::PACKET_TYPE_CONNECT));

	case SWAIProtocol::PACKET_TYPE_CONNECT:
		// Set the connected state to true
		SWAIConnectionManager::setComputerConnected(true);
		return false;
		
	case SWAIProtocol::PACKET_TYPE_DISCONNECT_REC:
		// Send a connection confirm packet
		SWAIPacketHandler::computerSendPacket(SWAIPacket(0, SWAIProtocol::PACKET_TYPE_DISCONNECT));

	case SWAIProtocol::PACKET_TYPE_DISCONNECT:
		// Set the connected state to false
		SWAIConnectionManager::setComputerConnected(false);
		break;

	default:
		break;
	}
	
	return true;
}

void SWAIPacketHandler::robotSendPacket(SWAIPacket p) {
	// Serialize and send the packet
	SWAIPacketHandler::robotSendPacket(SWAIProtocol::serialize(p));
}

void SWAIPacketHandler::robotSendPacket(String p) {
    // Set the status of the activity LED
	sm.setActivityStatus(SWAIStatusManager::ACTIVITY_BLINK);

	// Convert the serialized packet into a char array
	char *charArr=new char[p.length()+1];
	charArr[p.length()]=0;
	memcpy(charArr,p.c_str(),p.length());

	// Serialize and send the packet
	SWAIPacketHandler::robotStream.write(charArr);
	
    // Disable the activity LED
	sm.setActivityStatus(SWAIStatusManager::ACTIVITY_OFF);
}

void SWAIPacketHandler::robotReceive(char c) {  
    if(SWAIPacketHandler::robotSkipNext) {
        SWAIPacketHandler::robotBuff += c;
        SWAIPacketHandler::robotSkipNext = false;
                
    } else {
		// Check whether the next character should be skipped due to a backslash
        if(c == '\\')
            SWAIPacketHandler::robotSkipNext = true;
                   
		// Check whether the current packet is ending
        if(c == SWAIProtocol::CHAR_PACKET_END) {
            if(SWAIPacketHandler::robotReceivedPacket(SWAIProtocol::deserialize(robotBuff)))
				computerSendPacket(SWAIPacketHandler::robotBuff);
            SWAIPacketHandler::robotBuff = "";
        }
                
		// Check whether a new packet is beginning
        if(c == SWAIProtocol::CHAR_PACKET_BEGIN)
            SWAIPacketHandler::robotBuff = "";
    }
}

bool SWAIPacketHandler::robotReceivedPacket(SWAIPacket p) {
	switch(p.getPacketType()) {
	case SWAIProtocol::PACKET_TYPE_CONNECT_REQ:
		// Send a connection confirm packet
		SWAIPacketHandler::robotSendPacket(SWAIPacket(0, SWAIProtocol::PACKET_TYPE_CONNECT));

	case SWAIProtocol::PACKET_TYPE_CONNECT:
		// Set the connected state to true
		SWAIConnectionManager::setRobotConnected(true);
		return false;
		
	case SWAIProtocol::PACKET_TYPE_DISCONNECT_REC:
		// Send a connection confirm packet
		SWAIPacketHandler::robotSendPacket(SWAIPacket(0, SWAIProtocol::PACKET_TYPE_DISCONNECT));

	case SWAIProtocol::PACKET_TYPE_DISCONNECT:
		// Set the connected state to false
		SWAIConnectionManager::setRobotConnected(false);
		break;

	default:
		break;
	}

	return true;
}