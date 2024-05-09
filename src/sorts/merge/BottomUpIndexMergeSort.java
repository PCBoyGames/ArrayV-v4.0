package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi
extending code by aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 * @author aphitorite
 *
 */
public class BottomUpIndexMergeSort extends Sort {

    public BottomUpIndexMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bottom-up Index Merge");
        this.setRunAllSortsName("Bottom-up Index Merge Sort");
        this.setRunSortName("Bottom-up Index Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void indexSort(int[] array, int[] keys, int a, int b) {
        for (int i = 0; i < b - a; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                int t = array[a+i];
                int j = i, next = keys[i];

                do {
                    Writes.write(array, a+j, array[a+next], 0.0, true, false);
                    Writes.write(keys, j, j, 0.5, true, true);

                    j = next;
                    next = keys[next];
                }
                while (Reads.compareOriginalValues(next, i) != 0);

                Writes.write(array, a+j, t, 0.0, true, false);
                Writes.write(keys, j, j, 0.5, true, true);
            }
        }
        Highlights.clearMark(2);
    }

    protected void merge(int[] array, int[] idx, int a, int m, int b) {
        int i = a, j = m, c = 0;
        while (i < m || j < b) {
            if (i < m) Highlights.markArray(1, i);
            else Highlights.clearMark(1);
            if (j < b) Highlights.markArray(2, j);
            else Highlights.clearMark(2);
            if (i < m && (j >= b || Reads.compareIndices(array, i, j, 0, false) <= 0))
                Writes.write(idx, c, i++ - a, 0.5, false, true);
            else
                Writes.write(idx, c, j++ - a, 0.5, false, true);
            c++;
        }
        Highlights.clearMark(2);
        indexSort(array, idx, a, b);
    }

    public void sort(int[] array, int a, int b) {
        int length = b - a;
        int[] idx = Writes.createExternalArray(length);
        for (int j = 1; j < length; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                merge(array, idx, i,  i + j, Math.min(i + 2 * j, b));
        Writes.deleteExternalArray(idx);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        sort(array, 0, length);

    }

}
