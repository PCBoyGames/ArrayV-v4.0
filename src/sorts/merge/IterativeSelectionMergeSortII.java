package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
extending code by _fluffyy and PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author _fluffyy
 * @author PCBoy
 *
 */
public class IterativeSelectionMergeSortII extends Sort {

    public IterativeSelectionMergeSortII(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Iterative Selection Merge II");
        this.setRunAllSortsName("Iterative Selection Merge Sort II");
        this.setRunSortName("Iterative Selection Mergesort II");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void merge(int[] arr, int start, int mid, int stop) {
        if (Reads.compareIndices(arr, mid - 1, mid, 0.25, true) <= 0) return;
        int last = mid + 1;
        int orig = mid;

        for (int i = start; i < stop - 1; i++) {
            if (orig == i) orig++;
            int minIdx = i;
            for (int j = orig; j < last; j++) {
                if (Reads.compareIndices(arr, j, minIdx, 0.25, false) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) Writes.swap(arr, i, minIdx, 1.0, true, false);
            if (minIdx == last - 1 && last < stop) last++;
        }
    }
    
    public void sort(int[] array, int a, int b) {
        for (int j = 1; j < b - a; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                merge(array, i, i + j, Math.min(i + 2 * j, b));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
