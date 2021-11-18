package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedShoveSort extends Sort {
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
        int pull = 1;
        int running = 0;
        while (left < currentLength) {
            Highlights.markArray(1, left - 1);
            Highlights.markArray(2, left);
            Delays.sleep(0.0125);
            if (Reads.compareValues(array[left - 1], array[left]) > 0) {
                pull = left;
                while (pull < currentLength) {
                    Writes.swap(array, pull - 1, pull, 0.0125, true, false);
                    pull++;
                }
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