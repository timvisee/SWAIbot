#include <SoftwareSerial.h>

// Pin constants
const int STATUS_PIN = 13;
const int ANTENNA_STREAM_RX = 2;
const int ANTENNA_STREAM_TX = 3;

// Data streams
SoftwareSerial router(ANTENNA_STREAM_RX, ANTENNA_STREAM_TX);
 
/**
 * Called on start
 */
void setup() {
	// Set the proper pin modes
	pinMode(STATUS_PIN, OUTPUT);

	// Start the serial connections
	Serial.begin(9600);
	router.begin(9600);
}

/**
 * Called once each loop
 */
void loop() {
	// Check whether there's any data available on the computer or antenna side, if so, handle the data
	while(router.available() || Serial.available()) {
		// Handle data comming from the router antenna if available
		if(router.available()) {
			// Enable the status LED
			digitalWrite(STATUS_PIN, HIGH);

			// Route the data
			Serial.write(router.read());
		}

		// Handle data comming from the computer if available
		if(Serial.available()) {
			// Enable the status LED
			digitalWrite(STATUS_PIN, HIGH);

			// Route the data
			router.write(Serial.read());
		}
	}

	// Disable the status LED
	digitalWrite(STATUS_PIN, LOW);
}