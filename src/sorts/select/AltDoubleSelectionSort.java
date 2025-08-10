package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;


public class AltDoubleSelectionSort extends Sort {
    public AltDoubleSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Alt. Double Selection");
        this.setRunAllSortsName("Alternate Double Selection Sort");
        this.setRunSortName("Alt. Double Selectsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	int ll = -1, lh = -1;
        for(int i = length - 1; length - i - 1 < i; i--) {
        	int ii = length - i - 1, jj = i;
        	if(Reads.compareIndices(array, ii, jj, 0.5, true) > 0) {
        		Writes.swap(array, ii, jj, 0.5, true, false);
        	}
        }
        for(int i = length - 1; length - i - 1 < i; i--) {
        	int ii = length - i - 1, jj = i, cl = ii, ch = jj;
        	if((cl == ll || ch == lh) && Reads.compareIndices(array, ii, jj, 0.5, true) > 0) {
        		Writes.swap(array, ii, jj, 0.5, true, false);
        	}
            for(int j = ii + 1, k = jj - 1; j <= k; j++, k--) {
                if((j == ll || k == lh) && j < k && Reads.compareIndices(array, j, k, 0.5, true) > 0) {
                    Writes.swap(array, j, k, 0.075, true, false);
                }
                if(Reads.compareIndices(array, j, cl, 0.033, true) < 0) {
                    cl = j;
                }
                if(cl < k && Reads.compareIndices(array, k, ch, 0.033, true) > 0) {
                  	ch = k;
                }
            }
            if((ll = cl) > ii) Writes.swap(array, ll, ii, 2.5, true, false);
            if((lh = ch) < jj) Writes.swap(array, lh, jj, 2.5, true, false);
        }
    }
}