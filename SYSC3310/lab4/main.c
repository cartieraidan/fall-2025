#include "msp.h"
#include <stdint.h>
#include "isr.h"
#include <stdbool.h>

int main() {
	
	//disable watchdog timer
	WDT_A->CTL = WDT_A_CTL_PW | WDT_A_CTL_HOLD;
	
	//config P1.1 & P1.4
	
	//GPIO function
	P1SEL0 &= (uint8_t)(~((1<<1)|(1<<4)));
	P1SEL1 &= (uint8_t)(~((1<<1)|(1<<4)));
	
	//set as inputs
	P1DIR |= (uint8_t)(~((1<<1)|(1<<4)));
	
	//set pull up
	P1OUT |= (uint8_t)((1<<1)|(1<<4));
	
	//enable internal resistors
	P1REN |= (uint8_t)((1<<1)|(1<<4));
	
	//config P1.0 as output
	
	//GPIO function
	P1SEL0 &= (uint8_t)(~(1<<0));
	P1SEL1 &= (uint8_t)(~(1<<0));
	
	//set as output
	P1DIR |= (uint8_t)(1<<0);
	
	//clear pin
	P1OUT &= (uint8_t)(~(1<<0));
	
	//config P2.0 P2.1 P2.2 as output
	
	//GPIO function
	P2SEL0 &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
	P2SEL1 &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
	
	//set as output
	P2DIR |= (uint8_t)((1<<0)|(1<<1)|(1<<2));
	
	//clear pin
	P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2)));
	
	//turn LED off
	//P2OUT |= (uint8_t)((1<<0)|(1<<1)|(1<<2)); //don't know if this is correct
	
	//config interrupt for P1.1 & P1.4
	
	//set interrupt as falling edge
	P1IES |= (uint8_t)((1<<1)|(1<<4));
	
	//clearing flag register
	P1IFG &= (uint8_t)(~((1<<1)|(1<<4)));
	
	//enable interrupt for pin
	P1IE |= (uint8_t)((1<<1)|(1<<4));
	
	//timer config
	//set mode off
	TA0CTL &= (uint8_t)(~((1<<4)|(1<<5)));
	
	//set source to TAxCLK
	//TA0CTL &= (uint8_t)(~((1<<8)|(1<<9)));
	
	//enable interrupt
	TA0CTL |= (uint8_t)(1<<1);
	
	//clear TAIFG
	TA0CTL &= (uint8_t)(~(1<<0));
	
	//setting divider to /2
	TA0CTL &= (uint8_t)(~((1<<6)|(1<<7)));
	TA0CTL |= (uint8_t)((1<<6)|(1<<7));
	
	//set limit for timer
	TA0CCR0 = 40000;
	
	//set priority
	NVIC_SetPriority(TA0_0_IRQn, 2);
	//NVIC_SetPriority(TA0_N_IRQn, 2);
	NVIC_SetPriority(PORT1_IRQn, 2);
	
	//clear pending interrupt
	NVIC_ClearPendingIRQ(TA0_0_IRQn);
	//NVIC_ClearPendingIRQ(TA0_N_IRQn);
	NVIC_ClearPendingIRQ(PORT1_IRQn);
	
	//enable interrupts in NVIC
	NVIC_EnableIRQ(PORT1_IRQn);
	NVIC_EnableIRQ(TA0_0_IRQn);
	//NVIC_EnableIRQ(TA0_N_IRQn);
	
	__ASM("CPSIE I");


	while (1) { }
}

