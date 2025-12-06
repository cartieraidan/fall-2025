
def number_process(list, filepath1) -> int:
    
    total_process = 0

    for x in list:
        file_name1 = filepath1 + "/input_fileTest" + str(x) +".txt"
        with open(file_name1, "r") as file:
            for line in file:
                total_process = total_process + 1

    return total_process



def Throughput(list, filepath1, filepath2) -> str:

    simulation_time = 0
    total_time = 0
    total_process = 0

    for x in list:
        file_name1 = filepath1 + "/executionTest" + str(x) +".txt"

        with open(file_name1, "r") as file:
            for line in file:
                line = line.replace(" ", "").split("|")
                #print(line)
                if (len(line) == 6):
                    if (line[1] != "TimeofTransition"):
                        simulation_time = int(line[1])

            #print("simulation time: ", simulation_time)
            total_time = total_time + simulation_time

    total_process = number_process(list, filepath2)

    throughput = total_process / total_time
       
    return str(throughput)

def turnaround(list, filepath1, filepath2) -> str:

    total_time = 0
    total_process = 0

    for x in list:
        file_name1 = filepath1 + "/executionTest" + str(x) +".txt"
        dic = {}

        with open(file_name1, "r") as file:
                    for line in file:
                        line = line.replace(" ", "").split("|")
                        if (len(line) == 6):
                            if (line[1] != "TimeofTransition"):
                                if line[2] not in dic:
                                    dic[line[2]] = int(line[1])
                                    
                                if (line[4] == "TERMINATED"):
                                    total_time = int(line[1]) - dic[line[2]]

        total_process = number_process(list, filepath2)

    turn_around = total_time / total_process

    return str(turn_around)


def response(list, filepath1, filepath2) -> str:

    total_time = 0
    total_process = 0

    for x in list:
        file_name1 = filepath1 + "/executionTest" + str(x) +".txt"
        file_name2 = filepath2 + "/input_fileTest" + str(x) +".txt"
        start_time = {}
        arrival_time = {}

        with open(file_name1, "r") as file:
                    for line in file:
                        line = line.replace(" ", "").split("|")
                        if (len(line) == 6):
                            if (line[1] != "TimeofTransition"):
                        
                                    
                                if (line[4] == "RUNNING"):
                                    if line[2] not in start_time:
                                        start_time[line[2]] = int(line[1])

        with open(file_name2, "r") as file:
            for line in file:
                line = line.split(",")
                arrival_time[line[0]] = int(line[2])

        for key in start_time.keys():
            total_time = total_time + (start_time[key] - arrival_time[key])

        total_process = number_process(list, filepath2)

        return str(total_time / total_process)


def wait_time(list, filepath1, filepath2) -> str:
    total_time = 0
    total_process = 0

    for x in list:
        file_name1 = filepath1 + "/executionTest" + str(x) +".txt"
        dic = {}

        with open(file_name1, "r") as file:
                    for line in file:
                        line = line.replace(" ", "").split("|")
                        if (len(line) == 6):
                            if (line[1] != "TimeofTransition"):
                                #if line[2] not in dic:
                                   # dic[line[2]] = int(line[1])
                                    
                                if (line[4] == "READY"):
                                    if line[2] not in dic:
                                        dic[line[2]] = int(line[1])

                                if (line[3] == "READY"):
                                    if line[2] in dic:
                                        total_time = total_time + (int(line[1]) - dic[line[2]])
                                        del dic[line[2]]

        total_process = number_process(list, filepath2)

    
    return str(total_time / total_process)
        



if __name__ == "__main__":
    EP = [1,2,3,4,5,6,19,20]
    RR = [7,8,9,10,11,12]
    EP_RR = [13,14,15,16,17,18]


    print("The average throughput for the EP scheduler: " + Throughput(EP, "output_files/EP-test-cases", "input_files/EP-test-cases") + " ms")
    print("The average throughput for the RR scheduler: " + Throughput(RR, "output_files/RR-test-cases", "input_files/RR-test-cases") + " ms")
    print("The average throughput for the EP+RR scheduler: " + Throughput(EP_RR, "output_files/EP-RR-test-cases", "input_files/EP-RR-test-cases") + " ms\n")

    print("The average turnaround for the EP scheduler: " + turnaround(EP, "output_files/EP-test-cases", "input_files/EP-test-cases") + " ms")
    print("The average turnaround for the RR scheduler: " + turnaround(RR, "output_files/RR-test-cases", "input_files/RR-test-cases") + " ms")
    print("The average turnaround for the EP+RR scheduler: " + turnaround(EP_RR, "output_files/EP-RR-test-cases", "input_files/EP-RR-test-cases") + " ms\n")

    print("The average response for the EP scheduler: " + response(EP, "output_files/EP-test-cases", "input_files/EP-test-cases") + " ms")
    print("The average response for the RR scheduler: " + response(RR, "output_files/RR-test-cases", "input_files/RR-test-cases") + " ms")
    print("The average response for the EP+RR scheduler: " + response(EP_RR, "output_files/EP-RR-test-cases", "input_files/EP-RR-test-cases") + " ms\n")

    print("The average wait time for the EP scheduler: " +wait_time(EP, "output_files/EP-test-cases", "input_files/EP-test-cases") + " ms")
    print("The average wait time for the RR scheduler: " +wait_time(RR, "output_files/RR-test-cases", "input_files/RR-test-cases") + " ms")
    print("The average wait time for the EP+RR scheduler: " +wait_time(EP_RR, "output_files/EP-RR-test-cases", "input_files/EP-RR-test-cases") + " ms")
