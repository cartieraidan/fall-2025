#include "msp.h"
#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

extern volatile bool LEDstate = true; //default is RED LED = true
extern volatile bool loopLED = false;

void PORT1_IRQHandler(void) {
    //without debouncing
    if ((P1IFG & (uint8_t)(1<<1)) != 0) { //toggling LEDs
        P1IFG &= (uint8_t)(~(1<<1)); //clearing flag register
        LEDstate = (LEDstate) ? false : true;
        
    } else if ((P1IFG & (uint8_t)(1<<4)) != 0) { //looping through LEDs
        P1IFG &= (uint8_t)(~(1<<4)); //clearing flag register
        loopLED = (loopLED) ? false : true; //toggling off and on

    }
}
