package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author PCBoy
 *
 */
public final class UnstableSmartSingularityQuickSort extends Sort {

    public UnstableSmartSingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unstable Smart Singularity Quick");
        this.setRunAllSortsName("Unstable Smart Singularity Quick Sort");
        this.setRunSortName("Unstable Smart Singularity Quicksort");
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

    int depthlimit;
    int insertlimit;

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

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    protected int findReverseRun(int[] array, int start, int end) {
        int reverse = start;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp > 0) different = true;
            reverse++;
            if (reverse + 1 < end)
                cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (reverse < start + 3) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
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

    // get rank of r between [a,a+g...b)
    int gaprank(int[] array, int a, int b, int g, int r) {
        int re = 0;
        while (a < b) {
            if (a != r) {
                if (Reads.compareIndices(array, a, r, 0.25, true) < 0) re++;
            }
            a += g;
        }
        return re;
    }

    int findPivot(int[] array, int a, int b, int rIdx) {
        // 2^(log(b-a)/2)
        int s = 1;
        while (s*s<b-a) s*=2;
        s /= 2;
        if (s < 2) s = 1;
        int mid = (b-a-1)/(2*s)+1, cm = a+(b-a)/2, cr = 0, rLen = rIdx - a;
        int[] pivs;
        if (rLen == 2)
            pivs = new int[] {a, rIdx - 1};
        else if (rLen % 2 == 0)
            pivs = new int[] {a, a + (rLen - 1) / 2, a + rLen / 2, rIdx - 1};
        else
            pivs = new int[] {a, a + rLen / 2, rIdx - 1};
        boolean change = false;
        for (int i=0; i < pivs.length; i++) {
            int r = gaprank(array, a, b, s, pivs[i]);
            if (Math.abs(cr - mid) > Math.abs(r - mid)) {
                cm = pivs[i];
                cr = r;
                change = true;
            }
        }
        if (!change) cm = rIdx - 1;
        return cm;
    }

    protected int[] partition(int[] array, int a, int b, int piv) {
        // partition -> [a][E < piv][i][E == piv][j][E > piv][b]
        // returns -> i and j ^
        int i1 = a, i = a - 1, j = b, j1 = b;
        for (;;) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0)
                    swap(array, i1++, i, 1, true, false);
                else if (cmp > 0)
                    break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0)
                    swap(array, --j1, j, 1, true, false);
                else if (cmp < 0)
                    break;
            }
            Highlights.clearMark(2);
            if (i < j) {
                // The patch is not needed here, because it never swaps when i == j.
                Writes.swap(array, i, j, 1, true, false);
                Highlights.clearMark(2);
            } else {
                if (i1 == b)
                    return new int[] { a, b };
                else if (j < i)
                    j++;
                if (i1 - a > i - i1) {
                    int i2 = i;
                    i = a;
                    while (i1 < i2) swap(array, i++, i1++, 1, true, false);
                }
                else while (i1 > a) swap(array, --i, --i1, 1, true, false);
                if (b - j1 > j1 - j) {
                    int j2 = j;
                    j = b;
                    while (j1 > j2) swap(array, --j, --j1, 1, true, false);
                }
                else while (j1 < b) swap(array, j++, j1++, 1, true, false);
                break;
            }
        }
        return new int[] { i, j };
    }

    void innerSort(int[] array, int a, int b, int depth, int rep, int d) {
        Writes.recordDepth(d);
        while (b - a > insertlimit) {
            if (depth >= depthlimit || rep >= 4) {
                heapSort(array, a, b);
                return;
            }
            findReverseRun(array, a, b);
            int rIdx = a + 1;
            while (rIdx < b && Reads.compareIndices(array, rIdx - 1, rIdx, 0.125, true) <= 0)
                rIdx++;
            if (rIdx >= b)
                return;
            int pIdx = findPivot(array, a, b, rIdx);
            int[] pr = partition(array, a, b, array[pIdx]);
            int lLen = pr[0] - a, rLen = b - pr[1], eqLen = pr[1] - pr[0];
            if (eqLen == b - a)
                return;
            boolean bad;
            if (lLen == 0)
                bad = Math.min(eqLen, rLen) < Math.max(eqLen, rLen) / 16;
            else if (rLen == 0)
                bad = Math.min(eqLen, lLen) < Math.max(eqLen, lLen) / 16;
            else
                bad = Math.min(lLen, rLen) < Math.max(lLen, rLen) / 16;
            rep = bad ? rep + 1 : 0;
            depth++;
            if (rLen < lLen) {
                Writes.recursion();
                innerSort(array, pr[1], b, depth, rep, d + 1);
                b = pr[0];
            } else {
                Writes.recursion();
                innerSort(array, a, pr[0], depth, rep, d + 1);
                a = pr[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        boolean sorted = true;
        for (int i = a; i < b - 1; i++) {
            if (Reads.compareIndices(array, i, i + 1, 0.5, true) > 0) {
                sorted = false;
                break;
            }
        }
        if (sorted)
            return;
        depthlimit = (int) Math.min(Math.sqrt(b - a), 2 * log2(b - a));
        insertlimit = Math.max(depthlimit / 2, 16);
        if (findReverseRun(array, a, b) + 1 < b) {
            innerSort(array, a, b, 0, 0, 0);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
