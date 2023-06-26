package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveClamberSort extends Sort {
    public AdaptiveClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Clamber");
        this.setRunAllSortsName("Adaptive Clamber Sort");
        this.setRunSortName("Adaptive Clambersort");
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
        for (int right = 1; right < currentLength; right++) if (Reads.compareIndices(array, right - 1, right, 1, true) > 0) for (int left = 0; left < right; left++) if (Reads.compareIndices(array, left, right, 0.1, true) > 0) while (left < right) Writes.swap(array, left++, right, 0.2, true, false);
    }
}