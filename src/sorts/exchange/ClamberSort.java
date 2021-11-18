package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ClamberSort extends Sort {
    public ClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Clamber");
        this.setRunAllSortsName("Clamber Sort");
        this.setRunSortName("Clambersort");
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
        for (int i = 1; i < currentLength; i++) for (int j = 0; j < i; j++) if (Reads.compareIndices(array, i, j, 0.1, true) < 0) Writes.swap(array, i, j, 0.1, true, false);
    }
}