import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;


public class proj4 {

	public static void main(String[] args) {
		int pageCount = 12;
		int pages[] = new int[pageCount];
		int framesCount = 0;
		int pageRequestTotal = 0;
		int loopCounter = 0;
	
        String fileName = "input1.txt";

        // This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            //read first line
            line = bufferedReader.readLine();
            
            //parse line, set parameters
            int parseCount = 0;
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                if (parseCount == 0){
                	//do nothing
                	st.nextToken();
                }
                else if (parseCount == 1){
                	framesCount = Integer.parseInt(st.nextToken());
                }
                else if (parseCount == 2){
                	pageRequestTotal = Integer.parseInt(st.nextToken());
                }
                
                parseCount++;
               
            }
            
            while((line = bufferedReader.readLine()) != null){ 
                pages[loopCounter] = Integer.parseInt(line);
                loopCounter++;
            }
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
    
        if(args.length == 0){
            System.out.println("Usage: proj4 input file [FIFO|LRU|OPT]");
        }else{  
              if(args[0].equals("FIFO")){
              
                  FifoSimulation fifoSim = new FifoSimulation();
                  int pageFaults = fifoSim.execute(pages,pageRequestTotal,framesCount);
              	
              }
              else if(args[0].equals("OPT")){
              	 OptSimulation optSim = new OptSimulation();
                   optSim.execute(pages, pageRequestTotal, framesCount);
                   
              } 	 
              else if(args[0].equals("LRU")){
                	LruSimulation lruSim = new LruSimulation();
                  lruSim.execute(pages, pageRequestTotal, framesCount);
             } 
        }
   	}
}
