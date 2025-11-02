#include "msp.h"
#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

#define DEBOUNCE_VALUE 50000

volatile static bool LEDstate = true; //default is RED LED = true //can be put in PORT1 function as a local volatile static
volatile bool loopLED = false; //accessed from main.c
volatile static bool debounced = false; //needs to be global for both interrupt handlers

void PORT1_IRQHandler(void) {
    
    P1IE &= (uint8_t)(~((1<<1)|(1<<4))); //disable interrupt for pin
    
    T32CONTROL1 |= (uint32_t)(1<<7); //enable timer for debounce
    
    if (debounced) {
        if ((P1IFG & (uint8_t)(1<<1)) != 0) { //toggling LEDs
            P1IFG &= (uint8_t)(~(1<<1)); //clearing flag register
            LEDstate = (LEDstate) ? false : true;
            
        } else if ((P1IFG & (uint8_t)(1<<4)) != 0) { //looping through LEDs
            P1IFG &= (uint8_t)(~(1<<4)); //clearing flag register
            loopLED = (loopLED) ? false : true; //toggling off and on
    
        }

        debounced = false; //reset for debouncing
    }
}

void T32_INT1_IRQHandler(void) {

    T32CONTROL1 &= (uint32_t)(~(1<<7)); //disable timer

    T32INTCLR1 = (uint32_t)(1); //clear timer interrupt

    T32LOAD1 = (uint32_t)DEBOUNCE_VALUE; //reset timer count
    
    debounced = true; //finished debouncing

    P1IE |= (uint8_t)((1<<1)|(1<<4)); //re-enable interrupt for pin
   
}
