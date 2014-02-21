#include "SWAIConnectionManager.h"

// Initialize class members
bool SWAIConnectionManager::compCon = false;
bool SWAIConnectionManager::robotCon = false;

bool SWAIConnectionManager::isComputerConnected() {
	return compCon;
}

void SWAIConnectionManager::setComputerConnected(bool c) {
	compCon = c;
}

bool SWAIConnectionManager::isRobotConnected() {
	return robotCon;
}

void SWAIConnectionManager::setRobotConnected(bool c) {
	robotCon = c;
}