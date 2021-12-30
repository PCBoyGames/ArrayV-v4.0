package sorts.hybrid;

import java.util.ArrayList;

import main.ArrayVisualizer;
import sorts.insert.BinaryInsertionSort;
import sorts.merge.RotateMergeSort;
import sorts.templates.GrailSorting;

// Image Sorting: Chaos
final public class GrimmageSort extends GrailSorting {
    public GrimmageSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Grimmage");
        this.setRunAllSortsName("Grimmage Sort");
        this.setRunSortName("Grimmage Sort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
	// This sort gets part of its name from another sort I made,
    // "Image Sort." It used a really weird method that kind of
    // ties in with this one.
    
	private void multiSwap(int[] array, int a, int b, int s) {
		for(int i=0; i<s; i++) {
			Writes.swap(array,a+i,b+i,1,true,false);
		}
	}
	
	private int seek(int[] array, int start, int end) {
		int size = 1, seek = start + 1,
			direction = Reads.compareValues(array[start], array[start+1]);
		if(direction == 0)
			direction = -1;
		for(int i=start+2; i<end && seek < end - 1; i++) {
			if(Reads.compareValues(array[seek], array[i]) == direction) {
				Writes.multiSwap(array, i, ++seek, 0.01, true, false);
				size++;
			}
		}
		if(direction == 1) {
			Writes.reversal(array, start, seek, 1, true, false);
		}
		return seek;
	}
	
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
    	int k = 0, runs = 1;
    	while(k < sortLength) {
    		int tempK = k;
    		k = this.seek(array, k, sortLength);
    		if(runs > 1) {
    			this.grailMergeWithoutBuffer(array, 0, tempK, k - tempK);
    		}
    		runs++;
    	}
    }
}