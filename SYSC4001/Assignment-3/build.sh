if [ ! -d "bin" ]; then
    mkdir bin
else
	rm bin/*
fi

g++ -std=c++17 -g -O0 -I . -o bin/interrupts_EP interrupts_EP_Aidan_Sultan.cpp
g++ -std=c++17 -g -O0 -I . -o bin/interrupts_RR interrupts_RR_Aidan_Sultan.cpp
g++ -std=c++17 -g -O0 -I . -o bin/interrupts_EP_RR interrupts_EP_RR_Aidan_Sultan.cpp
