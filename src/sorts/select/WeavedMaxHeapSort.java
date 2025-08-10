package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.HeapSorting;

public class WeavedMaxHeapSort extends HeapSorting {
    public WeavedMaxHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Weaved Max Heap");
        this.setRunAllSortsName("Weaved Max Heap Sort");
        this.setRunSortName("Weaved Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void siftDown(int[] array, int a, int b, int hl, int tmp, boolean step) {
    	while(hl > 0) {
	    	int l = a + 1, r = a + (1 << hl--);
	    	int ll = r >= b || Reads.compareIndices(array, l, r, 0.5, true) > 0 ? l : r;
	    	if(ll < b && Reads.compareValueIndex(array, tmp, ll, 0.5, true) < 0) {
	    		Writes.write(array, a, array[ll], 2, true, false);
	    		a = ll; step = true;
	    	} else {
	    		break;
	    	}
    	}
		if(step) Writes.write(array, a, tmp, 1, true, false);
    }


    @Override
    public void runSort(int[] array, int length, int bucketCount) {
    	int mh = 0;
    	while((2 << ++mh) - 1 <= length);
    	for(int hv = 0; hv <= mh; hv++) {
    		for(int i = mh - hv, j = 1; i < length; i += (2 << hv) + Integer.numberOfTrailingZeros(j++) - 1) {
        		siftDown(array, i, length, hv, array[i], false);
    		}
    	}
    	for(int i = length - 1; i > 0; i--) {
    		int t = array[i];
    		Writes.write(array, i, array[0], 2.5, true, false);
    		siftDown(array, 0, i, mh, t, true);
    	}
    }
}