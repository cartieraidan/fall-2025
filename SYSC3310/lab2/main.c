#include "my_function.h"
#include "msp.h"

static int i;
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
			//pin 1 pressed
		}
		else {
			//pin 4 pressed
		}
		
	}
	
	return 0;
}
