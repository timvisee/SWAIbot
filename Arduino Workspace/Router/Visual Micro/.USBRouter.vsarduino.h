#ifndef _VSARDUINO_H_
#define _VSARDUINO_H_
//Board = Arduino Uno
#define __AVR_ATmega328P__
#define 
#define ARDUINO 105
#define ARDUINO_MAIN
#define __AVR__
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
void setActivityLight(bool active);
void setActivityLight(bool active, bool force);
void updateActivityLight();

#include "C:\Program Files (x86)\Arduino\hardware\arduino\variants\standard\pins_arduino.h" 
#include "C:\Program Files (x86)\Arduino\hardware\arduino\cores\arduino\arduino.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\USBRouter.ino"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\PacketHandler.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\PacketHandler.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\SWAIPacket.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\SWAIPacket.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\SWAIProtocl.h"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\SWAIProtocol.cpp"
#include "C:\Users\Tim\Documents\PWS\Arduino Workspace\USBRouter\USBRouter\SWAIProtocol.h"
#endif
