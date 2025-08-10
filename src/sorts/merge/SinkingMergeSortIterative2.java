package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Flanlaina

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Flanlaina
 *
 */
public class SinkingMergeSortIterative2 extends Sort {
    public SinkingMergeSortIterative2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sinking Merge (Iterative II)");
        this.setRunAllSortsName("Iterative Sinking Merge Sort");
        this.setRunSortName("Iterative Sinking Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void bubbleMerge(int[] array, int start, int mid, int end) {
        int swap = end;
        while (swap > start && mid > start) {
            int lastSwap = start;
            for (int i = mid; i < swap; i++) {
                if (Reads.compareIndices(array, i - 1, i, 0.025, true) > 0) {
                    Writes.swap(array, i, i - 1, 0.025, true, false);
                    lastSwap = i;
                } else break;
            }
            swap = lastSwap;
            mid--;
        }
    }

    public void sort(int[] array, int a, int b) {
        for (int j = 1; j < b - a; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                bubbleMerge(array, i, i + j, Math.min(i + 2 * j, b));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
