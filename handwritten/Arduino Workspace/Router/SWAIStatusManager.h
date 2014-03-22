#ifndef SWAIStatusManager_h
#define SWAIStatusManager_h

#import "Arduino.h"

class SWAIStatusManager {
public:
	enum PowerState {
		POWER_OFF,
		POWER_BLINK,
		POWER_ON
	};
	enum ConnectionState {
		CONNECTION_OFF,
		CONNECTION_BLINK,
		CONNECTION_SEARCH,
		CONNECTION_ON
	};
	enum ActivityState {
		ACTIVITY_OFF,
		ACTIVITY_BLINK
	};

	SWAIStatusManager(int powerPin, int conPin, int activityPin);
	void init();
	void update();
	int getPowerStatus();
	void setPowerStatus(PowerState state);
	int getConnectionStatus();
	void setConnectionStatus(ConnectionState state);
	int getActivityStatus();
	void setActivityStatus(ActivityState state);
	void setActivityStatus(ActivityState state, bool force);
	void testLeds();

private:
	int powerPin;
	int conPin;
	int activityPin;
	PowerState powerState;
	ConnectionState conState;
	ActivityState activityState;
	unsigned long activityStateUntil;
	unsigned long connectionSearchOffset;
};

#endif