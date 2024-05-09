package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class PCircleSort extends Sort {
    public PCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("P-Circle");
        this.setRunAllSortsName("P-Circle Sort");
        this.setRunSortName("P-Circlesort (Curslesort II)");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private boolean c(int[] array, int left, int right) {
    	if (left >= right)
    		return false;
    	int L = left, R = right;
    	boolean a, s;
    	a = s = false;
    	while (left < right) {
    		int l = left, r = right;
    		while (left < right - 1 && Reads.compareIndices(array, left, right, 0, true) <= 0) {
    			if (a) left++;
    			else right--;
    		}
    		a = !a;
    		do {
    			if (Reads.compareIndices(array, left, right, 0, true) > 0) {
    				s = true;
    				Writes.swap(array, left++, right--, 1, true, false);
    			} else {
    				left++; right--;
    				break;
    			}
    		} while (left < right);
    		if (left < right) {
    			c(array, l, left);
        		c(array, right, r);
    		}
    	}
    	if (right < R) {
    		s |= c(array, L, right);
    		s |= c(array, left, R);
    	}
    	return s;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
    	while (c(array, 0, sortLength-1));
    }
}