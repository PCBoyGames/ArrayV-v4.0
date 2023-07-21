package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES
IN COLLABORATION WITH ACEOFSPADESPRODUC100

------------------------------
- THE MARIAM-TS SORT-O-MATIC -
------------------------------

NOTE: Not actually an Insertion sort.

*/
final public class WeirdInsertionSort extends Sort {
    public WeirdInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Weird \"Insertion\"");
        this.setRunAllSortsName("Weird \"Insertion\" Sort");
        this.setRunSortName("Weird \"Insertion\" Sort");
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
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 1; i < currentLength; i++) for (int j = i; j > 0 && Reads.compareIndices(array, j - 1, j, 0.05, true) > 0; j--) Writes.swap(array, i, j - 1, 0.05, true, sorted = false);
        }
    }
}