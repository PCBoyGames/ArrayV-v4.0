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
public class ExchangeMergeSortRecursive extends Sort {

    public ExchangeMergeSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Merge (Recursive)");
        this.setRunAllSortsName("Recursive Exchange Merge Sort");
        this.setRunSortName("Recursive Exchange Mergesort");
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
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        sort(array, a, m);
        sort(array, m, b);
        exchangeMerge(array, a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
