#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/time.h>
#include <time.h>
#define LB_SIZE 2048

enum TYPE{SHORT,LONG,STANDARD};
struct timeval now;

void SectionB();
void SectionC();
void SectionD(int interval, int duration);
void vendorId();
void modelName();
void kernelVersion();
void lastBoot();
void initializing();
void hostname();
void printingTime(char* label, long time);
void CPUModeTimeSpent();
void diskReq();
void contextSwitchs();
void last_time_sys_Rebooted();
void process_nember();
void memory_total();
void memoryAvailable();
void sampleLoadAvarage();
void printingSampleLoadAvarage(int interval, int duration);


int main(int argc, char *argv[]) {
    
    int interval, duration;
    char repTypeName[16];
    char c1, c2;
    enum TYPE reportType;
    reportType = STANDARD;
    strcpy(repTypeName, "Standard");
    
    if (argc > 1) {
        sscanf(argv[1], "%c%c", &c1, &c2);
        
        if (c1 != '-') {
            fprintf(stderr, "usage: observer [-s][-l int dur]\n");
            exit(1);
        }
        
        if (c2 == 's') {
            reportType = SHORT;
            strcpy(repTypeName, "Short");
        }
        
        if (c2 == 'l') {
            reportType = LONG;
            strcpy(repTypeName, "Long");
            interval = atoi(argv[2]);
            duration = atoi(argv[3]);
        }
    }
    
    if(reportType == STANDARD){
        SectionB();
    }
    if(reportType ==  SHORT){
        SectionB();
        SectionC();
    }
    if(reportType == LONG) {
        SectionB();
        SectionC();
        SectionD(interval, duration);
    }
    
    exit(0);
}

void SectionB(){
    vendorId();
    modelName();
    kernelVersion();
    lastBoot();
    initializing();
    hostname();
}

void SectionC(){
    CPUModeTimeSpent();
    diskReq();
    contextSwitchs();
    last_time_sys_Rebooted();
    process_nember();
}

void SectionD(int interval, int duration){
    memory_total();
    memoryAvailable();
    sampleLoadAvarage();
    printingSampleLoadAvarage(interval, duration);
}

void vendorId(){
    FILE *File;
    char lineBuffer[2048];
    
    File = fopen ("/proc/cpuinfo", "r");
    fgets(lineBuffer,LB_SIZE+1, File);
    fgets(lineBuffer,LB_SIZE+1, File);
    printf("%s", lineBuffer);
    fclose(File);
}

void modelName(){
    FILE *File;
    char lineBuffer[2048];
    
    File = fopen ("/proc/cpuinfo", "r");
    fgets(lineBuffer,LB_SIZE+1, File);
    fgets(lineBuffer,LB_SIZE+1, File);
    fgets(lineBuffer,LB_SIZE+1, File);
    fgets(lineBuffer,LB_SIZE+1, File);
    fgets(lineBuffer,LB_SIZE+1, File);
    printf("%s", lineBuffer);
    fclose(File);
}

void kernelVersion(){
    FILE *File;
    char lineBuffer[2048];
    
    File = fopen ("/proc/version", "r");
    fgets(lineBuffer,LB_SIZE+1, File);
    printf("%s", lineBuffer);
    fclose(File);
}

void lastBoot(){
    FILE *File;
    char lineBuffer[2048];
    double uptime, idle_time;
    
    File = fopen ("/proc/uptime", "r");
    fscanf (File, "%lf %lf\n", &uptime, &idle_time);
    printingTime ("System last boot time ", (long) uptime);
    fclose(File);
}

void initializing(){
    char repTypeName[16] = "Standard";
    int reportType;
    reportType = STANDARD;
    
    gettimeofday(&now, NULL); //current time
    printf("Status report type %s at %s", repTypeName, ctime(&(now.tv_sec)));
}

void hostname(){
    FILE *File;
    char lineBuffer[2048];
    
    File = fopen ("/proc/sys/kernel/hostname", "r");
    fgets(lineBuffer, LB_SIZE+1, File);
    printf("Machine Hostname: %s", lineBuffer);
    fclose(File);
}

void printingTime(char* time_label, long time)
{	/* Conversion constants. */
    const long minute = 60;
    const long hour = minute * 60;
    const long day = hour * 24;
    
    /* Produce output. */
    printf ("%s : %ld:%ld:%02ld:%02ld\n", time_label, time / day,
            (time % day) / hour, (time % hour) / minute, time % minute);
}

void CPUModeTimeSpent(){
    FILE *File;
    char lineBuffer[2048];
    char* label;
    long user, sys, niced, ctxt;
    long long idle;
    
    File = fopen ("/proc/stat", "r");
    fscanf(File,"%s %lu %lu %lu %llu \n",&label, &user, &niced, &sys, &idle);
    printf("User: %lu\nSystem: %lu\nIdle: %llu \n", user, sys, idle);
    fclose(File);
}

void diskReq(){
    FILE *File;
    char lineBuffer[2048];
    File = fopen ("/proc/diskstats", "r");
    char* keyword = "sda";
    char* strstr_var;
    char* temp_var;
    fgets(lineBuffer,LB_SIZE+1, File);
    strstr_var = strstr(lineBuffer,keyword);
    int count = 0;
    int i;
    long temp_var1, temp_var2, temp_var3, readIssued, readMerged, sectorRead, millisecRead, writeComp,writeMerge, sectorWrite;
    
    
    while(!strstr_var){
        fgets(lineBuffer, LB_SIZE+1, File);
        strstr_var = strstr(lineBuffer,keyword);
        count++;
    }
    
    rewind(File);
    
    for(i=0; i < count; i++){
        fgets(lineBuffer,LB_SIZE+1, File);
    }
    
    fscanf(File,"%lu %lu %s %lu %lu %lu %lu %lu %lu %lu\n",&temp_var1, &temp_var2, &temp_var3,
           &readIssued, &readMerged, &sectorRead, &millisecRead, &writeComp, &writeMerge, &sectorWrite);
    printf("Disk Read Request: %lu\nDisk Write Request: %lu\n", sectorRead,sectorWrite);
    fclose(File);
    
}

void contextSwitchs(){
    FILE *File;
    char lineBuffer[2048];
    File = fopen ("/proc/stat", "r");
    char* keyword = "ctxt";
    char* strstr_var;
    char* temp_var;
    fgets(lineBuffer,LB_SIZE+1, File);
    strstr_var = strstr(lineBuffer,keyword);
    int count = 0;
    int j;
    char temp_var1;
    long long ctxt;
    
    while(!strstr_var){
        fgets(lineBuffer, LB_SIZE+1, File);
        strstr_var = strstr(lineBuffer,keyword);
        count++;
    }
    
    rewind(File);
    for(j=0; j <count; j++){
        fgets(lineBuffer,LB_SIZE+1, File);
    }
    
    fscanf(File,"%s %llu \n",&temp_var1, &ctxt);
    printf("Context Switch: %llu\n", ctxt);
    
    fclose(File);
}

void process_nember(){
    
    FILE *File;
    char lineBuffer[2048];
    File = fopen ("/proc/stat", "r");
    char* keyword = "processes";
    char* strstr_var;
    char* temp_var;
    fgets(lineBuffer,LB_SIZE+1, File);
    strstr_var = strstr(lineBuffer,keyword);
    int count = 0;
    int j;
    char temp_var1;
    long processes;
    
    while(!strstr_var){
        fgets(lineBuffer, LB_SIZE+1, File);
        strstr_var = strstr(lineBuffer,keyword);
        count++;
    }
    
    rewind(File);
    for(j=0; j <count; j++){
        fgets(lineBuffer,LB_SIZE+1, File);
    }
    
    fscanf(File,"%s %lu \n",&temp_var1, &processes);
    printf("Number of Processes: %lu\n", processes);
    
    fclose(File);
    
}

void last_time_sys_Rebooted(){
    system("who -b");
}

void memory_total(){
    FILE *File;
    char lineBuffer[2048];
    File = fopen ("/proc/meminfo", "r");
    fgets(lineBuffer,LB_SIZE+1, File);
    printf("%s", lineBuffer);
    fclose(File);
}

void memoryAvailable(){
    FILE *File;
    char lineBuffer[2048];
    File = fopen ("/proc/meminfo", "r");
    fgets(lineBuffer,LB_SIZE+1, File);
    fgets(lineBuffer,LB_SIZE+1, File);
    printf("%s", lineBuffer);
    fclose(File);
}

void sampleLoadAvarage()
{
    FILE* File;
    char lineBuffer[2048];
    float avg;
    File = fopen ("/proc/loadavg", "r");
    
    fscanf(File,"%f\n",&avg);
    printf("Load Avg: %0.2f\n", avg);
    
    fclose(File);
}

void printingSampleLoadAvarage(int interval, int duration){
    int iteration = 0;
    
    while (iteration < duration) {
        sleep(interval);
        sampleLoadAvarage();
        iteration += interval;
    }
}
