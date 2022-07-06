package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Exchange Bogosort randomly sorts in shuffle order until the array is sorted.
 */
public final class ExchangeBogoSort extends BogoSorting {
    public ExchangeBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Exchange Bogo");
        this.setRunAllSortsName("Exchange Bogo Sort");
        this.setRunSortName("Exchange Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isRangeSorted(array, 0, length, false, true)) {
            for (int i = 0; i < length - 1; i++) {
                int j = BogoSorting.randInt(i, length);
                if (Reads.compareIndices(array, i, j, this.delay, true) == 1) {
                    Writes.swap(array, i, j, this.delay, true, false);
                }
            }
        }
    }
}
