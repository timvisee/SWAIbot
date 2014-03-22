#include <utility.h>
#include <system_configuration.h>
#include <StandardCplusplus.h>
#include <SoftwareSerial.h>
#include "Globals.h"
#include "SWAIPacket.h"
#include "SWAIPacketHandler.h"
#include "SWAIProtocol.h"
#include "SWAIStatusManager.h"
#include "SWAIConnectionManager.h"

// Pin constants
const int ANTENNA_STREAM_RX = 3;
const int ANTENNA_STREAM_TX = 2;
const int LED_POWER_PIN = 10;
const int LED_CONNECTION_PIN = 11;
const int LED_ACTIVITY_PIN = 12;

// LED Status manager
SWAIStatusManager sm(LED_POWER_PIN, LED_CONNECTION_PIN, LED_ACTIVITY_PIN);

// Data streams
Stream& computerStream = Serial;
SoftwareSerial robotStream(ANTENNA_STREAM_RX, ANTENNA_STREAM_TX);

void setup() {
	// Initialize the status manager
	sm.init();
	sm.setPowerStatus(SWAIStatusManager::POWER_BLINK);
	sm.update();

	// Start the data streams
	Serial.begin(9600);
	robotStream.begin(9600);

	// Set the data stream instances in the packet handler
	SWAIPacketHandler::setComputerStream(Serial);
	SWAIPacketHandler::setRobotStream(robotStream);

	// Wait for the data streams to fully initialize (takes 1 second)
	sm.setPowerStatus(SWAIStatusManager::POWER_BLINK);
	long initAt = millis() + 1000;
	while(initAt > millis())
		sm.update();
	sm.setPowerStatus(SWAIStatusManager::POWER_ON);
}

void loop() {
	// Check robot connection: Ask the robot to connect (wait until connected)
	if(!SWAIConnectionManager::isComputerConnected()) {
		// Set the proper connection status
		sm.setConnectionStatus(SWAIStatusManager::CONNECTION_BLINK);

		// Wait for the robot to connect
		while(!SWAIConnectionManager::isComputerConnected()) {
			// Handle all received data
			handleReceivedData();

			// Update the status lights
			sm.update();
		}

		// Set the proper status lights
		sm.setConnectionStatus(SWAIStatusManager::CONNECTION_ON);
	}

	// Check robot connection: Ask the robot to connect (wait until connected)
	if(!SWAIConnectionManager::isRobotConnected()) {
		// Set the proper connection status
		sm.setConnectionStatus(SWAIStatusManager::CONNECTION_SEARCH);

		// Remember the last request time
		long lastRequest = 0;

		// Wait for the robot to connect
		while(!SWAIConnectionManager::isRobotConnected()) {
			// Send a connection request to the robot each 3 seconds
			if(lastRequest + 3000 <= millis()) {
				// Reset the timer
				lastRequest = millis();

				// Send a new connect request
				SWAIPacketHandler::robotSendPacket(SWAIPacket(0, SWAIProtocol::PACKET_TYPE_CONNECT_REQ));
			}

			// Handle all received data
			handleReceivedData();

			// Update the status lights
			sm.update();
		}

		// Set the proper status lights
		sm.setConnectionStatus(SWAIStatusManager::CONNECTION_ON);
	}

    // Handle new data
    handleReceivedData();

	// Route all received data if available
	/*if(robotStream.available() || computerStream.available()) {
		// Enable the activity light
		sm.setActivityStatus(SWAIStatusManager::ACTIVITY_BLINK);

		// Route all received data
		while(robotStream.available() || computerStream.available()) {
			// Route data comming from the antenna data stream
			if(robotStream.available())
				computerStream.write(robotStream.read());

			// Route data comming from the computer data stream
			if(computerStream.available())
				robotStream.write(computerStream.read());
		}
		
		// Disable the activity light
		sm.setActivityStatus(SWAIStatusManager::ACTIVITY_OFF);
	}*/

	// Reset the connection status light
	//sm.setConnectionStatus(SWAIStatusManager::CONNECTION_SEARCH);

	// Update the status lights each loop
	sm.update();
}

void handleReceivedData() {
	if(computerStream.available()) {
        // Enable the activity light
		sm.setActivityStatus(SWAIStatusManager::ACTIVITY_BLINK);
        
        // Handle the new data
    	while(computerStream.available())
			SWAIPacketHandler::computerReceive((char) computerStream.read());
        
        // Disable the activity light
		sm.setActivityStatus(SWAIStatusManager::ACTIVITY_OFF);
    }

	if(robotStream.available()) {
        // Enable the activity light
		sm.setActivityStatus(SWAIStatusManager::ACTIVITY_BLINK);
        
        // Handle the new data
    	while(robotStream.available())
			SWAIPacketHandler::robotReceive((char) robotStream.read());
        
        // Disable the activity light
		sm.setActivityStatus(SWAIStatusManager::ACTIVITY_OFF);
    }
}