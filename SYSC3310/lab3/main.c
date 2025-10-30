#include "msp.h"
#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

static uint8_t RGBoverflow = ((1<<0)|(1<<1)|(1<<2)); //00000111
static uint8_t RGBstate = (0<<0);
static int loopCount = 0;

//comment
int main() {

    //disable watchdog timer
    WDT_A->CTL = WDT_A_CTL_PW | WDT_A_CTL_HOLD;

    //config P1.1 & P1.4

    //GPIO function (set to 0)
    P1SEL0 &= (uint8_t)(~((1<<1)|(1<<4)));
    P1SEL1 &= (uint8_t)(~((1<<1)|(1<<4)));

    //setting pins as input (set to 0)
    P1DIR &= (uint8_t)(~((1<<1)|(1<<4)));

    //set pins high (will read 0 when pressed)
    P1OUT |= (uint8_t)((1<<1)|(1<<4));

    //set inputs as pull up resistor
    P1REN |= (uint8_t)((1<<1)|(1<<4));

    //config P1.0 as output 

    //GPIO function
    P1SEL0 &= (uint8_t)(~(1<<0));
    P1SEL1 &= (uint8_t)(~(1<<0));

    //set as output
    P1DIR |= (uint8_t)(1<<0);

    //set pin active low
    P1OUT &= (uint8_t)(~(1<<0));

    //config P2.0, P2.1, P2.2 as output

    //GPIO function
    P2SEL0 &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
    P2SEL1 &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));

    //set as output
    P2DIR |= (uint8_t)((1<<0)|(1<<1)|(1<<2));

    //set active low
    P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));

    //Turn LED in off state
    P1OUT |= (uint8_t)(1<<0);
    P2OUT |= (uint8_t)((1<<0)|(1<<1)|(1<<2));

    //interrupt config for P1.1 & P1.4

    //set interrupt as falling edge
    P1IES |= (uint8_t)((1<<1)|(1<<4));

    //clearing flag register
    P1IFG &= (uint8_t)(~((1<<1)|(1<<4)));

    //enable interrupt for pin
    P1IE |= (uint8_t)((1<<1)|(1<<4));

    //set priority
		NVIC_SetPriority(PORT1_IRQn, 2);

    //clean any pending interrupt for port 1?
    //NVIC->ICPR |= (uint8_t)(1);
		//clears automatically so won't need it?

    //enable interrupts in NVIC
    NVIC_EnableIRQ(PORT1_IRQn);

    //enable interrupts globally
    __ASM("CPSIE I");

    while (1) {
		
		while (loopLED) { //since loopLED static another instance of the interrupt can turn to false breaking loop
            loopCount++;
    		if ((loopCount % 100000 == 0)&&(LEDstate)) { //for RED LED
    			loopCount = 0;
    		
    			P1OUT ^= (uint8_t)(1<<0);
    				
    		} else if ((loopCount % 400000 == 0)&&(!(LEDstate)) { //for RGB LED
                loopCount = 0;
                
                RGBstate++; //increment state by 1
                RGBstate &= RGBoverflow; //ensure overflow does not affect other pins

                P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2))); //resets pins 0, 1, 2 to 0
                P2OUT |= RGBstate; //setting new state of pins 0, 1, 2
                
            }
            
        }
	}
}
