package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Yuri-chan
 *
 */
public final class UnoptimizedReverseBubbleSort extends Sort {

    public UnoptimizedReverseBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unoptimized Reverse Bubble");
        this.setRunAllSortsName("Unoptimized Reverse Bubble Sort");
        this.setRunSortName("Unoptimized Reverse Bubblesort");
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
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = sortLength - 1; i > 0; i--) {
                if (Reads.compareIndices(array, i - 1, i, 0.05, true) > 0) {
                    Writes.swap(array, i - 1, i, 0.075, true, false);
                    sorted = false;
                }
            }
        }

    }

}
