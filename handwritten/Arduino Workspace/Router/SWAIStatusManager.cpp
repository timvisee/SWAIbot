#include "SWAIStatusManager.h"

/**
 * Constructor
 * @param int powerPin Pin for power LED
 * @param int conPin Pin for connection LED
 * @param int activityPin Pin for activity LED
 */
SWAIStatusManager::SWAIStatusManager(int powerPin, int conPin, int activityPin) {
	// Initialize variables
	this->powerState = POWER_OFF;
	this->conState = CONNECTION_OFF;
	this->activityState = ACTIVITY_OFF;
	this->powerPin = powerPin;
	this->conPin = conPin;
	this->activityPin = activityPin;
	this->activityStateUntil = 0;
	this->connectionSearchOffset = 0;
}

/**
 * Initialize
 */
void SWAIStatusManager::init() {
	pinMode(this->powerPin, OUTPUT);
	pinMode(this->conPin, OUTPUT);
	pinMode(this->activityPin, OUTPUT);

	// Test the LEDS
	testLeds();
}

/**
 * Update the status of the LED's
 */
void SWAIStatusManager::update() {
	// Update the activity state
	if(this->activityStateUntil != 0 && this->activityStateUntil < millis()) {
		this->activityState = ACTIVITY_OFF;
		this->activityStateUntil = 0;
	}

	// Variables used to store the current LED states in
	bool powerEnabled = (this->powerState == POWER_ON);
	bool conEnabled = (this->conState == CONNECTION_ON);
	bool activityEnabled = false;

	// Check whether the current connection state is search
	if(this->conState == CONNECTION_SEARCH) {
		int t = (millis() - this->connectionSearchOffset) % 1400;
		powerEnabled = (t < 300) || (t >= 900 && t < 1200);
		conEnabled = (t >= 100 && t < 400) || (t >= 800 && t < 1100);
		activityEnabled = (t >= 200 && t < 500) || (t >= 700 && t < 1000);

	} else {
		// Handle the power LED blink state
		if(this->powerState == POWER_BLINK)
			powerEnabled = (millis() % 1000) < 500;

		// Handle the connection LED blink state
		if(this->conState == CONNECTION_BLINK)
			conEnabled = (millis() % 1000) < 500;

		if(this->activityState == ACTIVITY_BLINK)
			activityEnabled = (millis() % 100) < 50;
	}

	// Update the LED's
	digitalWrite(this->powerPin, powerEnabled ? HIGH : LOW);
	digitalWrite(this->conPin, conEnabled ? HIGH : LOW);
	digitalWrite(this->activityPin, activityEnabled ? HIGH : LOW);
}

/**
 * Get the power LED state
 */
int SWAIStatusManager::getPowerStatus() {
	return this->powerState;
}

/**
 * Set the power LED state
 * @param int status New power LED state
 */
void SWAIStatusManager::setPowerStatus(PowerState state) {
	this->powerState = state;
}

/**
 * Get the connectoin LED state
 */
int SWAIStatusManager::getConnectionStatus() {
	return this->conState;
}

/**
 * Set the connection LED state
 * @param int status New connection LED state
 */
void SWAIStatusManager::setConnectionStatus(ConnectionState state) {
	if(this->conState != CONNECTION_SEARCH && state == CONNECTION_SEARCH)
		this->connectionSearchOffset = millis();

	this->conState = state;
}

/**
 * Get the activity LED state
 */
int SWAIStatusManager::getActivityStatus() {
	return this->activityState;
}

/**
 * Set the acitivity LED state
 * @param int status New activity LED status
 */
void SWAIStatusManager::setActivityStatus(ActivityState state) {
	setActivityStatus(state, false);
}

/**
 * Set the acitivity LED state
 * @param int status New activity LED status
 * @param bool force True to force the new state
 */
void SWAIStatusManager::setActivityStatus(ActivityState state, bool force) {
	// Update the state
	if(state != ACTIVITY_OFF)
		this->activityState = state;

	if(state == ACTIVITY_OFF) {
		// Check whether the new state should be forced
		if(!force)
			this->activityStateUntil = millis() + 200;
		else
			this->activityStateUntil = 0;
	} else if(state == ACTIVITY_BLINK)
		this->activityStateUntil = 0;
}

/**
 * Test each LED. Has to be initialized!
 */
void SWAIStatusManager::testLeds() {
	// Turn off each LED
	digitalWrite(this->powerPin, LOW);
	digitalWrite(this->conPin, LOW);
	digitalWrite(this->activityPin, LOW);

	digitalWrite(this->powerPin, HIGH);
	delay(100);
	digitalWrite(this->conPin, HIGH);
	delay(100);
	digitalWrite(this->activityPin, HIGH);
	delay(100);
	digitalWrite(this->powerPin, LOW);
	delay(100);
	digitalWrite(this->conPin, LOW);
	delay(100);
	digitalWrite(this->activityPin, LOW);
	delay(500);
}