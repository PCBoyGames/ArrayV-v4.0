package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Lancewer
 * @author Flanlaina
 *
 */
public class ExchangeMergeSortIterative extends Sort {
    public ExchangeMergeSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Merge (Iterative)");
        this.setRunAllSortsName("Iterative Exchange Merge Sort");
        this.setRunSortName("Iterative Exchange Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void exchangeMerge(int[] array, int a, int m, int b) {
        for (int i = a; i < m; i++) {
            for (int j = b - 1; j >= m; j--) {
                if (Reads.compareIndices(array, i, j, 0.05, true) > 0)
                    Writes.swap(array, i, j, 0.05, true, false);
            }
        }
    }

    public void sort(int[] array, int a, int b) {
        for (int j = 1; j < b - a; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                exchangeMerge(array, i, i + j, Math.min(i + 2 * j, b));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
