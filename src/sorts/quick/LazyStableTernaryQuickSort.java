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
public final class LazyStableTernaryQuickSort extends Sort {

    public LazyStableTernaryQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Lazy Stable Ternary Quick");
        setRunAllSortsName("Lazy Stable Ternary Quick Sort");
        setRunSortName("Lazy Stable Ternary Quicksort");
        setCategory("Quick Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    class PivotPair {
        public int pa, pb;

        public PivotPair(int pa, int pb) {
            this.pa = pa;
            this.pb = pb;
        }
    }

    int partialInsertLimit = 8;

    public static int floorLog(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }

    protected int medianOf3(int[] array, int v0, int v1, int v2) {
        int[] t = new int[2];
        int val;
        val = (Reads.compareIndices(array, v0, v1, 1, true) > 0) ? 1 : 0;
        t[0] = val;
        t[1] = val ^ 1;
        val = (Reads.compareIndices(array, v0, v2, 1, true) > 0) ? 1 : 0;
        t[0] += val;
        if (t[0] == 1)
            return v0;
        val = (Reads.compareIndices(array, v1, v2, 1, true) > 0) ? 1 : 0;
        t[1] += val;
        return t[1] == 1 ? v1 : v2;
    }

    protected int medianOf9(int[] array, int a, int b) {
        int v0, v1, v2, div = (b - a) / 9;
        v0 = medianOf3(array, a, a + div * 1, a + div * 2);
        v1 = medianOf3(array, a + div * 3, a + div * 4, a + div * 5);
        v2 = medianOf3(array, a + div * 6, a + div * 7, a + div * 8);
        return medianOf3(array, v0, v1, v2);
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b, double sleep) {
        IndexedRotations.holyGriesMills(array, a, m, b, sleep, true, false);
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

    protected void insertSort(int[] array, int a, int b) {
        buildRuns(array, a, b, b - a);
    }

    // Refactored from PDQSorting.java
    protected boolean partialInsert(int[] array, int a, int b) {
        if (a == b) return true;
        double sleep = 0.25;
        int c = 0;
        for (int i = a + 1; i < b; i++) {
            if (c > partialInsertLimit) return false;
            if (Reads.compareIndices(array, i - 1, i, sleep, true) > 0) {
                Highlights.clearMark(2);
                int t = array[i];
                int j = i;
                do {
                    Writes.write(array, j, array[j - 1], sleep, true, false);
                    j--;
                } while (j - 1 >= a && Reads.compareValues(array[j - 1], t) > 0);
                Writes.write(array, j, t, sleep, true, false);
                c += i - j;
            }
        }
        return true;
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = leftExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i, 1.0);
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
            rotate(array, i, m, b, 1.0);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = rightExpSearch(array, m, b, array[m - 1], true);
        }
    }

    public void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        if (Reads.compareIndices(array, a, b - 1, 0.0, true) > 0)
            rotate(array, a, m, b, 1.0);
        else if (b - m < m - a)
            inPlaceMergeBW(array, a, m, b);
        else
            inPlaceMergeFW(array, a, m, b);
    }

    public void lazyStableSort(int[] array, int a, int b) {
        int mRun = b - a;
        for (; mRun >= 32; mRun = (mRun + 1) / 2);
        if (buildRuns(array, a, b, mRun))
            return;
        for (int i, j = mRun; j < (b - a); j *= 2) {
            for (i = a; i + 2 * j <= b; i += 2 * j)
                smartInPlaceMerge(array, i, i + j, i + 2 * j);
            if (i + j < b)
                smartInPlaceMerge(array, i, i + j, b);
        }
    }

    protected PivotPair partition(int[] array, int a, int b, int pIdx) {
        Highlights.clearMark(2);
        int p = array[pIdx];
        int pa = a, pb = a, cmp = Reads.compareIndexValue(array, a, p, 0.5, true);
        for (int i = a; i < b;) {
            int j = i;
            if (cmp < 0) {
                do {
                    j++;
                    if (j < b) cmp = Reads.compareIndexValue(array, j, p, 0.5, true);
                } while (j < b && cmp < 0);
                rotate(array, pa, i, j, 0.25);
                pa += j - i;
                pb += j - i;
                i = j;
            } else if (cmp == 0) {
                do {
                    j++;
                    if (j < b) cmp = Reads.compareIndexValue(array, j, p, 0.5, true);
                } while (j < b && cmp == 0);
                rotate(array, pb, i, j, 0.25);
                pb += j - i;
                i = j;
            } else
                do {
                    i++;
                    if (i < b) cmp = Reads.compareIndexValue(array, i, p, 0.5, true);
                } while (i < b && cmp > 0);
        }
        Highlights.clearMark(2);
        return new PivotPair(pa, pb);
    }

    protected void quickSort(int[] array, int a, int b, int d) {
        while (b - a > 16) {
            if (d == 0) {
                lazyStableSort(array, a, b);
                return;
            }
            int piv = medianOf3(array, a, a + (b - a) / 2, b - 1);
            PivotPair p = partition(array, a, b, piv);
            int pa = p.pa, pb = p.pb;
            int l = pa - a, r = b - pb, eqLen = pb - pa;
            if (eqLen == b - a) return;
            if ((l == 0 || r == 0) || (l / r >= 16 || r / l >= 16)) {
                piv = medianOf9(array, a, b);
                p = partition(array, a, b, piv);
                pa = p.pa;
                pb = p.pb;
                l = pa - a;
                r = b - pb;
                eqLen = pb - pa;
                if (eqLen == b - a) return;
            }
            if (partialInsert(array, a, pa) && partialInsert(array, pb, b)) return;
            if (l > r) {
                quickSort(array, pb, b, --d);
                b = pa;
            } else {
                quickSort(array, a, pa, --d);
                a = pb;
            }
        }
        insertSort(array, a, b);
    }

    public void customSort(int[] array, int a, int b) {
        quickSort(array, a, b, 2 * floorLog(b - a));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
