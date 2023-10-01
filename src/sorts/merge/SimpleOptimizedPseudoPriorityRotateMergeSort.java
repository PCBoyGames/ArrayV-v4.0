package sorts.merge;

import main.ArrayVisualizer;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE BY AYAKO-CHAN AND APHITORITE

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class SimpleOptimizedPseudoPriorityRotateMergeSort extends OptimizedPseudoPriorityRotateMergeSort {
    public SimpleOptimizedPseudoPriorityRotateMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Simple Optimized Pseudo-Priority Rotate Merge");
        this.setRunAllSortsName("Simple Optimized Pseudo-Priority Rotate Merge Sort");
        this.setRunSortName("Simple Optimized Pseudo-Priority Rotate Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int mergeFindRun(int[] array, int a, int b) {
        int i = stableFindRun(array, a, b, 0.5, true, false);
        int j;
        for (; i < a + seglimit && i < b; i = j) {
            j = stableFindRun(array, i, b, 0.5, true, false);
            // Simple insertions.
            for (int h = i; h < j; h++) {
                int k = maxExponentialSearch(array, a, h, array[h], false, 0.5, true);
                if (k < h) Writes.insert(array, h, k, 0.5, true, false);
                else break;
            }
        }
        return i;
    }

    // No attemptSmall or run tracking.
    public void sOPPRMS(int[] array, int start, int end) {
        // Disables fallbacks.
        insertlimit = 0;
        int i, j, k;
        while (true) {
            i = mergeFindRun(array, start, end);
            if (i >= end) break;
            j = mergeFindRun(array, i, end);
            merge(array, 0, i, j);
            if (j >= end) break;
            k = j;
            while (true) {
                i = mergeFindRun(array, k, end);
                if (i >= end) break;
                j = mergeFindRun(array, i, end);
                merge(array, k, i, j);
                if (j >= end) break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        sOPPRMS(array, 0, currentLength);
    }
}