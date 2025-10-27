#include "msp.h"
#include <stdint.h>
#include "isr.h"

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
    P1ES |= (uint8_t)((1<<1)|(1<<4));

    //clearing flag register
    P1IFG &= (uint8_t)(~((1<<1)|(1<<4)));

    //enable interrupt for pin
    P1IE |= (uint8_t)((1<<1)|(1<<4));

    //set priority
    NVIC_SetPriority(PORT1_IRQn, 2);

    //clean any pending interrupt for port 1
    NVIC_ICR1 |= 0x00000008;

    //enable interrupts in NVIC
    NVIC_EnableIRQ(PORT1, IRQn);

    //enable interrupts globally
    __ASM("CPSIE I");

    while (1) {}
}
