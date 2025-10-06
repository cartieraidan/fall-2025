#include "my_function.h"
#include "msp.h"
#include <stdbool.h>
#include <stdio.h>

#define LOOP_INTERVAL_1 4000
#define LOOP_INTERVAL_2 10000

static int i;
static bool LEDstate = true;
static uint8_t RGBoverflow = ((1<<0)|(1<<1)|(1<<2)); //00000111
static uint8_t RGBstate = (0<<0);
static bool loopState = false;
static uint16_t loopCount = 0;
static uint16_t loopInterval = LOOP_INTERVAL_1; //default 4 seconds


//comments
int main() {
	
	//disable watchdog timer
	WDT_A->CTL = WDT_A_CTL_PW | WDT_A_CTL_HOLD;
	
	//config P1.1 & P1.4
	////GPIO function
	P1SEL0 &= (uint8_t)(~((1<<1)|(1<<4)));
	P1SEL1 &= (uint8_t)(~((1<<1)|(1<<4)));
	//set output direction
	//setting pin 1 and 4 to 0 for inputs
	P1DIR &= (uint8_t)(~((1<<1)|(1<<4)));
	//set drive pins high
	P1OUT |= (uint8_t)((1<<1)|(1<<4));
	//Enable resistor for pull up
	P1REN |= (uint8_t)((1<<1)|(1<<4));
	
	//config P1.0, P2.0, P2.1, P2.2
	//GPIO function
	P1SEL0 &= (uint8_t)(~(1<<0));
	P1SEL1 &= (uint8_t)(~(1<<0));
	//setting P1.0 as output
	P1DIR |= (uint8_t)(1<<0);
	//set active low
	P1OUT &= (uint8_t)(~(1<<0));
	//GPIO function
	P2SEL0 &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
	P2SEL0 &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
	//setting P2.0, P2.1, P2,2 as outputs
	P2DIR |= (uint8_t)((1<<0)|(1<<1)|(1<<2));
	//set active low
	P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
	
	//set LED states to off
	P1OUT &= (uint8_t)(~(1<<0));
	P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
	
	//inifite loop
	while (1) {
		
		//while both input pins are high
		while ((P1IN & (uint8_t)(1<<1))&&(P1IN & (uint8_t)(1<<4)))
		{}
		//debouncing inputs
		i = 5000;
		while (i>0){i--;}
		
		if (!(P1IN & (uint8_t)(1<<1))) {
			//pin 1 pressed, change LED selected

			//RED true, RGB false
			LEDstate = !LEDstate;
		}
		else if (!(P1IN & (uint8_t)(1<<4))) {
			//pin 4 pressed

            //might suggest debouncing here if testing does not go well
            //turn on continuous state
			loopState = !loopState;

		}

        //continously change states
        loopCount++;
        if ((loopCount % loopInterval == 0)&&(loopState)) { //%4000 gussing around 4 seconds
            
            loopCount = 0;
            
            if (LEDstate) { //RED LED
                P1OUT ^= (uint8_t)(1<<0);
            } else { //RGB LED

                RGBstate++; //increment state by 1
                RGBstate &= RGBoverflow; //ensure overflow does not affect other pins

				if (RGBstate == 0) { //phase change
					loopInterval = (loopInterval == LOOP_INTERVAL_1) ? LOOP_INTERVAL_2 : LOOP_INTERVAL_1;
				}

                P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2))); //resets pins 0, 1, 2 to 0
                P2OUT |= RGBstate; //setting new state of pins 0, 1, 2
            }
        }
		
	}
	
	return 0;
}
