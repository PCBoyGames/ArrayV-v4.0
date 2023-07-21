package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ThirtySort extends BogoSorting {
    public ThirtySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Thirty");
        this.setRunAllSortsName("Thirty Sort");
        this.setRunSortName("Thirty Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0, dir = 1; i < currentLength; i++, dir *= -1) {
            boolean escape = false;
            for (int j = dir == 1 ? i + 1 : currentLength - 1; ((dir == 1 && j < currentLength) || (dir == -1 && j > i)) && !escape; j += dir) if (Reads.compareIndices(array, i, j, 0.001, true) > 0) Writes.swap(array, i--, j, 0.01, escape = true, false);
        }
    }
}