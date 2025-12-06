
/**
 *
 * @file interrupts.cpp
 * @author Sasisekhar Govind
 *
 */

#include<interrupts.hpp>

std::tuple<std::string, std::string, int> simulate_trace(std::vector<std::string> trace_file, int time, std::vector<std::string> vectors, std::vector<int> delays, std::vector<external_file> external_files, PCB current, std::vector<PCB> wait_queue) {

    std::string trace;      //!< string to store single line of trace file
    std::string execution = "";  //!< string to accumulate the execution output
    std::string system_status = "";  //!< string to accumulate the system status output
    int current_time = time;

    //parse each line of the input trace file. 'for' loop to keep track of indices.
    for(size_t i = 0; i < trace_file.size(); i++) {
        auto trace = trace_file[i];

        auto [activity, duration_intr, program_name] = parse_trace(trace);

        if(activity == "CPU") { //As per Assignment 1
            execution += std::to_string(current_time) + ", " + std::to_string(duration_intr) + ", CPU Burst\n";
            current_time += duration_intr;
        } else if(activity == "SYSCALL") { //As per Assignment 1
            auto [intr, time] = intr_boilerplate(current_time, duration_intr, 10, vectors);
            execution += intr;
            current_time = time;

            execution += std::to_string(current_time) + ", " + std::to_string(delays[duration_intr]) + ", SYSCALL ISR (ADD STEPS HERE)\n";
            current_time += delays[duration_intr];

            execution +=  std::to_string(current_time) + ", 1, IRET\n";
            current_time += 1;
        } else if(activity == "END_IO") {
            auto [intr, time] = intr_boilerplate(current_time, duration_intr, 10, vectors);
            current_time = time;
            execution += intr;

            execution += std::to_string(current_time) + ", " + std::to_string(delays[duration_intr]) + ", ENDIO ISR(ADD STEPS HERE)\n";
            current_time += delays[duration_intr];

            execution +=  std::to_string(current_time) + ", 1, IRET\n";
            current_time += 1;
        } else if(activity == "FORK") {
            auto [intr, time] = intr_boilerplate(current_time, 2, 10, vectors);
            execution += intr;
            current_time = time;

            ///////////////////////////////////////////////////////////////////////////////////////////
            //Add your FORK output here

            //Part of cloning
            execution += std::to_string(current_time) + ", " + std::to_string(duration_intr) + ", cloning the PCB\n";
            current_time += duration_intr;

            //cloning current PCB (like init)
            PCB clone(current.PID + 1, current.PID, current.program_name, current.size, -1);
            //assign to partition
            if(!allocate_memory(&clone)) {
                std::cerr << "ERROR! Memory allocation failed!" << std::endl;
            }
            //add PCB to wait queue
            wait_queue.push_back(clone);
            //end of cloning

            //Scheduler call
            execution += std::to_string(current_time) + ", 0, scheduler called\n";

            //IRET
            execution += std::to_string(current_time) + ", 1, IRET\n";
            current_time += 1;

            
            //getting current time and parent trace
            system_status += "time: " + std::to_string(current_time) + "; current trace: " + trace_file[i] + "\n";
            //every fork take a snapshot
            system_status += print_PCB(current, wait_queue);

            ///////////////////////////////////////////////////////////////////////////////////////////

            //The following loop helps you do 2 things:
            // * Collect the trace of the chile (and only the child, skip parent)
            // * Get the index of where the parent is supposed to start executing from
            std::vector<std::string> child_trace;
            bool skip = true;
            bool exec_flag = false;
            int parent_index = 0;

            for(size_t j = i; j < trace_file.size(); j++) {
                auto [_activity, _duration, _pn] = parse_trace(trace_file[j]);
                if(skip && _activity == "IF_CHILD") {
                    skip = false;
                    continue;
                } else if(_activity == "IF_PARENT"){
                    skip = true;
                    parent_index = j;
                    if(exec_flag) {
                        break;
                    }
                } else if(skip && _activity == "ENDIF") {
                    skip = false;
                    continue;
                } else if(!skip && _activity == "EXEC") {
                    skip = true;
                    child_trace.push_back(trace_file[j]);
                    exec_flag = true;
                }

                if(!skip) {
                    child_trace.push_back(trace_file[j]);
                }
            }
            i = parent_index;

            //std::cout << i;
            

            ///////////////////////////////////////////////////////////////////////////////////////////
            //With the child's trace, run the child (HINT: think recursion)



            /* can be used for exec not fork()
            //Converting the trace file into a vector of strings.
            std::vector<std::string> child_file; //trace in programX

            auto [child_act, child_dur, extern_file] = parse_trace(child_trace[0]); //getting program name

            std::cout << extern_file << std::endl; //debug
            extern_file += ".txt";

            // Open the file
            std::ifstream file(extern_file);
            //check if the file opened
            if (!file.is_open()) {
                std::cerr << "Error: could not open program1.txt" << std::endl;
            } else {
                while (std::getline(file, trace)) {
                    child_file.push_back(trace);
                }
                file.close();
            }

            */
    
            //debug to see what child trace looks like
           // for(int j = 0; j < child_trace.size(); j++) {
           //     std::cout << child_trace[j] << std::endl;
            //}
            //now how vector trace of program1 now need to send it to simulate_trace

            //sets current child to running
            wait_queue.push_back(current);
            current = wait_queue.front();
            wait_queue.erase(wait_queue.begin());

           // std::cout << print_PCB(current, wait_queue) << std::endl;

            //runs child process
            auto [executionTemp, system_statusTemp, current_timeTemp] = simulate_trace(child_trace, current_time, vectors, delays, external_files, current, wait_queue);

            execution += executionTemp;
            current_time = current_timeTemp;
            system_status += system_statusTemp;

            ///////////////////////////////////////////////////////////////////////////////////////////


        } else if(activity == "EXEC") {
            //execution += "in EXEC for "  + current.program_name + "\n"; //debug

            auto [intr, time] = intr_boilerplate(current_time, 3, 10, vectors);
            current_time = time;
            execution += intr;

            ///////////////////////////////////////////////////////////////////////////////////////////
            //Add your EXEC output here

            //gets size of file
            int fileSize = 0;
            for (external_file file : external_files) {
                if (file.program_name == program_name) {
                    fileSize = file.size;
                }
            }

            //trace for file size
            execution += std::to_string(current_time) + ", 0, program size is " + std::to_string(fileSize) + "Mb large\n";

            //simulating loading
            execution += std::to_string(current_time) + ", " + std::to_string(15 * fileSize) + ", loading program into memory\n";
            current_time += 15 * fileSize;


            //creates a new PCB and replaces child cloned in partition
            

            if (current.partition_number != -1) {
                PCB cloneExec(current.PID, current.PPID, program_name, fileSize, -1);
                //deallocates old PCB
                PCB* toDelete = &current;
                free_memory(toDelete);

                //Finding free partition
                if(!allocate_memory(&cloneExec)) {
                    std::cerr << "ERROR! Memory allocation failed!" << std::endl;
                }
                //set current running program
                current = cloneExec;
            } else {
                PCB cloneExec(0, -1, program_name, fileSize, -1);
                //Finding free partition
                if(!allocate_memory(&cloneExec)) {
                    std::cerr << "ERROR! Memory allocation failed!" << std::endl;
                }

                //set current running program
                current = cloneExec;
            }

    

            

            //mark PCB as not empty
            //for random number
            std::random_device rd;                
            std::mt19937 gen(rd());               
            std::uniform_int_distribution<> dist(1, 10);  
            int random_num = dist(gen);
            memory[current.partition_number - 1].code = "not empty";
            execution += std::to_string(current_time) + ", " + std::to_string(random_num) + ", marking partition as occupied\n";
            current_time += random_num;

            //updating PCB
            int random_num2 = dist(gen);
            execution += std::to_string(current_time) + ", " + std::to_string(random_num2) + ", updating PCB\n";
            current_time += random_num2;

            //Scheduler call
            execution += std::to_string(current_time) + ", 0, scheduler called\n";

            //IRET
            execution += std::to_string(current_time) + ", 1, IRET\n";
            current_time += 1;


            //std::cout << print_PCB(current, wait_queue) << std::endl; //debug

            //std::cout << fileSize << std::endl; //debug

            //std::cout << memory[current.partition_number - 1].code << std::endl;

            system_status += "time: " + std::to_string(current_time) + "; current trace: " + trace_file[i] + "\n";
            //every fork take a snapshot
            system_status += print_PCB(current, wait_queue);

            ///////////////////////////////////////////////////////////////////////////////////////////


            std::ifstream exec_trace_file(program_name + ".txt");

            std::vector<std::string> exec_traces;
            std::string exec_trace;
            while(std::getline(exec_trace_file, exec_trace)) {
                exec_traces.push_back(exec_trace);
            }

            ///////////////////////////////////////////////////////////////////////////////////////////
            //With the exec's trace (i.e. trace of external program), run the exec (HINT: think recursion)

            auto [executionTemp, system_statusTemp, current_timeTemp] = simulate_trace(exec_traces, current_time, vectors, delays, external_files, current, wait_queue);

            execution += executionTemp;
            current_time = current_timeTemp;
            system_status += system_statusTemp;

            
            /* does not clear out parent
            if (current.PPID != -1) {

                std::cout << "hereeeee" << std::endl;
                for (PCB node : wait_queue) {
                    if (current.PPID == node.PID) {
                        wait_queue.erase(wait_queue.begin()); //might need to change
                        free_memory(&node);

                    }
                }
            }
            */

            free_memory(&current);

            PCB cloneExec3(0, -1, program_name, fileSize, -1);
            current = cloneExec3;

            //system_status += "time: " + std::to_string(current_time) + "; current trace: " + trace_file[i] + "\n";
            //every fork take a snapshot
            //system_status += print_PCB(current, wait_queue);

            ///////////////////////////////////////////////////////////////////////////////////////////

            break; //Why is this important? (answer in report)

        }
    }

    return {execution, system_status, current_time};
}

int main(int argc, char** argv) {

    //vectors is a C++ std::vector of strings that contain the address of the ISR
    //delays  is a C++ std::vector of ints that contain the delays of each device
    //the index of these elemens is the device number, starting from 0
    //external_files is a C++ std::vector of the struct 'external_file'. Check the struct in 
    //interrupt.hpp to know more.
    auto [vectors, delays, external_files] = parse_args(argc, argv);
    std::ifstream input_file(argv[1]);

    //Just a sanity check to know what files you have
    print_external_files(external_files);

    //Make initial PCB (notice how partition is not assigned yet)
    PCB current(0, -1, "init", 1, -1);
    //Update memory (partition is assigned here, you must implement this function)
    if(!allocate_memory(&current)) {
        std::cerr << "ERROR! Memory allocation failed!" << std::endl;
    }

    std::vector<PCB> wait_queue;

    /******************ADD YOUR VARIABLES HERE*************************/

    

    

    /******************************************************************/

    //Converting the trace file into a vector of strings.
    std::vector<std::string> trace_file;
    std::string trace;
    while(std::getline(input_file, trace)) {
        trace_file.push_back(trace);
    }

    auto [execution, system_status, _] = simulate_trace(   trace_file, 
                                            0, 
                                            vectors, 
                                            delays,
                                            external_files, 
                                            current, 
                                            wait_queue);

    input_file.close();

    write_output(execution, "execution.txt");
    write_output(system_status, "system_status.txt");

    return 0;
}
