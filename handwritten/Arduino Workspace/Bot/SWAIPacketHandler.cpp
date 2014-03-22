#include "SWAIPacketHandler.h"

// Initialize class members
Stream& SWAIPacketHandler::stream = Serial;
bool SWAIPacketHandler::skipNext = false;
String SWAIPacketHandler::buff = "";

void SWAIPacketHandler::setStream(Stream& s) {
	SWAIPacketHandler::stream = s;
}

void SWAIPacketHandler::sendPacket(SWAIPacket p) {
	SWAIPacketHandler::sendPacket(SWAIProtocol::serialize(p));
}

void SWAIPacketHandler::sendPacket(const String p) {
	// TODO: Handle and manage status LED activity in a separate handler class!
    // Enable the status LED
    digitalWrite(12, HIGH);

	// Convert the serialized packet into a char array
	char *charArr=new char[p.length()+1];
	charArr[p.length()]=0;
	memcpy(charArr,p.c_str(),p.length());

	// Serialize and send the packet
	SWAIPacketHandler::stream.write(charArr);
	
    // Disable the status LED
    digitalWrite(12, LOW);
}

void SWAIPacketHandler::receive(char c) {
    if(SWAIPacketHandler::skipNext) {
        SWAIPacketHandler::buff += c;
        SWAIPacketHandler::skipNext = false;
                
    } else {
		// Check whether the next character should be skipped due to a backslash
        if(c == '\\')
            SWAIPacketHandler::skipNext = true;
                   
		// Check whether the current packet is ending
        if(c == SWAIProtocol::CHAR_PACKET_END) {
            SWAIPacketHandler::receivedPacket(SWAIProtocol::deserialize(buff));
            SWAIPacketHandler::buff = "";
        }
                
		// Check whether a new packet is beginning
        if(c == SWAIProtocol::CHAR_PACKET_BEGIN)
            SWAIPacketHandler::buff = "";
    }
}

void SWAIPacketHandler::receivedPacket(SWAIPacket p) {
	switch(p.getPacketType()) {
	case SWAIProtocol::PACKET_TYPE_CONNECT_REQ:
		// Send a connection confirm packet
		SWAIPacketHandler::sendPacket(SWAIPacket(0, SWAIProtocol::PACKET_TYPE_CONNECT));

	case SWAIProtocol::PACKET_TYPE_CONNECT:
		// Set the connected state to true
		SWAIConnectionManager::setConnected(true);
		break;

	case SWAIProtocol::PACKET_TYPE_MOTOR_SET:
		// Set the motor states
		for(int i = 0; i < 4; i++)
			servoTarget[i] = p.getIntegers()[i];
		break;
		
	case SWAIProtocol::PACKET_TYPE_DISCONNECT_REC:
		// Send a connection confirm packet
		SWAIPacketHandler::sendPacket(SWAIPacket(0, SWAIProtocol::PACKET_TYPE_DISCONNECT));

	case SWAIProtocol::PACKET_TYPE_DISCONNECT:
		// Set the connected state to false
		SWAIConnectionManager::setConnected(false);
		break;

	default:
		break;
	}
}