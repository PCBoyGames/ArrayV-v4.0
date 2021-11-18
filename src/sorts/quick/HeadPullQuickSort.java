package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class HeadPullQuickSort extends Sort {
    public HeadPullQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Head Pull Quick");
        this.setRunAllSortsName("Head Pull Quick Sort");
        this.setRunSortName("Head Pull Quicksort");
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
        int right = 2;
        int pull = 1;
        boolean anyswaps = false;
        while (left != currentLength) {
            right = left + 1;
            anyswaps = false;
            while (right <= currentLength) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.001);
                if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                    pull = right - 1;
                    while (pull >= 1) {
                        Writes.swap(array, pull - 1, pull, 0.01, true, false);
                        pull--;
                    }
                    left++;
                    anyswaps = true;
                }
                right++;
            }
            if (anyswaps) left = 1;
            else left++;
        }
    }
}