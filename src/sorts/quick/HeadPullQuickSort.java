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
        while (left != currentLength) {
            boolean anyswaps = false;
            for (int right = left + 1; right <= currentLength; right++) if (Reads.compareIndices(array, left - 1, right - 1, 0.001, true) > 0) Writes.multiSwap(array, right - 1, 0, 0.001, anyswaps = true, false);
            if (anyswaps) left = 1;
            else left++;
        }
    }
}