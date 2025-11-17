
static volatile bool LEDstate = true; //default RED LED = true
static uint16_t modes[] = {300, 600, 0, 150}; //regular, slow, off, fast
static volatile int index = 0;
static uint8_t RGBoverflow = ((1<<0)|(1<<1)|(1<<2)); //00000111
static volatile uint8_t RGBstate = (1<<0);
static volatile int count = 0;
static volatile uint16_t mm = 30000;

void PORT1_IRQHandler(void) {
  
    if ((P1IFG & (uint8_t)(1<<1)) != 0) { //toggling LEDs
        P1IFG &= (uint8_t)(~(1<<1)); //clear flag
        LEDstate = (LEDstate) ? false : true; 
      
    } else if ((P1IFG & (uint8_t)(1<<4)) != 0) { //chaning modes
        P1IFG &= (uint8_t)(~(1<<4)); //clear flag

        //resets if overflow
        index = (index + 1) % (sizeof(modes) / sizeof(modes[0]));

        if (index == 0) { //regular mode
          //TA0CCR0 = modes[index];
					mm = 30000;

        } else if (index == 1) { //slow mode
          //TA0CCR0 = modes[index];
					mm = 60000;

        } else if (index == 2) { //off mode
            TA0CTL &= (uint8_t)(~((1<<4)|(1<<5))); //turn off timer
            if (LEDstate) {
                P1OUT &= (uint8_t)(~(1<<0)); //turn off RED LED
            } else {
                P2OUT &= (uint8_t)(~((1<<0)|(1<<1)|(1<<2))); //turn off RGB
            }
          
        } else if (index == 3) { //fast mode
            //TA0CCR0 = modes[index]; //set new limit
						mm = 15000;
    
        }

		//if timer is off
		if (!(TA0CTL & (uint8_t)((1<<4)|(1<<5))) && index != 2) { //(TA0CTL & MC__STOP) == 0?
			//set to up mode
			TA0CTL |= (uint8_t)(1<<4); //TA0CTL |= MC__UP;?
			
		}
      

    }
}

void TA0_N_IRQHandler(void) {
  TA0CTL &= (uint8_t)(~(1<<0));
	count++;
	//TA0CCR0 = modes[index];
	TA0CCR0 = mm;
  
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



