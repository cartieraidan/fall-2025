#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

volatile static bool LEDstate = true; //default is RED LED = true
volatile static bool loopLED = false;
volatile static bool debouced = false;

void PORT1_IRQHandler(void) {

    T32CONTROL1 |= (uint32_t)(1<<7); //enable timer for debounce
    
    if ((P1IFG & (uint8_t)(1<<1)) != 0) { //toggling LEDs
        P1IFG &= (uint8_t)(~(1<<1)); //clearing flag register
        LEDstate = (LEDstate) ? false : true;
        
    } else if ((P1IFG & (uint8_t)(1<<4)) != 0) { //looping through LEDs
        P1IFG &= (uint8_t)(~(1<<4)); //clearing flag register
        loopLED = (loopLED) ? false : true; //toggling off and on

    }
}

void T32_INT1_IRQHandler(void) {
    
}
