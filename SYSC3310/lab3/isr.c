#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

static bool LEDstate = true; //default is RED LED = true
static bool loopLED = false;
static uint8_t RGBoverflow = ((1<<0)|(1<<1)|(1<<2)); //00000111
static uint8_t RGBstate = (0<<0);
static int loopCount = 0;

void PORT1_IRQHandler(void) {
    //without debouncing
    if (P1IFG & (uint8_t)(1<<1)) { //toggling LEDs
        P1IFG &= (uint8_t)(~(1<<1)); //clearing flag register
        LEDstate = (LEDstate) ? false : true;
        
    } else if (P1IFG & (uint8_t)(1<<4)) { //looping through LEDs
        P1IFG &= (uint8_t)(~(1<<4)); //clearing flag register
        loopLED = (loopLED) ? false : true; //toggling off and on

    }
}
