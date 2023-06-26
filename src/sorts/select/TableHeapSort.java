package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public final class TableHeapSort extends Sort {

    public TableHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Table Heap");
        this.setRunAllSortsName("Table Heap Sort");
        this.setRunSortName("Table Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    void siftDown(int[] array, int[] keys, int i, int p, int n, int kVal) {
        while (2 * i + 1 < n) {
            int kMax = kVal;
            int next = i, child = 2 * i + 1;
            for (int j = child; j < Math.min(child + 2, n); j++) {
                int cmp = Reads.compareIndices(array, keys[p + j], kMax, 0.25, true);
                if (cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j], kMax) > 0)) {
                    kMax = keys[j];
                    next = j;
                }
            }
            if (next == i) break;
            Highlights.markArray(3, p + i);
            Writes.write(keys, i, kMax, 0.5, false, true);
            i = next;
        }
        Highlights.markArray(3, p + i);
        Writes.write(keys, i, kVal, 0.5, false, true);
    }
    
    public void customSort(int[] array, int a, int b) {
        int len = b - a;
        int[] keys = Writes.createExternalArray(len);
        for (int i = 0; i < len; i++) {
            Highlights.markArray(1, a + i);
            Writes.write(keys, i, i, 0.5, false, true);
        }
        for (int i = (len - 1) / 2; i >= 0; i--)
            this.siftDown(array, keys, i, a, len, keys[i]);
        for (int i = len - 1; i > 0; i--) {
            Highlights.markArray(3, a + i);
            int t = keys[i];
            Writes.write(keys, i, keys[0], 0.5, false, true);
            this.siftDown(array, keys, 0, a, i, t);
        }
        Highlights.clearMark(3);
        for (int i = 0; i < len; i++) {
            Highlights.markArray(2, i);
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                int t = array[a + i];
                int j = i, next = keys[i];
                do {
                    Writes.write(array, a + j, array[a + next], 1.0, true, false);
                    Writes.write(keys, j, j, 1.0, true, true);
                    j = next;
                    next = keys[next];
                } while (Reads.compareOriginalValues(next, i) != 0);
                Writes.write(array, a + j, t, 1.0, true, false);
                Writes.write(keys, j, j, 1.0, true, true);
            }
        }
        Highlights.clearMark(2);
        Writes.deleteExternalArray(keys);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
