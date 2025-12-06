#include<interrupts_Aidan_Sultan.hpp>
#define TIME_QUANTUM 100

void FCFS(std::vector<PCB> &ready_queue) {
    std::sort( 
                ready_queue.begin(),
                ready_queue.end(),
                []( const PCB &first, const PCB &second ){
                    return (first.arrival_time > second.arrival_time); 
                } 
            );
}

void PID_Sort(std::vector<PCB> &ready_queue) {
    std::sort(
        ready_queue.begin(),
        ready_queue.end(),
        []( const PCB &first, const PCB &second ) {
            return (first.PID < second.PID);
        }
    );
}

PCB& find_PCB(int PID, std::vector<PCB> &job_list) {
    
    for (auto &process : job_list) {
        
        if (process.PID == PID) {
            return process;
        }
    }

    //should always be found
    throw std::runtime_error("PCB not found in job_list");

}

void remove_from_waitQ(int PID, std::vector<PCB> &wait_queue) {
    
    if (!(wait_queue.empty())) {
        //lambda push process we want to the end and captures PID
        std::stable_partition(
            wait_queue.begin(),
            wait_queue.end(),
            [PID]( const PCB &p ) {
                return p.PID != PID;
            }
        );

        wait_queue.pop_back(); //removing from wait queue
    }
}

std::tuple<std::string /* add std::string for bonus mark */ > run_simulation(std::vector<PCB> list_processes) {

    std::vector<PCB> ready_queue;   //The ready queue of processes
    std::vector<PCB> wait_queue;    //The wait queue of processes
    std::vector<PCB> job_list;      //A list to keep track of all the processes. This is similar
                                    //to the "Process, Arrival time, Burst time" table that you
                                    //see in questions. You don't need to use it, I put it here
                                    //to make the code easier :).

    std::vector<PCB> allocate_queue; //The wait queue for a process if there are no partitions available
    std::unordered_map<int, unsigned int> io_duration; //I/O duration map for PCB's in wait queue

    unsigned int current_time = 0;
    PCB running;

    //Initialize an empty running process
    idle_CPU(running);

    std::string execution_status;

    //make the output table (the header row)
    execution_status = print_exec_header();

    //Loop while till there are no ready or waiting processes.
    //This is the main reason I have job_list, you don't have to use it.
    while(!all_process_terminated(job_list) || job_list.empty()) {

        //Inside this loop, there are three things you must do:
        // 1) Populate the ready queue with processes as they arrive
        // 2) Manage the wait queue
        // 3) Schedule processes from the ready queue

        //Population of ready queue is given to you as an example.
        //Go through the list of proceeses
        for(auto &process : list_processes) {
            bool assigned; //flag if process got allocated
            bool arrived = false; //flag if the process arrived to be allocated
            
            if(process.arrival_time == current_time) {//check if the AT = current time
                //if so, assign memory and put the process into the ready queue
                assigned = assign_memory(process); 

                arrived = true; 

                process.state = READY;  //Set the process state to READY
                ready_queue.push_back(process); //Add the process to the ready queue
                job_list.push_back(process); //Add it to the list of processes

                execution_status += print_exec_status(current_time, process.PID, NEW, READY);
            }

            //adds process to allocate queue if there are no free partitions at the time of arrival
            if (arrived && !assigned) {
                allocate_queue.push_back(process);
            }
        }

        //try and allocate memory for process
        if (!(allocate_queue.empty())) {

            for (auto &process : allocate_queue) { 

                if (assign_memory(process)) {
                    process.state = READY;  //Set the process state to READY
                    ready_queue.push_back(process); //Add the process to the ready queue
                    job_list.push_back(process); //Add it to the list of processes

                    execution_status += print_exec_status(current_time, process.PID, NEW, READY);

                }
            }

            //removes from allocate queue
            allocate_queue.erase(
                std::remove_if(allocate_queue.begin(), allocate_queue.end(),
                [](const PCB &p){ return p.state == READY; }),
                allocate_queue.end()
            );
        }

        ///////////////////////MANAGE WAIT QUEUE/////////////////////////
        if (!(io_duration.empty())) { //decrement of I/O of process in wait queue
            
            for (auto pair = io_duration.begin(); pair != io_duration.end();) {
                int PID = pair->first;
                unsigned int &remaining = pair->second;
                
                //if process done I/O wait
                if (remaining == 0) {
                    PCB current_PCB = find_PCB(PID, job_list); //gets PCB referenxe

                    current_PCB.state = READY;  //Set the process state to READY

                    sync_queue(job_list, current_PCB);

                    remove_from_waitQ(current_PCB.PID, wait_queue); //remove from wait queue
                    ready_queue.push_back(current_PCB); //Add the process to the ready queue

                    execution_status += print_exec_status(current_time, current_PCB.PID, WAITING, READY);

                    pair = io_duration.erase(pair); //erase from map and return valid iterator

                } else {
                    --remaining; //decrement I/O duration time
                    ++pair;

                }

            }

        }

        if (running.PID != -1) { //not doing logic on idle CPU
            
            running.remaining_time--; //decrements current running process

            if (running.remaining_time == 0) { //if process is done

                execution_status += print_exec_status(current_time, running.PID, RUNNING, TERMINATED);
                
                terminate_process(running, job_list);
                idle_CPU(running); //no longer any process in CPU

            } else if ((current_time - running.start_time) == TIME_QUANTUM) { //check quantum expiration

                running.state = READY;
                ready_queue.push_back(running);   // put process at back

                execution_status += print_exec_status(current_time, running.PID, RUNNING, READY);

                idle_CPU(running); // clears running.PID = -1

            } else if ((current_time - running.start_time) % (running.io_freq) == 0 && running.io_freq != 0) { //if process requires IO

                wait_queue.push_back(running); //add to I/O wait queue
                running.state = WAITING;

                sync_queue(job_list, running); //don't know if needed
                
                io_duration[running.PID] = running.io_duration - 1; //adding to map to track I/O duration

                execution_status += print_exec_status(current_time, running.PID, RUNNING, WAITING);

                idle_CPU(running); //idle CPU to go back to PID -1

            }
        }
        
        /////////////////////////////////////////////////////////////////

        //////////////////////////SCHEDULER//////////////////////////////
        //PID_Sort(ready_queue); //tested and works, puts Highest PID to back of queue
        
        //schedule new process if CPU idle
        if (running.PID == -1 && !(ready_queue.empty())) {
            //run_process(running, job_list, ready_queue, current_time);
            running = ready_queue.front();
            ready_queue.erase(ready_queue.begin());
            running.start_time = current_time;
            running.state = RUNNING;
            sync_queue(job_list, running);

            execution_status += print_exec_status(current_time, running.PID, READY, RUNNING);
        }
        /////////////////////////////////////////////////////////////////

        current_time++;

    }
    
    //Close the output table
    execution_status += print_exec_footer();

    return std::make_tuple(execution_status);
}


int main(int argc, char** argv) {

    //Get the input file from the user
    if(argc != 2) {
        std::cout << "ERROR!\nExpected 1 argument, received " << argc - 1 << std::endl;
        std::cout << "To run the program, do: ./interrutps <your_input_file.txt>" << std::endl;
        return -1;
    }

    //Open the input file
    auto file_name = argv[1];
    std::ifstream input_file;
    input_file.open(file_name);

    //Ensure that the file actually opens
    if (!input_file.is_open()) {
        std::cerr << "Error: Unable to open file: " << file_name << std::endl;
        return -1;
    }

    //Parse the entire input file and populate a vector of PCBs.
    //To do so, the add_process() helper function is used (see include file).
    std::string line;
    std::vector<PCB> list_process;
    while(std::getline(input_file, line)) {
        auto input_tokens = split_delim(line, ", ");
        auto new_process = add_process(input_tokens);
        list_process.push_back(new_process);
    }
    input_file.close();

    //With the list of processes, run the simulation
    auto [exec] = run_simulation(list_process);

    write_output(exec, "execution.txt");

    return 0;
}
