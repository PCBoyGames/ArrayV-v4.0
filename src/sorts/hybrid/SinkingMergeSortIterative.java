package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 *
 */
public class SinkingMergeSortIterative extends Sort {

    public SinkingMergeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sinking Merge (Iterative)");
        this.setRunAllSortsName("Iterative Sinking Merge Sort");
        this.setRunSortName("Iterative Sinking Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void bubbleSort(int[] array, int start, int end, double sleep) {
        int consecSorted = 1;
        for (int i = end - 1; i > start; i -= consecSorted) {
            consecSorted = 1;
            for (int j = start; j < i; j++) {
                if (Reads.compareIndices(array, j, j + 1, sleep / 2.0, true) > 0) {
                    Writes.swap(array, j, j + 1, sleep, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
        }
    }
    
    public void sort(int[] array, int start, int end, double sleep) {
        int i;
        for (int j = 1; j < (end - start); j *= 2) {
            for (i = start; i + 2 * j <= end; i += 2 * j)
                bubbleSort(array, i, i+2*j, sleep);
            if (i + j < end)
                bubbleSort(array, i, end, sleep);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 0.25);

    }

}
