#ifndef SWAIConnectionManager_h
#define SWAIConnectionManager_h

#include "Arduino.h"1
#include "SWAIPacket.h"
#include "StringUtils.h"
#include "Stream.h"
#include "StandardCplusplus.h"
#include <SoftwareSerial.h>
#include <vector>

class SWAIConnectionManager {
public:
	static bool isComputerConnected();
	static void setComputerConnected(bool c);
    static bool isRobotConnected();
	static void setRobotConnected(bool c);

private:
	static bool compCon;
	static bool robotCon;
};

#endif
