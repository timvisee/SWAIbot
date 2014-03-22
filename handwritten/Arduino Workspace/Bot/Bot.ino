#include <utility.h>
#include <unwind-cxx.h>
#include <system_configuration.h>
#include <StandardCplusplus.h>
#include <SoftwareSerial.h>
#include <Servo.h>
#include "Globals.h"
#include "SWAIPacket.h"
#include "SWAIProtocol.h"
#include "SWAIPacketHandler.h"
#include "SWAIConnectionManager.h"

// Create the servo enum
const int SERVO_TL = 0;
const int SERVO_TR = 1;
const int SERVO_BL = 2;
const int SERVO_BR = 3;

// Define the status LED pin
const int statusPin = 12;

// Amount of servos used
const int SERVO_COUNT = 4;
// Define the servo instances
Servo servo[SERVO_COUNT];
// Define the servo pins
const int servoPin[SERVO_COUNT] = {11, 10, 9, 8};
// Define the servo's default position
const int servoDef[SERVO_COUNT] = {90, 80, 96, 90};
// Define the servo position buffer
int servoPos[SERVO_COUNT] = {0, 0, 0, 0};
// Define the servo target tracker
int servoTarget[SERVO_COUNT] = {0, 0, 0, 0};
// If true, servos will be forced to reset
boolean forceReset = true;

// Servo update task variables
long servoUpdateTaskClock = 0;
const int servoUpdateTaskDelay = 10;
// Heartbeat task variables
long sendHeartbeatTaskClock = 0;
const int sendHeartbeatTaskDelay = 2000;
// Send status task variables
long sendStatusUpdateTaskClock = 0;
const int sendStatusUpdateTaskDelay = 1000;

// Clock for status LED timing
long statusLedUntil = 0;

// True whenever the loop() method is first called
bool firstRun = true;

/**
 * Called once on start
 */
void setup() {
    // Set the proper pin modes
    pinMode(statusPin, OUTPUT);
        
    // Set up the serial connection
    Serial.begin(9600);
    
    // Blink the status LED 3 times
    for(int i = 0; i < 3; i++) {
        digitalWrite(statusPin, HIGH);
        delay(100);
        digitalWrite(statusPin, LOW);
        delay(100);
    }
    
    // Attach each servo
    servo[SERVO_TL].attach(servoPin[SERVO_TL]);
    servo[SERVO_TR].attach(servoPin[SERVO_TR]);
    servo[SERVO_BL].attach(servoPin[SERVO_BL]);
    servo[SERVO_BR].attach(servoPin[SERVO_BR]);
    
    // Reset the position and target of each servo
    for(int servoIndex = 0; servoIndex < SERVO_COUNT; servoIndex++) {
        servoPos[servoIndex] = servoDef[servoIndex];
        servoTarget[servoIndex] = servoDef[servoIndex];
    }
    
    // Force all servo's to reset
    forceReset = true;
    
    // Reset all task clocks
    servoUpdateTaskClock = millis() + servoUpdateTaskDelay;
    sendHeartbeatTaskClock = millis() + sendHeartbeatTaskDelay;
    sendStatusUpdateTaskClock = millis() + sendStatusUpdateTaskDelay;
}

/**
 * Called once each loop
 */
void loop() {
	// Check whether this is the first run
	if(firstRun) {
		long handleServosUntil = millis() + 3000;

		// Handle the servo update task
		while(handleServosUntil > millis())
			handleServos();
	}

	// Ensure the robot is connected to a router
	if(!SWAIConnectionManager::isConnected()) {
		while(!SWAIConnectionManager::isConnected()) {
			// Handle new data
			handleReceivedData();
		}
	}
    
	// Ensure the robot is connected
	if(SWAIConnectionManager::isConnected()) {
		if(sendHeartbeatTaskClock <= millis()) {
			// Reset the send status task clock
			sendHeartbeatTaskClock = millis() + sendHeartbeatTaskDelay;
        
			// Create a heartbeat packet
			SWAIPacket p = SWAIPacket(0, SWAIProtocol::PACKET_TYPE_HEARTBEAT);

			// Send the heartbeat packet
			SWAIPacketHandler::sendPacket(p);
		}

		// Handle the send status task
		if(sendStatusUpdateTaskClock <= millis()) {
			// Reset the send status task clock
			sendStatusUpdateTaskClock = millis() + sendStatusUpdateTaskDelay;
        
			// Create a list of the current servo positions
			std::vector<int> ints;
			std::vector<bool> bools;
			std::vector<String> strs;
			ints.push_back(servoPos[SERVO_TL]);
			ints.push_back(servoPos[SERVO_TR]);
			ints.push_back(servoPos[SERVO_BL]);
			ints.push_back(servoPos[SERVO_BR]);

			// Send a packet with the current motor states
			SWAIPacket p = SWAIPacket(0, SWAIProtocol::PACKET_TYPE_HEARTBEAT, ints, bools, strs);
		}
	}

    // Handle new data
    handleReceivedData();

    // Handle the servo update task
    handleServos();
    
	// Update the status LED state based on the last activity
    digitalWrite(statusPin, (statusLedUntil > millis()) ? HIGH : LOW);

	// Reset the firstRun variable
	firstRun = false;
}

void handleReceivedData() {
	if(Serial.available()) {
        // Enable the status LED
    	digitalWrite(statusPin, HIGH);
        
        // Handle the new data
    	while(Serial.available())
			SWAIPacketHandler::receive((char) Serial.read());
        
        // Keep the status LED on for .1 second
        statusLedUntil = millis() + 50;
    }
}

void handleServos() {
	if(servoUpdateTaskClock <= millis()) {
        // Reset the servo update task clock
        servoUpdateTaskClock = millis() + servoUpdateTaskDelay;
        
        // Update all servo's
        for(int servoIndex = 0; servoIndex < SERVO_COUNT; servoIndex++) {
            if(servoPos[servoIndex] != servoTarget[servoIndex] || forceReset) {
                // Update the current position of the current servo towards the target position (if this wasn't a force reset)
                if(servoPos[servoIndex] != servoTarget[servoIndex])
                    servoPos[servoIndex] += (servoPos[servoIndex] < servoTarget[servoIndex]) ? 1 : -1;
                    
                // Rewrite the current position to the servo
                servo[servoIndex].write(servoPos[servoIndex]);
            }
        }
        
        // Every servo has been force reset
        forceReset = false;
    }
}