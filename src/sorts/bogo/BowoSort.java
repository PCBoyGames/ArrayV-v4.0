package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Bowosort rotates a random part of the array <0,rand> a random amount of times until sorted.
 */
public final class BowoSort extends BogoSorting {
    public BowoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bowo");
        this.setRunAllSortsName("Bowo Sort");
        this.setRunSortName("Bowosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        while(!this.isArraySorted(array, length)) {
            int r = randInt(0, length),
                r2 = randInt(r, length);
            for(int i=0; i<r; i++)
                Writes.multiSwap(array, 0, r2, 0.01, true, false);
        }
    }
}
