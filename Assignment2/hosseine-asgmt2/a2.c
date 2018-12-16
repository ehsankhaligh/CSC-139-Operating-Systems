#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <semaphore.h>
#include "buffer.h"

#define TRUE 1
#define TEST 100000000

static unsigned int seed = 1;
buffer_item buffer[BUFFER_SIZE];
int counter;

pthread_mutex_t mutex;
sem_t segmentEmpty;
sem_t segmentFull;
pthread_t pid;
pthread_t cid;
pthread_attr_t attr;

void *producer(void *param);
void *consumer(void *param);

/* insert item into the buffer
return 0 if successful, otherwise
return -1 indicating an error condition */

int insert_item(buffer_item item) {
	if(counter < BUFFER_SIZE) {
		buffer[counter] = item;
		counter++;
		return 0;
	} else {
		return -1;
	}
}

/* remove an object from the buffer
placing it in item
return 0 if successful, otherwise
return -1 indicating error */

int remove_item(buffer_item *item) {

	if(counter > 0) {
		*item = buffer[(counter-1)];
		counter--;
		return 0;
	} else {
		return -1;
	}
}

void initialize(){
	//make the mutex, empty and full semaphore
	pthread_mutex_init(&mutex, NULL);
	sem_init(&segmentEmpty, 0, BUFFER_SIZE);
	sem_init(&segmentFull, 0, 0);
}

void *producer(void *param) {
	buffer_item item;
	while(TRUE) {
		//sleep for random time
		sleep(rand_r(&seed)/TEST);
		//generate a random number
		item = rand_r(&seed);
		//empty and mutux
		sem_wait(&segmentEmpty);
		pthread_mutex_lock(&mutex);

		if(insert_item(item)){
			printf("report error condition\n");
		}
		else {
			printf("producer produced %d\n", item);
		}

		//release mutex, signal full
		pthread_mutex_unlock(&mutex);
		sem_post(&segmentFull);
	}
}

void *consumer(void *param) {
	buffer_item item;
	while(TRUE) {
		//sleep for random time
		sleep(rand_r(&seed)/TEST);
		//get the full and mutex
		sem_wait(&segmentFull);
		pthread_mutex_lock(&mutex);

		if(remove_item(&item)) {
			printf("report error condition");
		}
		else {
			printf("consumer consumed %d\n", item);
		}

		//release the mutex and signal empty
		pthread_mutex_unlock(&mutex);
		sem_post(&segmentEmpty);
	}
}

//1. Get command line args 1 2 3
//2. Initialize buffer
//3. Create producer threads
//4. Create consumer threads
//5. Sleep
//6. Exit

int main(int argc, char *argv[]) {
	int i;
	srand(time(NULL));
	//check to see if enter right args
	if(argc < 4){
		printf("\nPlease enter Three arguments: Sleep Time, Producer Threads, Consumer Threads\n");
		exit(0);
	}

	int sleeptime = atoi(argv[1]);
	const long int producerThreads = strtol(argv[2], NULL, 0);
    const long int consumerThreads = strtol(argv[3], NULL, 0);
	initialize();

	//init the attr
	pthread_attr_init(&attr);

	//init buffer
	counter = 0;
	pthread_t producers[producerThreads];
	pthread_t consumers[consumerThreads];

	//creation of each thread type
	for(i=0; i < producerThreads; i++) {
		pthread_create(&producers[i], &attr, producer, NULL);
	}
	for(i=0; i < consumerThreads; i++) {
		pthread_create(&consumers[i], &attr, consumer, NULL);
	}

	sleep(sleeptime);
	return 0;

}
