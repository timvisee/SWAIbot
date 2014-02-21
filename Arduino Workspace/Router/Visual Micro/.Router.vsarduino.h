#ifndef _VSARDUINO_H_
#define _VSARDUINO_H_
//Board = Arduino Uno
#define __AVR_ATmega328p__
#define __AVR_ATmega328P__
#define _VMDEBUG 1
#define ARDUINO 101
#define ARDUINO_MAIN
#define __AVR__
#define __avr__
#define F_CPU 16000000L
#define __cplusplus
#define __inline__
#define __asm__(x)
#define __extension__
#define __ATTR_PURE__
#define __ATTR_CONST__
#define __inline__
#define __asm__ 
#define __volatile__

#define __builtin_va_list
#define __builtin_va_start
#define __builtin_va_end
#define __DOXYGEN__
#define __attribute__(x)
#define NOINLINE __attribute__((noinline))
#define prog_void
#define PGM_VOID_P int
            
typedef unsigned char byte;
extern "C" void __cxa_pure_virtual() {;}

//
//
void handleReceivedData();

#include "C:\Program Files (x86)\Arduino\hardware\arduino\cores\arduino\arduino.h"
#include "C:\Program Files (x86)\Arduino\hardware\arduino\variants\standard\pins_arduino.h" 
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\Router.ino"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\Globals.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIConnectionManager.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIConnectionManager.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIPacket.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIPacket.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIPacketHandler.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIPacketHandler.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIProtocol.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIProtocol.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIStatusManager.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\SWAIStatusManager.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\StringUtils.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\Router\StringUtils.h"
#endif
