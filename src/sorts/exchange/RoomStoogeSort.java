package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class RoomStoogeSort extends Sort {
    public RoomStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Room Stooge");
        this.setRunAllSortsName("thatsOven's Room Stooge Sort");
        this.setRunSortName("thatsOven's Room Stooge Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public boolean isSorted(int[] a, int n) {
        for (int i=0; i < n-1; i++) {
            if (Reads.compareValues(a[i], a[i+1]) > 0) {
                return false;
            }
        }
        return true;
    }

    public void stoogeBubble(int[] A, int i, int j) {
        if (Reads.compareValues(A[i], A[j]) == 1) {
	        Writes.swap(A, i, j, 0.005, true, false);
	    }

	    Delays.sleep(0.0025);

	    Highlights.markArray(1, i);
        Highlights.markArray(2, j);

        if (j - i + 1 >= 3) {
	        int t = (j - i + 1) / 3;

	        Highlights.markArray(3, j - t);
	        Highlights.markArray(4, i + t);

	        this.stoogeBubble(A, i, j-t);
	        this.stoogeBubble(A, i+t, j);
	    }
	}

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        while (!this.isSorted(array, currentLength)) {
            this.stoogeBubble(array, 0, currentLength-1);
        }
    }
}