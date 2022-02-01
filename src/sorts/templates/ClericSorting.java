package sorts.templates;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public abstract class ClericSorting extends Sort {
    protected ClericSorting(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
    }
    protected int end;
    protected int clericSortRoutine(int[] array, int lo, int hi, int swapCount, double sleep, int depth) {
        Writes.recordDepth(depth);
        if (lo == hi) return swapCount;
        int high = hi;
        int low = lo;
        int mid = (hi - lo) / 2;
        while (lo < hi) {
            if (hi < end && Reads.compareIndices(array, lo, hi, sleep / 2, true) > 0) {
                Highlights.markArray(3, lo);
                Highlights.markArray(4, hi);
                Writes.reversal(array, lo, hi, sleep, true, false);
                Highlights.clearAllMarks();
                swapCount++;
            }
            lo++;
            hi--;
        }
        Writes.recursion();
        swapCount = clericSortRoutine(array, low, low + mid, swapCount, sleep, depth + 1);
        if(low + mid + 1 < end) {
            Writes.recursion();
            swapCount = clericSortRoutine(array, low + mid + 1, high, swapCount, sleep, depth + 1);
        }
        return swapCount;
    }
}