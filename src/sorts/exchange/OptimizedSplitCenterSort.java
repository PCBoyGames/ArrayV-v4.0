package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedSplitCenterSort extends Sort {
    public OptimizedSplitCenterSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Split Center");
        this.setRunAllSortsName("Optimized Split Center Sort");
        this.setRunSortName("Optimized Split Center Sort");
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
        int way = 1;
        int swapless = 0;
        for (int runs = 1; swapless < 2 && runs < currentLength; runs++) {
            boolean anyswaps = false;
            for (int i = (int) Math.floor(currentLength / 2); i < currentLength && i > 0; i += way) if (Reads.compareIndices(array, i - 1, i, 0.005, true) > 0) Writes.swap(array, i - 1, i, 0.005, anyswaps = true, false);
            way *= -1;
            if (!anyswaps) swapless++;
            else swapless = 0;
        }
    }
}