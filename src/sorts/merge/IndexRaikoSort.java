package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
in collaboration with aphitorite and Gaming32

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki (Ayako-chan)
 * @author aphitorite
 * @author Gaming32
 *
 */
public class IndexRaikoSort extends Sort {

    public IndexRaikoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Index Raiko");
        this.setRunAllSortsName("Index Raiko Sort");
        this.setRunSortName("Index Raikosort");
        this.setCategory("Merge Sorts");
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
        while(2*r+2 < size) {
            int nxt = 2*r+1;
            int min = nxt + (this.keyLessThan(src, pa, heap[nxt], heap[nxt+1]) ? 0 : 1);
            if(this.keyLessThan(src, pa, heap[min], t)) {
                Writes.write(heap, r, heap[min], 0.25, true, true);
                r = min;
            }
            else break;
        }
        int min = 2*r+1;
        if(min < size && this.keyLessThan(src, pa, heap[min], t)) {
            Writes.write(heap, r, heap[min], 0.25, true, true);
            r = min;
        }
        Writes.write(heap, r, t, 0.25, true, true);
    }

    protected void kWayMerge(int[] arr, int[] idx, int[] heap, int[] pa, int[] pb, int size, int o) {
        for(int i = 0; i < size; i++)
            Writes.write(heap, i, i, 0, false, true);
        for(int i = (size-1)/2; i >= 0; i--)
            this.siftDown(arr, heap, pa, heap[i], i, size);
        for(int i = 0; size > 0; i++) {
            int min = heap[0];
            Highlights.markArray(2, pa[min]);
            Writes.write(idx, i, pa[min] - o, 0.5, false, true);
            Writes.write(pa, min, pa[min]+1, 0, false, true);
            if(pa[min] == pb[min])
                this.siftDown(arr, heap, pa, heap[--size], 0, size);
            else 
                this.siftDown(arr, heap, pa, heap[0], 0, size);
        }
    }
    
    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b) return i;
        boolean dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        while (i < b) {
            if (dir ^ Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) break;
            i++;
        }
        if (!dir) {
            if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
            else Writes.reversal(array, a, i - 1, 1.0, true, false);
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
        if (len < 2) return;
        int[] runs = Writes.createExternalArray((len - 1) / 2 + 2);
        int r = a, rf = 0;
        while (r < b) {
            Writes.write(runs, rf++, r, 0.5, false, true);
            r = findRun(array, r, b);
        }
        Writes.write(runs, rf, b, 0.5, false, true);
        int[] idx = Writes.createExternalArray(len);
        int alloc = 0;
        if (rf > 1) {
            int[] pa   = new int[rf];
            int[] pb   = new int[rf];
            int[] heap = new int[rf];
            alloc = 3 * rf;
            Writes.changeAllocAmount(alloc);
            for (int i = 0; i < rf; i++) {
                Writes.write(pa, i, runs[i], 0, false, true);
                Writes.write(pb, i, runs[i + 1], 0, false, true);
            }
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
