package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;


final public class StupidRoomSort extends Sort {
    public StupidRoomSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Stupid Room");
        this.setRunAllSortsName("Stupid Room Sort");
        this.setRunSortName("Stupid Roomsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    private boolean insertChange(int[] array, int start, int end) {
    	boolean c = true;
    	for(int i=start+1; i<end; i++) {
    		int t=array[i], j=i-1;
    		while(j>=start && Reads.compareValues(array[j], t) > 0) {
    			Writes.write(array, j+1, array[j], 1, true, false);
    			j--;
    		}
    		c = c && j == i - 1;
			Writes.write(array, j+1, t, 1, true, false);
    	}
    	return c;
    }
    
    private boolean wtfInsert(int[] array, int bound, int start, int end) {
    	boolean c = insertChange(array, bound, start);
    	for(int i=start; i<end; i++) {
    		int t=array[i], j=i-1;
    		while(j>=bound && Reads.compareValues(array[j], t) > 0) {
    			Writes.write(array, j+1, array[j], 1, true, false);
    			j--;
    		}
    		c = c && j == i - 1;
			Writes.write(array, j+1, t, 1, true, false);
			//if(i == start)
				bound = Math.max(j, bound);
    	}
    	return c;
    }
    
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	boolean c = false;
    	int k = 16;
		int consec = 0;
    	for(int j=0; j<currentLength && !c; j+=consec*16+1) {
    		boolean z = true;
        	for(int i=Math.max(k-16, 16); i<currentLength-j; i+=16) {
        		boolean f = this.wtfInsert(array, i-16, i, Math.min(i+16, currentLength));
        		if(f)
        			consec++;
        		else
        			consec = 0;
        		if(!f && z) {
        			z = false;
        			k = i - 16;
        		}
        		c = f && c;
        	}
    	}
    }
}