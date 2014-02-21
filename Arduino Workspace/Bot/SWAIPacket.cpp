#include "SWAIPacket.h"

SWAIPacket::SWAIPacket(int targetDeviceId, int packetType) {
	this->targetDeviceId = targetDeviceId;
	this->packetType = packetType;
}

SWAIPacket::SWAIPacket(int targetDeviceId, int packetType, 
		std::vector<int> ints,
		std::vector<bool> bools,
		std::vector<String> strs) {
	this->targetDeviceId = targetDeviceId;
	this->packetType = packetType;
	this->ints = ints;
	this->bools = bools;
	this->strs = strs;
}

int SWAIPacket::getTargetDeviceId() {
	return this->targetDeviceId;
}

void SWAIPacket::setTargetDeviceId(int id) {
	this->targetDeviceId = id;
}

int SWAIPacket::getPacketType() {
	return this->packetType;
}

void SWAIPacket::setPacketType(int type) {
	this->packetType = type;
}

std::vector<int> SWAIPacket::getIntegers() {
	return this->ints;
}

int SWAIPacket::getIntegersCount() {
	return this->ints.size();
}

bool SWAIPacket::hasIntegers() {
	return (getIntegersCount() > 0);
}

void SWAIPacket::setIntegers(std::vector<int> ints) {
	this->ints = ints;
}

std::vector<bool> SWAIPacket::getBooleans() {
	return this->bools;
}

int SWAIPacket::getBooleansCount() {
	return this->bools.size();
}

bool SWAIPacket::hasBooleans() {
	return (getBooleansCount() > 0);
}

void SWAIPacket::setBooleans(std::vector<bool> bools) {
	this->bools = bools;
}

std::vector<String> SWAIPacket::getStrings() {
	return this->strs;
}

int SWAIPacket::getStringsCount() {
	return this->strs.size();
}

bool SWAIPacket::hasStrings() {
	return (getStringsCount() > 0);
}

void SWAIPacket::setStrings(std::vector<String> strs) {
	this->strs = strs;
}

int SWAIPacket::getArrayCount() {
	// Count amount of arrays
	int arrCount = 0;
	if(hasIntegers() > 0)
		arrCount++;
	if(hasBooleans() > 0)
		arrCount++;
	if(hasStrings() > 0)
		arrCount++;

	// Return array count
	return arrCount;
}