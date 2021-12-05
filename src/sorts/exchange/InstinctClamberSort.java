package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class InstinctClamberSort extends Sort {
    public InstinctClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Instinct Clamber");
        this.setRunAllSortsName("Instinct Clamber Sort");
        this.setRunSortName("Instinct Clambersort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength; i++) for (int j = 0; j < i; j++) {
            Highlights.markArray(1, i);
            Highlights.markArray(2, j);
            Delays.sleep(0.05);
            Writes.swap(array, i, j, 0.1, true, false);
            if (Reads.compareIndices(array, i, j, 0.05, true) < 0) Writes.swap(array, i, j, 0.05, true, false);
        }
    }
}