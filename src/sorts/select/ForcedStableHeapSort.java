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
public final class ForcedStableHeapSort extends Sort {

    public ForcedStableHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Forced Stable Heap");
        this.setRunAllSortsName("Forced Stable Heap Sort");
        this.setRunSortName("Forced Stable Heapsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    void siftDown(int[] array, int[] keys, int val, int i, int p, int n, int kVal) {
        while (2 * i + 1 < n) {
            int max = val;
            int kMax = kVal;
            int next = i, child = 2 * i + 1;
            for (int j = child; j < Math.min(child + 2, n); j++) {
                int cmp = Reads.compareValues(array[p + j], max);
                if (cmp > 0 || (cmp == 0 && Reads.compareOriginalValues(keys[j], kMax) > 0)) {
                    max = array[p + j];
                    kMax = keys[j];
                    next = j;
                }
            }
            if (next == i) break;
            Writes.write(array, p + i, max, 0, true, false);
            Writes.write(keys, i, kMax, 1, false, true);
            i = next;
        }
        Writes.write(array, p + i, val, 0, true, false);
        Writes.write(keys, i, kVal, 1, false, true);
    }
    
    public void customSort(int[] array, int a, int b) {
        int n = b - a;
        int[] keys = Writes.createExternalArray(n);
        for (int i = 0; i < n; i++) {
            Highlights.markArray(1, a + i);
            Writes.write(keys, i, i, 0.5, false, true);
        }
        for (int i = (n - 1) / 2; i >= 0; i--)
            this.siftDown(array, keys, array[a + i], i, a, n, keys[i]);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            int tk = keys[i];
            Writes.write(array, a + i, array[a], 0, false, false);
            Writes.write(keys, i, keys[0], 1, false, true);
            this.siftDown(array, keys, t, 0, a, i, tk);
        }
        Writes.deleteExternalArray(keys);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
