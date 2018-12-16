import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;

public class LruSimulation {
	 // Method to find page faults using indexes
    public int execute(int pages[], int n, int totalFrames)
    {
        // To represent set of current pages. We use
        // an unordered_set so that we quickly check
        // if a page is present in set or not
        ArrayList<Integer> currentPages = new ArrayList<>(totalFrames);
      
        // To store least recently used indexes
        // of pages.
        HashMap<Integer, Integer> indexes = new HashMap<>();
      
        // Start from initial page
        int page_faults = 0;
        for (int i=0; i<n; i++)
        {
            // Check if the set can hold more pages
            if (currentPages.size() < totalFrames)
            {
                // Insert it into set if not present
                // already which represents page fault
                if (!currentPages.contains(pages[i]))
                {
                    currentPages.add(pages[i]);
      
                    // increment page fault
                    page_faults++;
                    
                    System.out.println("Page " + pages[i] + " loaded into Frame " +
                    					currentPages.indexOf(pages[i]));
                }
                else{
                	System.out.println("Page " + pages[i] + " already in Frame " + 
                		currentPages.indexOf(pages[i]));
                }
      
                // Store the recently used index of
                // each page
                indexes.put(pages[i], i);
            }
      
            // If the set is full then need to perform lru
            // i.e. remove the least recently used page
            // and insert the current page
            else
            {
                // Check if current page is not already
                // present in the set
                if (!currentPages.contains(pages[i]))
                {
                    // Find the least recently used pages
                    // that is present in the set
                    int lru = Integer.MAX_VALUE, val=Integer.MIN_VALUE;
                     
                    Iterator<Integer> itr = currentPages.iterator();
                     
                    while (itr.hasNext()) {
                        int temp = itr.next();
                        if (indexes.get(temp) < lru)
                        {
                            lru = indexes.get(temp);
                            val = temp;
                        }
                    }
                 
                    // Remove the indexes page
                    int frameNumReplace = currentPages.indexOf(val);
                   
                    // insert the current page
                    currentPages.set(frameNumReplace,pages[i]);
      
                    // Increment page faults
                    page_faults++;
                    
                    System.out.println("Page " + val + " unloaded from Frame " +
                                      frameNumReplace + ", Page " + pages[i] +
                                       " loaded into " + frameNumReplace);
                }
                else{
                	System.out.println("Page " + pages[i] + " already in Frame " + 
                    		currentPages.indexOf(pages[i]));
                }
      
                // Update the current page index
                indexes.put(pages[i], i);
            }
        }
        
        System.out.println( page_faults + " page faults");
        return page_faults;
    }
}
