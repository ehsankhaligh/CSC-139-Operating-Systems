/*
 * Ehsan Hosseinzadeh Khaligh 
 * Simulates the FCFS disk scheduling algorithm.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(int argc, char *argv[]){
    int requests[argc];
    if(argc > 1){
        for(int i = 1; i<argc; i++){
            requests[i-1] = atoi(argv[i]);
        }
    }
    else{
        printf("Invalid number of arguments.\n");
        printf("Usage: $ a.out <space separated list of int>.\n");
    }

    int total_dist = 50 - requests[0];
    for (int i=0; i<argc-2; i++){
        int curr_track = requests[i];
        int next_track = requests[i+1];
        printf("Reading track %d\n", curr_track);
        total_dist += abs(next_track - curr_track);
    }

    printf("Reading track %d\n", requests[argc-2]);
    printf("%d\n", total_dist);
}
