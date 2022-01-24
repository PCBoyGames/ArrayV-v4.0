package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
  *  Corruptsort: An accidental O(n^2) find
  * @author Distay
  * Recursively merges Pairwise partitions together.
  */


final public class CorruptSort extends Sort {
    public CorruptSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Corrupt");
        this.setRunAllSortsName("Corrupt Sort");
        this.setRunSortName("Corruptsort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void comp(int[] array, int start, int end) {
    	if(Reads.compareIndices(array, start, end, 0.5, true) == 1) {
    		Writes.swap(array, start, end, 0.5, false, false);
    	}
    }
    private int cpotlt(int len) {
    	int z=1;
    	while(z<=len)
    		z*=2;
    	return z/2;
    }
    private void Pass(int[] array, int start, int end, int length, int gap, int target) {
    	for(int i=start; i<end; i++) {
    		int g2 = gap;
    		while(g2 > target) {
    			if(i+g2 < length)
    				this.comp(array, i, i+g2);
    			g2 /= 2;
    		}
    	}
    }
    private void Merge(int[] array, int start, int end, boolean base) {
    	if(end <= start)
    		return;
    	int mid = (start + end) / 2, g0 = mid-start, g1 = g0 / 2;
    	if(!base)
    		for(int i=start; i<mid; i++) {
	    		if(i+g0 < end)
	    			this.comp(array, i, i+g0);
	    	}
    	
    	if(start == mid)
    		return;
    	
    	if(end - start > 4)
    		this.Pass(array, start+g1, mid, end, g1, 1);
    	
    	this.Merge(array, start, mid, true);
    	this.Merge(array, mid, end, true);
    }
    private void sort(int[] array, int start, int end) {
    	if(end <= start)
    		return;
    	int mid = (start + end) / 2;
    	if(start == mid) {
    		this.Merge(array, start, end, false);
    	} else {
    		this.sort(array, start, mid);
    		this.sort(array, mid, end);
    		this.Merge(array, start, end, false);
    	}
    }
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
    	this.sort(array, 0, sortLength);
    	this.Pass(array, 0, sortLength, sortLength, cpotlt(sortLength)-1, 0);
    }
}