package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan
 *
 */
public final class OptimizedReverseBubbleSort extends Sort {

    public OptimizedReverseBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Reverse Bubble");
        this.setRunAllSortsName("Optimized Reverse Bubble Sort");
        this.setRunSortName("Optimized Reverse Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        int consecSorted;
        for (int i = 0; i < sortLength - 1; i += consecSorted) {
            consecSorted = 1;
            for (int j = sortLength - 1; j > i; j--) {
                if (Reads.compareIndices(array, j - 1, j, 0.05, true) > 0) {
                    Writes.swap(array, j - 1, j, 0.075, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
        }
    }

}
