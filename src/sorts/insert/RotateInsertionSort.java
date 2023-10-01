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
public class RotateInsertionSort extends OptimizedNaturalRotateMergeSort {
    public RotateInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rotate Insertion");
        this.setRunAllSortsName("Rotate Insertion Sort");
        this.setRunSortName("Rotate Insertsort");
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
            j = stableFindRun(array, i, currentLength, 0.5, true, false);
            merge(array, 0, i, j, 0);
        }
    }
}