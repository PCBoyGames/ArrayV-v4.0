package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class BoyoSort extends BogoSorting {

    public BoyoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Boyo");
        this.setRunAllSortsName("Boyo Sort");
        this.setRunSortName("Boyosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    protected void boyoSwap(int[] array, int a, int b) {
        int gap = BogoSorting.randInt(1, b - a);
        if (BogoSorting.randBoolean()) {
            for (int i = a; gap + i < b; i++) {
                Writes.swap(array, i, i + gap, this.delay, true, false);
            }
        } else {
            for (int i = b - 1; i >= a + gap; i--) {
                Writes.swap(array, i - gap, i, this.delay, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!this.isArraySorted(array, sortLength))
            boyoSwap(array, 0, sortLength);

    }

}
