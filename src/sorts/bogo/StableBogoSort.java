package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * Stable Bogosort randomly shuffles the array without changing the equal
 * elements' order until the array is sorted.
 * 
 * @author aphitorite
 * @author Flanlaina
 */
public class StableBogoSort extends BogoSorting {
    public StableBogoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stable Bogo");
        this.setRunAllSortsName("Stable Bogo Sort");
        this.setRunSortName("Stable Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(true);
    }

    public void stableShuffle(int[] array, int a, int b) {
        for (int j = a + 1; j < b; j++)
            for (int i = j, rIdx = randInt(a, j + 1); i > rIdx; i--)
                if (Reads.compareIndices(array, i - 1, i, this.delay, true) != 0)
                    Writes.swap(array, i - 1, i, this.delay, false, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        while (!isArraySorted(array, sortLength))
            stableShuffle(array, 0, sortLength);
        
    }
}
