package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Stable Bokosort randomly swaps adjacent pairs of elements without changing
 * the equal elements' order until the array is sorted.
 * 
 * @author Flanlaina
 */
public class StableBokoSort extends BogoSorting {
    public StableBokoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Boko");
        this.setRunAllSortsName("Stable Boko Sort");
        this.setRunSortName("Stable Bokosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while (!this.isRangeSorted(array, 0, length, false, true)) {
            int index = BogoSorting.randInt(0, length-1);

            if (Reads.compareIndices(array, index, index+1, this.delay, true) != 0)
                Writes.swap(array, index, index+1, this.delay, true, false);
        }
    }
}
