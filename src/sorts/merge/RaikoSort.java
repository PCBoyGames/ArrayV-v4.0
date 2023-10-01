package sorts.merge;

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
public class RaikoSort extends Sort {

    public RaikoSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Raiko");
        this.setRunAllSortsName("Raiko Sort");
        this.setRunSortName("Raikosort");
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

    protected void kWayMerge(int[] src, int[] dest, int[] heap, int[] pa, int[] pb, int size, boolean aux) {
        for (int i = 0; i < size; i++)
            Writes.write(heap, i, i, 0, false, true);
        for (int i = (size-1)/2; i >= 0; i--)
            this.siftDown(src, heap, pa, heap[i], i, size);
        for (int i = 0; size > 0; i++) {
            int min = heap[0];
            Highlights.markArray(2, pa[min]);
            Writes.write(dest, i, src[pa[min]], 0.5, !aux, aux);
            Writes.write(pa, min, pa[min]+1, 0, false, true);
            if (pa[min] == pb[min])
                this.siftDown(src, heap, pa, heap[--size], 0, size);
            else
                this.siftDown(src, heap, pa, heap[0], 0, size);
        }
    }

    protected int findRun(int[] array, int a, int b) {
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
        Highlights.clearMark(2);
        return i;
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
        int[] buf = Writes.createExternalArray(len);
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
            kWayMerge(array, buf, heap, pa, pb, rf, true);
            Highlights.clearAllMarks();
            Writes.arraycopy(buf, 0, array, a, len, 1, true, false);
        }
        Writes.deleteExternalArray(buf);
        Writes.deleteExternalArray(runs);
        Writes.changeAllocAmount(-alloc);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
