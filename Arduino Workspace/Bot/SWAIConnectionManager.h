#ifndef SWAIConnectionManager_h
#define SWAIConnectionManager_h

#include "Arduino.h"
#include "SWAIPacket.h"
#include "StringUtils.h"
#include "Stream.h"
#include "StandardCplusplus.h"
#include <SoftwareSerial.h>
#include <vector>

class SWAIConnectionManager {
public:
    static bool isConnected();
	static void setConnected(bool c);

private:
	static bool con;
};

#endif
