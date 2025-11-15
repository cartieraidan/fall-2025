#include "msp.h"
#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

static volatile bool LEDstate = true; //default RED LED = true
static uint16_t modes[] = {30000, 60000, 0, 15000}; //regular, slow, off, fast
static volatile int index = 0;
static uint8_t RGBoverflow = ((1<<0)|(1<<1)|(1<<2)); //00000111
static volatile uint8_t RGBstate = (1<<0);

void PORT1_IRQHandler(void) {
  
    if ((P1IFG & (uint8_t)(1<<1)) != 0) { //toggling LEDs
        P1IFG &= (uint8_t)(~(1<<1)); //clear flag
        LEDstate = (LEDstate) ? false : true; 
      
    } else if ((P1IFG & (uint8_t)(1<<4)) != 0) { //chaning modes
        P1IFG &= (uint8_t)(~(1<<4)); //clear flag

        //resets if overflow
        index = (index + 1) % (sizeof(modes) / sizeof(modes[0]));

        if (index == 0) { //regular mode
          TA0CCR0 = modes[index];

        } else if (index == 1) { //slow mode
          TA0CCR0 = modes[index];

        } else if (index == 2) { //off mode
            TA0CTL &= (uint8_t)(~((1<<4)|(1<<5))); //turn off timer
            if (LEDstate) {
                P1OUT &= (uint8_t)(~(1<<0)); //turn off RED LED
            } else {
                P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2))); //turn off RGB
            }
          
        } else if (index == 3) { //fast mode
            TA0CCR0 = modes[index]; //set new limit
    
        }

		//if timer is off
		if (!(TA0CTL & (uint8_t)((1<<4)|(1<<5))) && index != 2) { //(TA0CTL & MC__STOP) == 0?
			//set to up mode
			TA0CTL |= (uint8_t)(1<<4); //TA0CTL |= MC__UP;?
			
		}
      

    }
}

void TA0_0_IRQHandler(void) {
    if (LEDstate) {
        P1OUT ^= (uint8_t)(1<<0); //toggling RED LED
    } else {
        RGBstate++; //increment state by 1
	    RGBstate &= RGBoverflow; //ensure overflow does not affect other pins

		if (RGBstate == 0) { //after roll over want to initialize to 1 not 0
			RGBstate++;
		}
	
	    P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2))); //resets pins 0, 1, 2 to 0
	    P2OUT |= RGBstate; //setting new state of pins 0, 1, 2

    }

}




