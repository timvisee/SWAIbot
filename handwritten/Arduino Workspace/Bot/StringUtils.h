#ifndef StringUtils_h
#define StringUtils_h

#include "Arduino.h"
#include "StandardCplusplus.h"
#include <vector>

class StringUtils {
public:
    static std::vector<String> split(String s, char splitChar, bool smart);
	static int getCharacterCount(String s, char c);
};

#endif
