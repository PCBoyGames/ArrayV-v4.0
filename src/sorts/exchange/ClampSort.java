package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ClampSort extends Sort {
    public ClampSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Clamp");
        this.setRunAllSortsName("Clamp Sort");
        this.setRunSortName("Clampsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(24);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 1; i < currentLength; i++) {
            for (int j = 0; j < i; j++) {
                if (Reads.compareIndices(array, i, j, 0.001, true) < 0) {
                    if (i - j > 2) Writes.reversal(array, j, i, 0.001, true, false);
                    else Writes.swap(array, j, i, 0.001, true, false);
                    i = 1;
                    j = -1;
                }
            }
        }
    }
}