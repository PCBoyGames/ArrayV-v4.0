package sorts.insert;

import main.ArrayVisualizer;
import sorts.merge.OptimizedNaturalRotateMergeSort;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE BY AYAKO-CHAN AND APHITORITE
AS AN INDIRECT VARIANT OF OPTIMIZED NATURAL ROTATE MERGE

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedRotateInsertionSort extends OptimizedNaturalRotateMergeSort {
    public OptimizedRotateInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Rotate Insertion");
        this.setRunAllSortsName("Optimized Rotate Insertion Sort");
        this.setRunSortName("Optimized Rotate Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        insertlimit = 0;
        int j;
        for (int i = 0; i < currentLength; i = j) {
            j = mergeFindRun(array, i, currentLength);
            merge(array, 0, i, j, 0);
        }
    }
}