package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Stable Bomosort pulls an element in the array to another location within the
 * array without changing the equal elements' order until the array is sorted.
 * 
 * @author aphitorite
 * @author Flanlaina
 */
public class StableBomoSort extends BogoSorting {
    public StableBomoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Bomo");
        this.setRunAllSortsName("Stable Bomo Sort");
        this.setRunSortName("Stable Bomosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    public void pull(int[] array, int a, int b) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, this.delay, true) != 0) {
                    Writes.swap(array, i, i + 1, delay, true, false);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, this.delay, true) != 0) {
                    Writes.swap(array, i, i - 1, delay, true, false);
                }
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!isArraySorted(array, sortLength))
            pull(array, randInt(0, sortLength), randInt(0, sortLength));
    }
}
