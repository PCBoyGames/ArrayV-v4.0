package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Gaming32

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Gaming32
 *
 */
public class OptimizedIndexRaikoSort extends Sort {

    public OptimizedIndexRaikoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Index Raiko");
        this.setRunAllSortsName("Optimized Index Raiko Sort");
        this.setRunSortName("Optimized Index Raikosort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean keyLessThan(int[] src, int[] pa, int a, int b) {
        int cmp = Reads.compareValues(src[pa[a]], src[pa[b]]);
        return cmp < 0 || (cmp == 0 && Reads.compareOriginalValues(a, b) < 0);
    }

    protected void siftDown(int[] src, int[] heap, int[] pa, int t, int r, int size) {
        while (2*r+2 < size) {
            int nxt = 2*r+1;
            int min = nxt + (this.keyLessThan(src, pa, heap[nxt], heap[nxt+1]) ? 0 : 1);
            if (this.keyLessThan(src, pa, heap[min], t)) {
                Writes.write(heap, r, heap[min], 0.25, true, true);
                r = min;
            }
            else break;
        }
        int min = 2*r+1;
        if (min < size && this.keyLessThan(src, pa, heap[min], t)) {
            Writes.write(heap, r, heap[min], 0.25, true, true);
            r = min;
        }
        Writes.write(heap, r, t, 0.25, true, true);
    }

    protected void kWayMerge(int[] arr, int[] idx, int[] heap, int[] pa, int[] pb, int size, int o) {
        for (int i = 0; i < size; i++)
            Writes.write(heap, i, i, 0, false, true);
        for (int i = (size-1)/2; i >= 0; i--)
            this.siftDown(arr, heap, pa, heap[i], i, size);
        for (int i = 0; size > 0; i++) {
            int min = heap[0];
            Highlights.markArray(2, pa[min]);
            Writes.write(idx, i, pa[min] - o, 0.5, false, true);
            Writes.write(pa, min, pa[min]+1, 0, false, true);
            if (pa[min] == pb[min])
                this.siftDown(arr, heap, pa, heap[--size], 0, size);
            else
                this.siftDown(arr, heap, pa, heap[0], 0, size);
        }
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
    }

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        boolean dir;
        if (i < b) dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else dir = true;
        while (i < b) {
            if (dir ^ Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                break;
            i++;
        }
        if (!dir)
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        while (i - a < mRun && i < b) {
            insertTo(array, i, expSearch(array, a, i, array[i]));
            i++;
        }
        Highlights.clearMark(2);
        return i;
    }

    protected void indexSort(int[] array, int[] keys, int a, int b) {
        for (int i = 0; i < b - a; i++) {
            Highlights.markArray(2, a + i);
            if (Reads.compareOriginalValues(i, keys[i]) != 0) {
                int t = array[a + i];
                int j = i, next = keys[i];
                do {
                    Writes.write(array, a + j, array[a + next], 0.0, true, false);
                    Writes.write(keys, j, j, 1.0, true, true);
                    j = next;
                    next = keys[next];
                } while (Reads.compareOriginalValues(next, i) != 0);
                Writes.write(array, a + j, t, 0.0, true, false);
                Writes.write(keys, j, j, 1.0, true, true);
            }
        }
        Highlights.clearMark(2);
    }

    public void mergeSort(int[] array, int a, int b) {
        int len = b - a;
        if (len <= 32) {
            // insertion sort
            findRun(array, a, b, b - a);
            return;
        }
        int mRun = 16;
        int[] runs = Writes.createExternalArray((b - a - 1) / mRun + 2);
        int r = a, rf = 0;
        while (r < b) {
            Writes.write(runs, rf++, r, 0.5, false, true);
            r = findRun(array, r, b, mRun);
        }
        int[] idx = Writes.createExternalArray(len);
        int alloc = 0;
        if (rf > 1) {
            int[] pa   = new int[rf];
            int[] pb   = new int[rf];
            int[] heap = new int[rf];
            alloc = 3 * rf;
            Writes.changeAllocAmount(alloc);
            Writes.arraycopy(runs, 0, pa, 0, rf, 0, false, true);
            Writes.arraycopy(pa, 1, pb, 0, rf - 1, 0, false, true);
            Writes.write(pb, rf - 1, b, 0, false, true);
            kWayMerge(array, idx, heap, pa, pb, rf, a);
            Highlights.clearAllMarks();
            indexSort(array, idx, a, b);
        }
        Writes.deleteExternalArray(idx);
        Writes.deleteExternalArray(runs);
        Writes.changeAllocAmount(-alloc);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
