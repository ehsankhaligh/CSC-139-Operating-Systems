/*
 * Ehsan Hosseinzadeh Khaligh
 * Simulates the LOOK disk scheduling algorithm.
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <limits.h>

int compare(const void * a, const void * b);

int main(int argc, char *argv[]){
    int requests[argc-1];
    int processed[argc-1];
    int order[argc-1];
    int dist = 0;
    int lastLook;
    bool goUp = false;

    if(argc > 1){
        for(int i = 1; i<argc; i++){
            requests[i-1] = atoi(argv[i]);
            processed[i-1] = 0;
        }
    }
    else{
        printf("Invalid number of arguments.\n");
        printf("Usage: $ a.out <space separated list of int>.\n");
    }

    qsort(requests, argc-1, sizeof(int), compare);
    int midwayPos = argc-1;

    //Check for tracks above starting point (50)
    for (int i=0; i<argc-1; i++){
        if (requests[i] >= 50){
            midwayPos = i;
            i = argc-1;
            goUp = true;
        }
    }

    //Fill in traversal order
    int orderPos = 0;
    for (int i=midwayPos; i<argc-1; i++){
        order[orderPos] = requests[i];
        orderPos++;
    }

    for (int i=midwayPos-1; i>=0; i--){
        order[orderPos] = requests[i] ;
        orderPos++;
    }

    printf("Reading track: %d\n", order[0]);
    dist += abs(50 - order[0]);

    for (int i=argc-2; i>0; i--){
        printf("Reading track: %d\n", order[argc-1-i]);
        dist += abs(order[i] - order[i-1]);
    }

    printf("Total distance: %d\n", dist);
}

int compare(const void * a, const void * b){
    return (*(int*)a - *(int*)b);
}
