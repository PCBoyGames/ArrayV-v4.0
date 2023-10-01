package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class IgnorantQuickSort extends Sort {
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
        boolean anyswaps = false;
        while (left != currentLength) {
            anyswaps = false;
            for (int right = left + 1; right <= currentLength; right++) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.01, true) > 0) {
                    if (!anyswaps && left != 1) first = left;
                    Writes.multiSwap(array, right - 1, left - 1, 0.01, anyswaps = true, false);
                    left++;
                }
            }
            if (anyswaps) left = first;
            else left++;
        }
    }
}