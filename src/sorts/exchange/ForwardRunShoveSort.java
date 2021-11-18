package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ForwardRunShoveSort extends Sort {
    public ForwardRunShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Forward Run Shove");
        this.setRunAllSortsName("Forward Run Shove Sort");
        this.setRunSortName("Forward Run Shove Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(2048);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int right = currentLength;
        int pull = 1;
        while (left != currentLength) {
            right = currentLength;
            while (left < right) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.0125);
                if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                    pull = left;
                    while (pull < right) {
                        Writes.swap(array, pull - 1, pull, 0.0125, true, false);
                        pull++;
                    }
                } else right--;
            }
            left++;
        }
    }
}