import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class FifoSimulation {
	
	FifoSimulation(){
		
	}
	
	 public int execute(int pages[], int n, int totalFrames)
	    {
	        // To represent set of current pages. We use
	        // an unordered_set so that we quickly check
	        // if a page is present in set or not
	        HashMap<Integer,Integer> currentPages = new HashMap<>(totalFrames);
	      
	        // To store the pages in FIFO manner
	        Queue<Integer> indexes = new LinkedList<>() ;
	      
	        // Start from initial page
	        int page_faults = 0;
	        int frameNumber = 0;
	        
	        for (int i=0; i<n; i++)
	        {
	            // Check if the set can hold more pages
	            if (currentPages.size() < totalFrames)
	            {
	                // Insert it into set if not present
	                // already which represents page fault
	            	
	                if (!currentPages.containsValue(pages[i]))
	                {
	                    currentPages.put(frameNumber,pages[i]);
	                    // increment page fault
	                    page_faults++;
	      
	                    // Push the current page into the queue
	                    indexes.add(pages[i]);
	                    
	                    System.out.println("Page " + pages[i] + " loaded into Frame " + frameNumber);
	                    frameNumber++;
	                }
	                else{
	                	System.out.println("Page " + pages[i] + " loaded in Frame " + (int)getKeyFromValue(currentPages,pages[i]) );
	                }
	            }
	      
	            // If the set is full then need to perform FIFO
	            // i.e. remove the first page of the queue from
	            // set and queue both and insert the current page
	            else
	            {
	                // Check if current page is not already
	                // present in the set
	                if (!currentPages.containsValue(pages[i]))
	                {
	                	String updateSting = new String();
	                    //Pop the first page from the queue
	                    int val = indexes.peek();
	      
	                    indexes.poll();
	      
	                    int frameNumReplace = (int)getKeyFromValue(currentPages,val);
	                    int valueInCurFrame = (int)currentPages.get(frameNumReplace);
	      
	                    // insert the current page
	                    currentPages.replace(frameNumReplace,pages[i]);
	      
	                    // push the current page into the queue
	                    indexes.add(pages[i]);
	      
	                    // Increment page faults
	                    System.out.println("Page " + valueInCurFrame + " unloaded from " + "Frame " + frameNumReplace + ", Page " +
	                    					pages[i] + " loaded into frame " + frameNumReplace);
	                    page_faults++;
	                }
	                else{
	                	System.out.println("Page " + pages[i] + " already in frame " + (int)getKeyFromValue(currentPages,pages[i]) );
	                }
	            }
	        }
	        System.out.println(page_faults + " page faults");
	        return page_faults;
	    }
	 
	 	public static Object getKeyFromValue(Map hm, Object value) {
		    for (Object o : hm.keySet()) {
		      if (hm.get(o).equals(value)) {
		        return o;
		      }
		    }
		    return null;
	 	}
}
