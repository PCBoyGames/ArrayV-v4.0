package sorts.templates;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public abstract class ShircleSorting extends Sort {
    protected ShircleSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }
	protected int end;
    protected int shircleSortRoutine(int[] array, int lo, int hi, int swapCount, double sleep, int depth) { 
        Writes.recordDepth(depth);       
        if (lo == hi) return swapCount;
        int high = hi;
        int low = lo;
        int mid = (hi - lo) / 2;
        int pull, item;
        while (lo < hi) {
			while (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0) {
                pull = lo;
                item = array[pull];
                Highlights.markArray(2, lo);
                Highlights.markArray(3, hi);
                while (pull < hi) {
				    Writes.write(array, pull, array[pull + 1], sleep, true, false);
                    pull++;
                }
                Highlights.clearMark(3);
                Writes.write(array, pull, item, sleep, true, false);
				swapCount++;
			}
            lo++;
            hi--;
        }
        Writes.recursion();
        swapCount = shircleSortRoutine(array, low, low + mid, swapCount, sleep, depth + 1);
		if(low + mid + 1 < end) {
            Writes.recursion();
            swapCount = shircleSortRoutine(array, low + mid + 1, high, swapCount, sleep, depth + 1);
        }
        return swapCount;
    }
}