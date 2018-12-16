/*
 * Ehsan Hosseinzadeh khaligh
 * Simulates the SSTF disk scheduling algorithm.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <limits.h>

int getIndexOfShortest(int* processed, int* requests, int last_index, int size);
int getIndexOfFirst(int* processed, int initial_position, int* requests, int size);

int main(int argc, char *argv[]){
    int requests[argc-1];
    int processed[argc-1];
    int order[argc-1];

    int next_pos;
    int last_pos;
    int length = argc-1;
    int total_dist = 0;
    int init_pos = 50;

    if(argc > 1){
        for(int i = 1; i<argc; i++){
            requests[i-1] = atoi(argv[i]);
        }
        //printf("Length = %d\n", length);
    }
    else{
        printf("Invalid number of arguments.\n");
        printf("Usage: $ a.out <space separated list of int>.\n");
        exit(0);
    }

    for (int i=0; i<argc; i++){
        processed[i] = 1;
    }

    // Must get first travel (from track 50)
    next_pos = getIndexOfFirst(processed, init_pos, requests, length);
    total_dist += abs(init_pos - requests[next_pos]);

    printf("Reading track %d\n", requests[next_pos]);
    processed[next_pos] = -1;
    order[0] = requests[next_pos];
    last_pos = next_pos;

    // Get the next travelled
    for (int i = 1; i<argc-1; i++){
        next_pos = getIndexOfShortest(processed, requests, last_pos, length);
        printf("Reading track %d\n", requests[next_pos]);
        processed[next_pos] = -1;
        order[i] = requests[next_pos];
        last_pos = next_pos;
    }

    for (int i=1; i < argc-1; i++){
        total_dist += abs(order[i] - order[i-1]);
        //printf("Distance: %d\n",abs(order[i] - order[i-1]));
    }
    printf("Total distance: %d\n", total_dist);
}

int getIndexOfFirst(int* processed, int init_pos, int* requests, int size){
    int min_index;
    int min_diff = INT_MAX;

    for (int i=0; i < size; i++){
        int diff = abs(requests[i] - init_pos);
        if (diff < min_diff){
            min_diff = diff;
            min_index = i;
        }
    }
    return min_index;
}

int getIndexOfShortest(int* processed, int* requests, int last_index, int size){
    int curr_pos = requests[last_index];
    int min_diff = INT_MAX;
    int min_index = 0;

    for (int i=0; i < size; i++){
        if ((i != last_index) && (processed[i] > -1)){
            int diff = abs(requests[i] - curr_pos);
            if (diff < min_diff){
                min_diff = diff;
                min_index = i;
            }
        }
    }
    return min_index;
}
