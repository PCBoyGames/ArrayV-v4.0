package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ReverseClamberSort extends Sort {
    public ReverseClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Clamber");
        this.setRunAllSortsName("Reverse Clamber Sort");
        this.setRunSortName("Reverse Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = currentLength - 2; i >= 0; i--) for (int j = currentLength - 1; j > i; j--) if (Reads.compareIndices(array, i, j, 0.1, true) > 0) Writes.swap(array, i, j, 0.1, true, false);
    }
}