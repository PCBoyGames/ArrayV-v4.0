package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Exchange Bozosort randomly sorts any two elements until the array is sorted.
 */
public final class ExchangeBozoSort extends BogoSorting {
    public ExchangeBozoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Exchange Bozo");
        this.setRunAllSortsName("Exchange Bozo Sort");
        this.setRunSortName("Exchange Bozosort");
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
            int index1 = BogoSorting.randInt(0, length),
                index2 = BogoSorting.randInt(0, length);

            int comp = Reads.compareIndices(array, index1, index2, this.delay, true);
            if (index1 < index2 ? comp > 0 : comp < 0)
                Writes.swap(array, index1, index2, this.delay, true, false);
        }
    }
}
