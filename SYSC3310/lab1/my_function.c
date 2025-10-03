#include "my_function.h"
#include <stdint.h>

static int x;
static uint8_t y;

void doubling(void) {
	x = 15;
	x = x * 2;
}

void process(void) {
	y = 68;
	y = y << 1;
	y = y | 4;
	y = y & 4;
}
