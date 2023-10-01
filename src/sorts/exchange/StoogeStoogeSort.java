package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class StoogeStoogeSort extends Sort {
    public StoogeStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stooge Stooge");
        this.setRunAllSortsName("Stooge Stooge Sort");
        this.setRunSortName("Stoogestoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setQuestion("Set disease stage:", 1);
        this.setBogoSort(false);
    }

	private void stoogeStoogeSort(int[] A, int i, int j, int d, int s) {
	    Delays.sleep(0.0025);

	    Highlights.markArray(1, i);
        Highlights.markArray(2, j);

        if (j - i + 1 >= 3) {
	        int t = (j - i + 1) / 3;

	        Highlights.markArray(3, j - t);
	        Highlights.markArray(4, i + t);
	        Writes.recordDepth(d++);

	        Writes.recursion(8);
	        stoogeStoogeSort(A, i, j-t, d, s);
	        stoogeStoogeSort(A, i+t, j, d, s);
	        stoogeStoogeSort(A, i+t, j, d, s);
	        stoogeStoogeSort(A, i, j-t, d, s);
	        stoogeStoogeSort(A, i, j-t, d, s);
	        stoogeStoogeSort(A, i+t, j, d, s);
	        stoogeStoogeSort(A, i+t, j, d, s);
	        stoogeStoogeSort(A, i, j-t, d, s);
	    }

        if (j - i + 1 >= Math.max(s, 0) + 1) {
        	for (int k = 0; k <= s; k++) {
        		int v = (j - i + 1) / (k + 1);
    	        Writes.recursion();
    	        stoogeStoogeSort(A, i, j, d, s - 1);
        		for (int l = 0; l <= k; l++) {
        	        Writes.recursion(2);
        	        stoogeStoogeSort(A, i, j - l * v, d, s - 1);
        	        stoogeStoogeSort(A, i + l * v, j, d, s - 1);
        		}
        	}
	        int t = (j - i + 1) / 3;

	        Highlights.markArray(3, j - t);
	        Highlights.markArray(4, i + t);
	        Writes.recordDepth(d);
	    }

	    if (Reads.compareValues(A[i], A[j]) == 1) {
	        Writes.swap(A, i, j, 0.005, true, false);
	    }
	}

	@Override
	public int validateAnswer(int s) {
		return Math.max(s, 1);
	}

    @Override
    public void runSort(int[] array, int currentLength, int stage) {
    	arrayVisualizer.setExtraHeading(String.format(", Disease Stage %d", stage));
        stoogeStoogeSort(array, 0, currentLength - 1, 0, stage - 1);
    }
}