import java.util.*;

public class OptSimulation {
	

	// demonstrate optimal page
	// replacement algorithm.

	// Function to check whether a page exists
	// in a frame or not
	public boolean search(int key, ArrayList<Integer> fr)
	{
		for (int i = 0; i < fr.size(); i++)
		{
			if (fr.get(i) == key)
			{
				return true;
			}
		}
		return false;
	}

	// Function to find the frame that will not be used
	// recently in future after given index in pg[0..pn-1]
	public int predict(int[] pg, ArrayList<Integer> fr, int pn, int index)
	{
		// Store the index of pages which are going
		// to be used recently in future
		int res = -1;
		int farthest = index;
		for (int i = 0; i < fr.size(); i++)
		{
			int j;
			for (j = index; j < pn; j++)
			{
				if (fr.get(i) == pg[j])
				{
					if (j > farthest)
					{
						farthest = j;
						res = i;
					}
					break;
				}
			}

			// If a page is never referenced in future,
			// return it.
			if (j == pn)
			{
				return i;
			}
		}

		// If all of the frames were not in future,
		// return any of them, we return 0. Otherwise
		// we return res.
		return (res == -1) ? 0 : res;
	}

	public void execute(int[] pg, int pn, int fn)
	{
		// Create an array for given number of
		// frames and initialize it as empty.
		ArrayList<Integer> fr = new ArrayList<Integer>();

		// Traverse through page reference array
		// and check for miss and hit.
		int hit = 0;
		for (int i = 0; i < pn; i++)
		{

			// Page found in a frame : HIT
			if (search(pg[i], fr))
			{
				hit++;
				System.out.println("Page " + pg[i] + " already in Frame " + fr.indexOf(pg[i]));
				continue;
			}

			// Page not found in a frame : MISS

			// If there is space available in frames.
			if (fr.size() < fn)
			{
				fr.add(pg[i]);
				System.out.println("Page " + pg[i] + " loaded into Frame " + fr.indexOf(pg[i]));
			}

			// Find the page to be replaced.
			else
			{
				int j = predict(pg, fr, pn, i + 1);
				int pageUnloaded = fr.get(j);
				fr.set(j, pg[i]);
				System.out.println("Page " + pageUnloaded + " unloaded from Frame " +
									j + ", Page " + pg[i] + " loaded into Frame " + j);
			}
		}
	
		System.out.print((pn - hit) + " page faults");
	}	
}
