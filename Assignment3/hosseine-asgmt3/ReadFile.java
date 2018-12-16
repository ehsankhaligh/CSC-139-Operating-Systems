package assignment3;
import java.lang.*;
import java.util.ArrayList;
import java.util.Vector;
import java.io.*;
import java.util.StringTokenizer;
class ReadFile{

     ReadFile(){}

      ArrayList<PCB> parseFile(String filename){
      ArrayList<PCB> line_arguments = new ArrayList<PCB>();
      Vector<Integer> v = new Vector<Integer>();
      String line = null;

      try{
        FileReader fileReader = new FileReader(filename);

        BufferedReader bufferedReader = new BufferedReader(fileReader);


        while((line = bufferedReader.readLine()) != null){

	  StringTokenizer st = new StringTokenizer(line);
          while(st.hasMoreTokens()){
            int temp = Integer.parseInt(st.nextToken());
            v.add(temp);
          }
          PCB pcb = new PCB(v.get(0),(float)v.get(1),(float)v.get(2));
          line_arguments.add(pcb);
          v.clear();
        }

        bufferedReader.close();

        return line_arguments;
      }

      catch(FileNotFoundException ex){
        System.out.println("Unable to open file");
      }

      catch(IOException ex){
        System.out.println("Error reading file");
      }
      return line_arguments;
    }
}
