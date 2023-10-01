package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.MadhouseTools;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedInOrderShoveSort extends MadhouseTools {
    public OptimizedInOrderShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized In-Order Shove");
        this.setRunAllSortsName("Optimized In-Order Shove Sort");
        this.setRunSortName("Optimized In-Order Shove Sort");
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
        for (int left = minSorted(array, 0, currentLength, 0.1, true); left < currentLength; left = minSorted(array, left + 1, currentLength, 0.1, true)) {
            int right = left + 1;
            while (right < currentLength) {
                if (Reads.compareIndices(array, left, right, 0.01, true) > 0) {
                    Highlights.clearAllMarks();
                    Writes.insert(array, left, currentLength - 1, 0.01, true, false);
                    right = left + 1;
                } else right++;
            }
        }
    }
}