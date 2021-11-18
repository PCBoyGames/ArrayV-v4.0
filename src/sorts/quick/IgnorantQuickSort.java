package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class IgnorantQuickSort extends Sort {
    public IgnorantQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ignorant Quick");
        this.setRunAllSortsName("Ignorant Quick Sort");
        this.setRunSortName("Ignorant Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int first = 1;
        int right = 2;
        int pull = 1;
        boolean anyswaps = false;
        while (left != currentLength) {
            right = left + 1;
            anyswaps = false;
            while (right <= currentLength) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                    if (!anyswaps && left != 1) first = left;
                    pull = right - 1;
                    while (pull >= left) {
                        Writes.swap(array, pull - 1, pull, 0.1, true, false);
                        pull--;
                    }
                    left++;
                    anyswaps = true;
                }
                right++;
            }
            if (anyswaps) left = first;
            else left++;
        }
    }
}