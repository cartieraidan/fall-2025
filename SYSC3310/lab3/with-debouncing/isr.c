
#include "msp.h"
#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

#define DEBOUNCE_VALUE 50000

extern volatile bool LEDstate; //default is RED LED = true
extern volatile bool loopLED;
volatile bool debounced = false;
volatile bool servicing = false;

void PORT1_IRQHandler(void) {

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

	if (!servicing) {
			servicing = true;
			P1IE &= (uint8_t)(~((1<<1)|(1<<4))); //disable interrupt for pin
			TIMER32_1->CONTROL |= TIMER32_CONTROL_ENABLE; //enable timer for debounce
		}
}

void T32_INT1_IRQHandler(void) {
	
	loopLED = (loopLED) ? false : true;

    TIMER32_1->CONTROL &= (uint32_t)(~(1<<7)); //disable timer

    TIMER32_1->CONTROL = (uint32_t)(1); //clear timer interrupt

    TIMER32_1->LOAD = (uint32_t)DEBOUNCE_VALUE; //reset timer count
    
    debounced = true; //finished debouncing
	servicing = false;

    P1IE |= (uint8_t)((1<<1)|(1<<4)); //re-enable interrupt for pin
   
}
