package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public final class DurakSort extends BogoSorting {

    public DurakSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Durak");
        this.setRunAllSortsName("Durak Sort");
        this.setRunSortName("Duraksort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(11);
        this.setBogoSort(true);
    }
    
    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!isArraySorted(array, sortLength)) {
            if (randBoolean()) swap(array, 0, randInt(0, sortLength), delay, true, false);
            else swap(array, randInt(0, sortLength), sortLength - 1, delay, true, false);
        }

    }

}
