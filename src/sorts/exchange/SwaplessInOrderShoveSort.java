package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SwaplessInOrderShoveSort extends Sort {
    public SwaplessInOrderShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless In-Order Shove");
        this.setRunAllSortsName("Swapless In-Order Shove Sort");
        this.setRunSortName("Swapless In-Order Shove Sort");
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
        int right;
        for (int left = 0; left < currentLength; left++) {
            right = left + 1;
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