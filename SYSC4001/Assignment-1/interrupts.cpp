
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
    std::int32_t activity_time = 40;
    std::int32_t iret_time = 1;
    std::int32_t switch_mode = 1;

    /******************************************************************/

    //parse each line of the input trace file
    while(std::getline(input_file, trace)) {
        auto [activity, duration_intr] = parse_trace(trace);

        /******************ADD YOUR SIMULATION CODE HERE*************************/
        //std::cout << activity << ", " << duration_intr << std::endl; //this just print out to the terminal

        if ((activity == "SYSCALL") || (activity == "END_IO")) { //instructions for SYSCALL or END_IO

            //error checking if duration is invalid or bigger then vector or device table
            if (duration_intr < 0 || duration_intr >= vectors.size() || duration_intr >= delays.size()) {
                std::cerr << "Invalid device number: " << duration_intr << std::endl;
                continue;
            }

            //deault interrupt boiler plate defined in .h
            auto [temp_execution, temp_time] = intr_boilerplate(time, duration_intr, context_save_restore_time, vectors);
            time = temp_time; //updating time
            execution.append(temp_execution); //add line from intr_boilerplate

            if (activity == "SYSCALL") {
                //each SYSCALL runs 2 activities with predefined time
                execution.append(std::to_string(time) + ", " + std::to_string(activity_time) + ", SYSCALL: run the ISR (device driver)\n");
                time += activity_time;

                execution.append(std::to_string(time) + ", " + std::to_string(activity_time) + ", transfer data from device to memory\n");
                time += activity_time;

                //getting delay time from device time
                int delay_time = delays[duration_intr];
                
                //add to output and increment time
                execution.append(std::to_string(time) + ", " + std::to_string(delay_time) + ", check for errors\n");
                time += delay_time;

            } else if (activity == "END_IO") {
                //each END_IO has 1 activity
                execution.append(std::to_string(time) + ", " + std::to_string(activity_time) + ", ENDIO: run the ISR (device driver)\n");
                time += activity_time;

                //getting delay time from device time
                int delay_time = delays[duration_intr];
                
                //add to output and increment time
                execution.append(std::to_string(time) + ", " + std::to_string(delay_time) + ", check device status\n");
                time += delay_time;

                //end process IRET and switch to user mode
                execution.append(std::to_string(time) + ", " + std::to_string(iret_time) + ", IRET\n");
                time += iret_time;

                execution.append(std::to_string(time) + ", " + std::to_string(switch_mode) + ", switch to user mode\n");
                time += switch_mode;
            }
            

        } else if (activity == "CPU") {
            //single cpu burst
            execution.append(std::to_string(time) + ", " + std::to_string(duration_intr) + ", CPU Burst\n");
            time += duration_intr;
        }


        /************************************************************************/

    }

    //close file
    input_file.close();

    //write output to execution.txt
    write_output(execution);

    return 0;
}
