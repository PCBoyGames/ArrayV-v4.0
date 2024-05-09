package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Kiriko-chan

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 *
 */
public final class LazierQuickSort extends Sort {

    public LazierQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazier Stable Quick");
        this.setRunAllSortsName("Lazier Stable Quick Sort");
        this.setRunSortName("Lazier Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class PivotPair {
        public int l, r;

        public PivotPair(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    public static int floorLog(int n) {
        int log = 0;
        while ((n >>= 1) != 0) ++log;
        return log;
    }

    protected int medianOf3(int[] array, int v0, int v1, int v2) {
        int[] t = new int[2];
        int val;
        val = (Reads.compareIndices(array, v0, v1, 1, true) > 0)? 1 : 0;
        t[0]  = val; t[1] = val^1;
        val = (Reads.compareIndices(array, v0, v2, 1, true) > 0)? 1 : 0;
        t[0] += val;
        if (t[0] == 1) return v0;
        val = (Reads.compareIndices(array, v1, v2, 1, true) > 0)? 1 : 0;
        t[1] += val;
        return t[1] == 1 ? v1 : v2;
    }

    protected int medianOf9(int[] array, int a, int b) {
        int v0, v1, v2, div = (b - a)/9;

        v0 = this.medianOf3(array, a,        a+div*1,  a+div*2);
        v1 = this.medianOf3(array, a+div*3,  a+div*4,  a+div*5);
        v2 = this.medianOf3(array, a+div*6,  a+div*7,  a+div*8);

        return this.medianOf3(array, v0, v1, v2);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.holyGriesMills(array, a, m, b, 1.0, true, false);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = leftExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = leftExpSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = rightExpSearch(array, a, m, array[b - 1], false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = rightExpSearch(array, m, b, array[m - 1], true);
        }
    }

    public void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareIndices(array, a, b - 1, 0.0, true) > 0)
            rotate(array, a, m, b);
        else if (b - m < m - a)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                if (i - j < 4) Writes.swap(array, j, i - 1, 1.0, true, false);
                else Writes.reversal(array, j, i - 1, 1.0, true, false);
            } else while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, binSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void insertionSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.125, true, false);
        if (a != b) Writes.write(array, b, temp, 0.125, true, false);
    }

    public void lazyStableSort(int[] array, int start, int end) {
        int mRun = end - start;
        for (; mRun >= 32; mRun = (mRun + 1) / 2);
        if (buildRuns(array, start, end, mRun)) return;
        for (int i, j = mRun; j < (end - start); j *= 2) {
            for (i = start; i + 2 * j <= end; i += 2 * j)
                smartInPlaceMerge(array, i, i + j, i + 2 * j);
            if (i + j < end)
                smartInPlaceMerge(array, i, i + j, end);
        }

    }

    protected PivotPair partition(int[] array, int a, int b, int piv) {
        int l = a, r = a;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if (cmp < 0) {
                insertTo(array, i, l++);
                r++;
            } else if (cmp == 0)
                insertTo(array, i, r++);
        }
        return new PivotPair(l, r);
    }

    protected void quickSort(int[] array, int a, int b, int depth) {
        while (b - a > 16) {
            if (depth == 0) {
                lazyStableSort(array, a, b);
                return;
            }
            int p = medianOf3(array, a, a + (b - a) / 2, b - 1);
            PivotPair m = partition(array, a, b, array[p]);
            int l = m.l - a, r = b - m.r, eqCnt = m.r - m.l;
            if (eqCnt == b - a) return;
            if ((l == 0 || r == 0) || (l / r >= 16 || r / l >= 16)) {
                p = medianOf9(array, a, b);
                m = partition(array, a, b, array[p]);
                l = m.l - a;
                r = b - m.r;
                eqCnt = m.r - m.l;
                if (eqCnt == b - a) return;
            }
            if (l > r) {
                quickSort(array, m.r, b, --depth);
                b = m.l;
            } else {
                quickSort(array, a, m.l, --depth);
                a = m.r;
            }
        }
        insertionSort(array, a, b);
    }

    public void customSort(int[] array, int a, int b) {
        quickSort(array, a, b, 2 * floorLog(b - a));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
