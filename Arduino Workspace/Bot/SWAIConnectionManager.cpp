#include "SWAIConnectionManager.h"

bool SWAIConnectionManager::con = false;

bool SWAIConnectionManager::isConnected() {
	return SWAIConnectionManager::con;
}

void SWAIConnectionManager::setConnected(bool c) {
	SWAIConnectionManager::con = c;
}