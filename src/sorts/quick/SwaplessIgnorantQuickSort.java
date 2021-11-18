package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SwaplessIgnorantQuickSort extends Sort {
    public SwaplessIgnorantQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Ignorant Quick");
        this.setRunAllSortsName("Swapless Ignorant Quick Sort");
        this.setRunSortName("Swapless Ignorant Quicksort");
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
        int item = 0;
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
                    item = array[pull];
                    Highlights.clearMark(2);
                    while (pull >= left) {
                        Writes.write(array, pull, array[pull - 1], 0.1, true, false);
                        pull--;
                    }
                    Writes.write(array, pull, item, 0.1, true, false);
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