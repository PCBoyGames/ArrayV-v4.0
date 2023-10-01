package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class InOrderShoveSort extends Sort {
    public InOrderShoveSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("In-Order Shove");
        this.setRunAllSortsName("In-Order Shove Sort");
        this.setRunSortName("In-Order Shove Sort");
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
                    Writes.multiSwap(array, left, currentLength - 1, 0.01, true, false);
                    right = left + 1;
                } else right++;
            }
        }
    }
}