package assignment3;
import java.lang.*;
import java.util.ArrayList;

public class proj2{
  public static void main(String arg[]){
    int q;
    if(arg.length > 2){
      q = Integer.parseInt(arg[2]);
    }
    else{
      q = 1;
    }
    ReadFile r = new ReadFile();
    ArrayList<PCB> fileLoad = r.parseFile(arg[0]);
    int selectedAlgorithm = 0;
    if(arg[1].equals( "FCFS"))
      selectedAlgorithm = 0;
    else if(arg[1].equals("RR"))
      selectedAlgorithm = 1;
    else if(arg[1].equals("SRTF"))
      selectedAlgorithm = 2;

    Scheduler scheduler = new Scheduler(selectedAlgorithm,q);
    scheduler.loadUnscheduled(fileLoad);
    scheduler.run();
    scheduler.printResults();
  }
}
