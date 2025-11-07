#include<string>
#include<iostream>

struct Partition {
    unsigned int pNumber;
    unsigned int size;
    std::string code = "free";

    Partition(unsigned int number, unsigned int cap) : pNumber(number), size(cap) {}
};

struct Node {
    Partition* partition;
    Node* next;

    //basically a constructor that intializes partition and next. Takes Partition* as argument
    Node(Partition* part) : partition(part), next(nullptr) {}
};

int main(void) {

    //initialize all partitions in a linked list
    unsigned int partition_size[6] = {40, 25, 15, 10, 8, 2};
    int count = 1;

    Node* head = new Node((new Partition(count, partition_size[count - 1])));
    Node* current = head;
    count++;

    //creates a Node and Partition and adds them to the linked list
    while(count < 7) {
        Partition* partition = new Partition(count, partition_size[count - 1]);
        Node* node = new Node(partition);

        current->next = node;
        current = current->next;
        count++;
    }

    //debug testing initialize
    Node* currentNode = head;
    while(currentNode != nullptr) {
        Partition* partition_data = currentNode->partition;
        std::cout << "partition number: " << partition_data->pNumber << "\npartition size: " << partition_data->size << "\nstatus: " << partition_data->code;
        std::cout << "\n" << std::endl;

        currentNode = currentNode->next;
    }

    //deconstructing memory
    //!!!!!!add console output to verify
    Node* currentNode = head;
    Node* toDelete = nullptr;
    while(currentNode != nullptr) {
        if (toDelete != nullptr) {
            delete toDelete->partition;
            delete toDelete;
            toDelete = nullptr;
        }

        toDelete = currentNode;
        currentNode = currentNode->next;
    }

    if (toDelete != nullptr) {
        delete toDelete->partition;
        delete toDelete;
        toDelete = nullptr;
    }


}
