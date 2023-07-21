package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

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
        for (int left = 1, first = 1; left != currentLength;) {
            boolean anyswaps = false;
            for (int right = left + 1; right <= currentLength; right++) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.01, true) > 0) {
                    if (!anyswaps && left != 1) first = left;
                    Highlights.clearMark(2);
                    Writes.insert(array, right - 1, left - 1, 0.01, anyswaps = true, false);
                    left++;
                }
            }
            if (anyswaps) left = first;
            else left++;
        }
    }
}