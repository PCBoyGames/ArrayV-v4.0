package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedOddEvenSort extends Sort {
    public OptimizedOddEvenSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Odd-Even");
        this.setRunAllSortsName("Optimized Odd-Even Sort");
        this.setRunSortName("Optimized Odd-Even Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int i = 0;
        int run = 0;
        int count = 0;
        while (i + 1 < currentLength && count < currentLength) {
            count++;
            if (i > 0) i--;
            while (Reads.compareIndices(array, i, i + 1, 0.025, true) <= 0 && i + 1 < currentLength) i++;
            if (i + 1 < currentLength) Writes.swap(array, i, i + 1, 0.075, true, false);
            run = i + 2;
            while (run + 1 < currentLength) {
                if (Reads.compareIndices(array, run, run + 1, 0.025, true) > 0) Writes.swap(array, run, run + 1, 0.075, true, false);
                run += 2;
            }
        }
    }
}