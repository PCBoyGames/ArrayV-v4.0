package sorts.quick;

import java.util.Random;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class ImprovedKWayQuickSort extends Sort {

    public ImprovedKWayQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Improved K-Way Quick");
        this.setRunAllSortsName("Improved K-Way Quick Sort");
        this.setRunSortName("Improved K-Way Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the number of pivots for this sort:", 4);
    }
    
    @Override
    public int validateAnswer(int answer) {
        return Math.max(answer, 1);
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
        int a1, b1;
        boolean dir = Reads.compareIndexValue(array, a + (b - a) / 2, val, 0, false) <= 0;
        if (dir) {
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        } else {
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        }
        return binSearch(array, a1, b1, val, false);
    }
    
    protected void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (i >= b)
            return;
        if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }
    
    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }

    protected void sortHelper(int[] array, int[] tmp, int a, int b, int pivCount) {
        if (b - a < 32) {
            insertSort(array, a, b);
            return;
        }
        Highlights.clearAllMarks();
        Random rng = new Random();
        int n = b - a, size = Math.min(n, pivCount);
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
            this.sortHelper(array, tmp, a, i, pivCount);
            this.sortHelper(array, tmp, j, b, pivCount);
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
                boolean t0IsValid = false;
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
                    t0IsValid = true;
                    t0 = array[pa[nxt]];
                    Writes.write(array, pa[nxt], t, 0.5, true, false);
                    Writes.write(pa, nxt, pa[nxt] + 1, 0, false, true);
                    t = t0;
                    nxt = nxt0;
                }
                Highlights.clearAllMarks();
                if (t0IsValid)
                    Writes.write(array, pa[i], t, 0.5, true, false);
                Writes.write(pa, i, pa[i] + 1, 0, false, true);
            }
        }
        this.sortHelper(array, tmp, a, pb[0], pivCount);
        for (int i = 1; i < bCnt; i++)
            this.sortHelper(array, tmp, pb[i - 1], pb[i], pivCount);
        Writes.changeAllocAmount(-2 * bCnt);
    }
    
    public void quickSort(int[] array, int a, int b, int pivCount) {
        int[] buf = Writes.createExternalArray(pivCount);
        sortHelper(array, buf, a, b, pivCount);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength, bucketCount);

    }

}
