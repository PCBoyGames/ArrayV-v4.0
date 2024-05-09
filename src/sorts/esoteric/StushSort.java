package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StushSort extends Sort {
    public StushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stush");
        this.setRunAllSortsName("Stush Sort");
        this.setRunSortName("Stushsort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private void stush(int[] array, int a, int b) {
    	int l = b - a + 1;
    	if (l > 1) {
    		boolean dc = false;
    		if (Reads.compareIndices(array, a, b, 0.01, true) > 0) {
    			if (dc) {
    			    Writes.multiSwap(array, a, b, 0.1, true, false);
    			} else {
    				IndexedRotations.centered(array, a, a + l / 2, b, 1, true, false);
    				Writes.swap(array, a + l / 2, a + l / 2 - 1, 1, true, false);
    			}
    		}
    	}
    	if (l >= 3) {
    		int q = Math.max(l / 4, 1);
    		
    		stush(array, a, b - l / 2);
    		stush(array, a + l / 2, b);
    		stush(array, a + q, b - q);
    		stush(array, a, a + q);
    		stush(array, a, a + l / 3);
    		stush(array, b - q, b);
    		stush(array, b - l / 3, b);
    		stush(array, a + q, b - q);
    	}
    }
    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
    	this.stush(array, 0, currentLength-1);
    	this.stush(array, 0, currentLength/2);
    	this.stush(array, currentLength/2-1, currentLength-1);
    }
}