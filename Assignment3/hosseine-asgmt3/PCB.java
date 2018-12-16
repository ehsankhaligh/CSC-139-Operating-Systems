package assignment3;
//process control block
class PCB {

  //input data
  private int pid;
  private float arrival;
  private float CPUburst;

  //simulation data
  private float finish;
  private float timeRemaining;
  private float waiting;
  private float response;
  private float turnaround;
  private boolean responseOccured;
  PCB(){ };

  PCB(int p, float a, float c){
    pid = p;
    arrival = a;
    CPUburst = c;
    finish = (float)0.0;
    timeRemaining = CPUburst;
    waiting = (float)0.0;
    response =(float) 0.0;
    responseOccured = false;
    turnaround = (float)0.0;
  }

  //getters
  public int getPid(){return pid;}
  public float getArrival(){return arrival;}
  public float getCPUBurst(){return CPUburst;}
  public float getFinish(){return finish;}
  public float getRemaining(){return timeRemaining;}
  public float getWaiting(){return waiting;}
  public float getResponse(){return response;}
  public float getTurnaround(){return turnaround;}
  public boolean hasResponseOccured(){return responseOccured;}

  //set functions
  public void setArrival(float a){arrival = a;}
  public void setResponse(float r){response = r;}
  public void setBoolResponse(boolean r){responseOccured = r;}
  public void responseHasOccured(){responseOccured = true;}
  public void incrementWaiting(float inc){
    waiting = waiting + inc;
  }
  public void incrementRunning(float inc){
    timeRemaining = timeRemaining - inc;
  }
  public void done(float time){
  finish = time;
  turnaround = finish - arrival;
  }
}
