package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedShoveSort extends Sort {
    public OptimizedShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Shove");
        this.setRunAllSortsName("Optimized Shove Sort");
        this.setRunSortName("Optimized Shove Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int running = 0;
        while (left < currentLength) {
            if (Reads.compareIndices(array, left - 1, left, 0.0125, true) > 0) {
                Writes.multiSwap(array, left - 1, currentLength - 1, 0.0125, true, false);
                if (left > 1) left--;
                running++;
                if (running >= currentLength - left) {
                    Writes.reversal(array, left, currentLength - 1, 1.25, true, false);
                    running = 0;
                }
            } else left++;
        }
    }
}