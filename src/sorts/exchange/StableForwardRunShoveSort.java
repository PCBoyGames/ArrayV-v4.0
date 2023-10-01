package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class StableForwardRunShoveSort extends Sort {
    public StableForwardRunShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Forward Run Shove");
        this.setRunAllSortsName("Stable Forward Run Shove Sort");
        this.setRunSortName("Stable Forward Run Shove Sort");
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
                if (Reads.compareIndices(array, left - 1, right - 1, 0.0125, true) > 0) {for (int pull = left; pull < right; pull++) if (Reads.compareIndices(array, pull - 1, pull, 0.0125, true) != 0) Writes.swap(array, pull - 1, pull, 0.0125, true, false);}
                else right--;
            }
        }
    }
}