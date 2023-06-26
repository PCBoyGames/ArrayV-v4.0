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
        for (int left = 1; left != currentLength; left++) {
            int right = currentLength;
            while (left < right) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.125, true) > 0) Writes.multiSwap(array, left - 1, right - 1, 0.0125, true, false);
                else right--;
            }
        }
    }
}