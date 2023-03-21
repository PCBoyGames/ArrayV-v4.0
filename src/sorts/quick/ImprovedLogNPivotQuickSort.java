package sorts.quick;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Meme Man
 *
 */
public final class ImprovedLogNPivotQuickSort extends Sort {

    public ImprovedLogNPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Improved Log(N) Pivot Quick");
        this.setRunAllSortsName("Improved Log(N) Pivot Quick Sort");
        this.setRunSortName("Improved Log(N) Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public static int log2(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }
    
    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.5, true, false);
            Writes.write(array, b, temp, 0.5, true, false);
        }
    }
    
    private int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b = m;
            else
                a = m + 1;
        }
        return a;
    }
    
    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, false);
    }
    
    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }
    
    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (4 * i + 1 < n) {
            int max = val;
            int next = i, child = 4 * i + 1;
            for (int j = child; j < Math.min(child + 4, n); j++) {
                if (Reads.compareValues(array[p + j], max) > 0) {
                    max = array[p + j];
                    next = j;
                }
            }
            if (next == i)
                break;
            Writes.write(array, p + i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p + i, val, 1, true, false);
    }

    protected void heapSort(int[] array, int a, int b) {
        int n = b - a;
        for (int i = (n - 1) / 4; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }
    
    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }

    protected void sortHelper(int[] array, int[] tmp, int a, int b, int depth) {
        if (b - a < 32) {
            insertSort(array, a, b);
            return;
        }
        if (depth == 0) {
            heapSort(array, a, b);
            return;
        }
        depth--;
        Highlights.clearAllMarks();
        Random rng = new Random();
        int n = b - a, size = log2(n);
        int s = (n - 1) / size + 1, c = 0;
        for (int i = a + rng.nextInt(s - 1); i < b; i += s, c++) {
            int loc = this.binSearch(tmp, 0, c, array[i], false);
            for (int j = c; j > loc; j--)
                Writes.write(tmp, j, tmp[j - 1], 0.25, true, true);
            Writes.write(tmp, loc, array[i], 1, true, true);
        }
        if (Reads.compareValues(tmp[0], tmp[c - 1]) == 0) {
            int i = a, j = b, piv = tmp[0];
            for (int k = i; k < j; k++) {
                if (Reads.compareValues(array[k], piv) < 0) {
                    swap(array, k, i++, 1, true, false);
                } else if (Reads.compareValues(array[k], piv) > 0) {
                    do {
                        j--;
                        Highlights.markArray(3, j);
                        Delays.sleep(1);
                    } while (j > k && Reads.compareValues(array[j], piv) > 0);
                    swap(array, k, j, 1, true, false);
                    Highlights.clearMark(3);
                    if (Reads.compareValues(array[k], piv) < 0) {
                        swap(array, k, i++, 1, true, false);
                    }
                }
            }
            this.sortHelper(array, tmp, a, i, depth);
            this.sortHelper(array, tmp, j, b, depth);
            return;
        }
        int bCnt = c + 1;
        int[] pa = new int[bCnt];
        int[] pb = new int[bCnt];
        Writes.changeAllocAmount(2 * bCnt);
        Writes.write(pa, 0, a, 0, false, true);
        Writes.write(pb, 0, a, 0, false, true);
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            int loc = this.binSearch(tmp, 0, c, array[i], true);
            Writes.write(pb, loc, pb[loc] + 1, 0, false, true);
        }
        for (int i = 1; i < bCnt; i++)
            Writes.write(pb, i, pb[i] + pb[i - 1], 0, false, true);
        Writes.arraycopy(pb, 0, pa, 1, bCnt - 1, 0, false, true);
        for (int i = 0; i < bCnt - 1; i++) {
            while (pa[i] < pb[i]) {
                int t = array[pa[i]], t0 = -1;
                boolean anyWrites = false;
                Highlights.markArray(4, pa[i]);
                int nxt = this.binSearch(tmp, 0, c, t, true), nxt0;
                while (nxt != i) {
                    Highlights.markArray(3, pa[nxt]);
                    nxt0 = this.binSearch(tmp, 0, c, array[pa[nxt]], true);
                    if (pa[nxt] != pb[nxt] - 1)
                        while (nxt0 == nxt) {
                            Writes.write(pa, nxt, pa[nxt] + 1, 0, false, true);
                            Highlights.markArray(3, pa[nxt]);
                            nxt0 = this.binSearch(tmp, 0, c, array[pa[nxt]], true);
                        }
                    anyWrites = true;
                    t0 = array[pa[nxt]];
                    Writes.write(array, pa[nxt], t, 0.5, true, false);
                    Writes.write(pa, nxt, pa[nxt] + 1, 0, false, true);
                    t = t0;
                    nxt = nxt0;
                }
                Highlights.clearAllMarks();
                if (anyWrites)
                    Writes.write(array, pa[i], t, 0.5, true, false);
                Writes.write(pa, i, pa[i] + 1, 0, false, true);
            }
        }
        this.sortHelper(array, tmp, a, pb[0], depth);
        for (int i = 1; i < bCnt; i++)
            this.sortHelper(array, tmp, pb[i - 1], pb[i], depth);
        Writes.changeAllocAmount(-2 * bCnt);
    }
    
    public void quickSort(int[] array, int a, int b) {
        if (b - a < 32) {
            insertSort(array, a, b);
            return;
        }
        int[] buf = Writes.createExternalArray(log2(b - a));
        sortHelper(array, buf, a, b, 2 * log2(b - a));
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
