package sorts.esoteric;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class HypocriteInsertionSort extends Sort {
    public HypocriteInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Hypocrite Insertion");
        this.setRunAllSortsName("Hypocrite Insertion Sort");
        this.setRunSortName("Hypocrite Insertion Sort");
        this.setCategory("Esoteric Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) {
            int val = currentLength - 1;
            for (int j = i; i >= 0; i--) Writes.write(array, j, val--, 0.25, true, false);
        }
    }
}