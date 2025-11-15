#include "msp.h"
#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

static volatile bool LEDstate = true; //default RED LED = true
static uint16_t modes[] = {30000, 60000, 0, 15000}; //regular, slow, off, fast
static volatile int index = 0;

void PORT1_IRQHandler(void) {
  
    if ((P1IFG & (uint8_t)(1<<1)) != 0) { //toggling LEDs
        P1IFG &= (uint8_t)(~(1<<1)); //clear flag
        LEDstate = (LEDstate) ? false : true; 
      
    } else if ((P1IFG & (uint8_t)(1<<4)) != 0) { //chaning modes
      P1IFG &= (uint8_t)(~(1<<4)); //clear flag
      

    }
}

void TA0_0_IRQHandler(void) {

}

