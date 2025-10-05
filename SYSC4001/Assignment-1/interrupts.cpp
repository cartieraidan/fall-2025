/**
 *
 * @file interrupts.cpp
 * @author Sasisekhar Govind
 *
 */

#include<interrupts.hpp>

int main(int argc, char** argv) {

    //vectors is a C++ std::vector of strings that contain the address of the ISR
    //delays  is a C++ std::vector of ints that contain the delays of each device
    //the index of these elemens is the device number, starting from 0
    auto [vectors, delays] = parse_args(argc, argv);
    std::ifstream input_file(argv[1]);

    std::string trace;      //!< string to store single line of trace file
    std::string execution;  //!< string to accumulate the execution output

    /******************ADD YOUR VARIABLES HERE*************************/

    std::int32_t time = 0;
    std::int32_t context_save_restore_time = 10;

    /******************************************************************/

    //parse each line of the input trace file
    while(std::getline(input_file, trace)) {
        auto [activity, duration_intr] = parse_trace(trace);

        /******************ADD YOUR SIMULATION CODE HERE*************************/
        //std::cout << activity << ", " << duration_intr << std::endl; //this just print out to the terminal

        if ((activity == "SYSCALL") or (activity == "END_IO")) {
            //execution.append("CPU Command\n");
            
            auto [temp_execution, temp_time] = intr_boilerplate(time, duration_intr, context_save_restore_time, vectors);

            time = temp_time;
            execution.append(temp_execution);
            
        } else if (activity == "CPU") {
         
            execution.append(std::to_string(time) + ", " + std::to_string(duration_intr) + ", CPU Burst\n");
            time += duration_intr;
        }


        /************************************************************************/

    }

    input_file.close();

    write_output(execution);

    return 0;
}
