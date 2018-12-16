package assignment3;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;

class Scheduler{
  private float SIMULATION_INCREMENT;
  private ArrayList<PCB> ready = new ArrayList<PCB>();
  private ArrayList<PCB> running = new ArrayList<PCB>();
  private ArrayList<PCB> unscheduled = new ArrayList<PCB>();
  private ArrayList<PCB> terminated = new ArrayList<PCB>();
  private ArrayList<PCB> results = new ArrayList<PCB>();
  private int algorithm;
  private int quantum;
  private int quantumCounter = 0;
  private int simulationTime;
  private int SIMULATION_BOUND = 99999;
  private float avgWait = 0;
  private float avgResp = 0;
  private float avgTurn = 0;
  private boolean srtfFirstRun = true;

  class srtfItem{
    private int index;
    private int pid;

    public srtfItem(int p, int i){
      pid = p;
      index = i;
    }
    public int getIndex(){return index;}
    public int getPid(){return pid;}


  }

  public Scheduler(int a,int q){
    algorithm = a;
    quantum = q;
    SIMULATION_INCREMENT = 1;
    simulationTime = 0;
    if (algorithm != 1){
	quantum = 1;
    }
  }

  public void advance(){
    checkUnscheduled();
    switch(algorithm){
      case 0:
        fcfs();
	break;
      case 1:
        rrobin();
	break;
      case 2:
        srtf();
	break;
    }
  }

  public void fcfs(){
    if(running.get(0).getRemaining() == 0){
      runningToTerminated();
      if(ready.size() > 0)
        readyToRun();
    }

    if(running.size() > 0){
      running.get(0).incrementRunning(SIMULATION_INCREMENT);
      printSystemStatus(running.get(0).getPid(),"running");
    }

    for(int i = 0; i < ready.size(); i++){
      ready.get(i).incrementWaiting(SIMULATION_INCREMENT);
    }

  }

  public void rrobin(){
    if(running.size() > 0 && running.get(0).getRemaining() == 0)
      runningToTerminated();

    //when quantum time expires, swap
    if(simulationTime != 0 && quantumCounter == 0) {

      //when a current process is running,context switch
      if(ready.size() > 0 && running.size() > 0)
        contextSwitch();

      //when no current process is running (one has just finished)
      else if(ready.size() > 0)
        readyToRun();

    }

    if(running.size() > 0){
      running.get(0).incrementRunning(SIMULATION_INCREMENT);
      printSystemStatus(running.get(0).getPid(),"running");

      for(int i = 0; i < ready.size(); i++){
        ready.get(i).incrementWaiting(SIMULATION_INCREMENT);
      }
    }
    else
      simulationTime--;

    advanceQuantumCounter();
  }

  public void srtf(){
    if(srtfFirstRun){
      srtfFirstRun = false;
      running.get(0).setBoolResponse(false);
    }
    if(running.get(0).getRemaining() == 0){
      runningToTerminated();
      if(ready.size() > 0){
        srtfItem shortest = srtfSwapTest(-1,SIMULATION_BOUND);

        ready.add(0,ready.get(shortest.getIndex()));
        ready.remove(shortest.getIndex()+ 1);

        readyToRun();
      }
    }
    else{

      srtfItem shortest = srtfSwapTest(running.get(0).getPid(),running.get(0).getRemaining());
      if(shortest.getPid() != running.get(0).getPid()){
        //move shortest to front
        ready.add(0,ready.get(shortest.getIndex()));
        ready.remove(shortest.getIndex() + 1);

        contextSwitch();
      }
    }

    if (running.size() > 0){
      running.get(0).incrementRunning(SIMULATION_INCREMENT);
      printSystemStatus(running.get(0).getPid(),"running");
    }

    for(int i = 0; i < ready.size(); i++){
      ready.get(i).incrementWaiting(SIMULATION_INCREMENT);
    }
  }

  public void checkUnscheduled(){
    for (int i=0; i<unscheduled.size(); i++){
      if(unscheduled.get(i).getArrival() == simulationTime){
         ready.add(unscheduled.get(i));
	 unscheduled.remove(i);
         i--;

         if(running.size() == 0){
	   readyToRun();
         }
      }
    }
  }

  public void readyToRun(){
    if(ready.get(0).hasResponseOccured() == false){
      ready.get(0).responseHasOccured();
      float difference = simulationTime - ready.get(0).getArrival();
      ready.get(0).setResponse(difference);
    }
    running.add(ready.get(0));
    ready.remove(0);
  }

  public void runningToTerminated(){
    running.get(0).done(simulationTime);
    terminated.add(running.get(0));
    printSystemStatus(running.get(0).getPid(),"finished...");
    results.add(running.get(0));
    running.remove(0);
  }

  public void run(){
    while(hasProcesses()){
      advance();
      simulationTime++;
    }
  }

  public boolean hasProcesses(){
    return ((unscheduled.size() > 0)
            || (ready.size() > 0)
            || (running.size() > 0));
  }

  public void printSystemStatus(int pid, String status){
    System.out.println("<system time " + String.format("%4d",simulationTime) +
                      "> process " + String.format("%5d",pid) + " is " + status);
  }

  public void printResults(){
    float sumWait = 0;
    float sumResp = 0;
    float sumTurn = 0;

    for(int i = 0; i < terminated.size(); i++){
      sumWait += results.get(i).getWaiting();
      sumResp += results.get(i).getResponse();
      sumTurn += results.get(i).getTurnaround();
    }

    avgWait = sumWait / results.size();
    avgResp = sumResp / results.size();
    avgTurn = sumTurn / results.size();

    String algorithmString = "";

    if(algorithm == 0)
      algorithmString = "FCFS";
    else if(algorithm == 1)
      algorithmString = "RR";
    else if(algorithm  == 2)
      algorithmString = "SRTF";
    else
      algorithmString = "Not Recognized";

    System.out.println("All processes finish");
    System.out.println();
    System.out.println("==============================");
    System.out.println("Average cpu usage : 100.00%");
    System.out.println("Average waiting time : " + String.format("%,.2f",avgWait));
    System.out.println("Average response time : " + String.format("%,.2f",avgResp));
    System.out.println("Average turnaound time : " + String.format("%,.2f",avgTurn));
    System.out.println("==============================");
  }

  public void loadUnscheduled(ArrayList<PCB> list){
    for(int i = 0; i < list.size(); i++){
      unscheduled.add(list.get(i));
    }
  }

  public void contextSwitch(){
    if(ready.get(0).hasResponseOccured() == false){
      ready.get(0).responseHasOccured();
      float difference = simulationTime - ready.get(0).getArrival();
      ready.get(0).setResponse(difference);
    }
    ready.add(running.get(0));
    running.remove(0);
    running.add(ready.get(0));
    ready.remove(0);
  }

  public void advanceQuantumCounter(){
    quantumCounter = (quantumCounter + 1) % quantum;
  }

  public srtfItem srtfSwapTest(int shortestPid, float shortestRemaining){
    int readyIndex = -1;
    for(int i = 0; i < ready.size(); i++){
      if(ready.get(i).getRemaining() < shortestRemaining){
        shortestPid = ready.get(i).getPid();
        shortestRemaining = ready.get(i).getRemaining();
        readyIndex = i;
      }
    }
      srtfItem shortestItem = new srtfItem(shortestPid,readyIndex);
      return shortestItem;
  }
}
