#ifndef SWAIPacketHandler_h
#define SWAIPacketHandler_h

#include "Arduino.h"
#include "StandardCplusplus.h"
#include "Globals.h"
#include "SWAIPacket.h"
#include "SWAIProtocol.h"
#include "SWAIConnectionManager.h"
#include <vector>

class SWAIPacketHandler {
public:
	static void setStream(Stream& s);
	static void sendPacket(SWAIPacket p);
	static void sendPacket(String p);
	static void receive(char c);
	static void receivedPacket(SWAIPacket p);

private:
	static Stream& stream;
	static bool skipNext;
	static String buff;
};

#endif
