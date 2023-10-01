package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class BopoSort extends BogoSorting {
    public BopoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bopo");
        this.setRunAllSortsName("Bopo Sort");
        this.setRunSortName("Boposort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(10);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        if (isArraySorted(array, currentLength)) return;
        int excl = randInt(0, currentLength);
        boolean found = false;
        while (!found) {
            if (excl != currentLength - 1) Writes.swap(array, excl, currentLength - 1, delay, true, false);
            for (int r = 0; r < currentLength && !found; r++) {
                bogoSwap(array, 0, currentLength - 1, false);
                if (excl != currentLength - 1) Writes.swap(array, excl, currentLength - 1, delay, true, false);
                if (isArraySorted(array, currentLength)) found = true;
                if (!found) if (excl != currentLength - 1) Writes.swap(array, excl, currentLength - 1, delay, true, false);
            }
            excl = randInt(0, currentLength);
        }
    }
}