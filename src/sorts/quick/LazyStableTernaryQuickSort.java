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

    protected int leftBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            if (Reads.compareValues(val, array[m]) <= 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int rightBinSearch(int[] array, int a, int b, int val) {
        while (a < b) {
            int m = a + (b - a) / 2;
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m + 1;
        }
        return a;
    }

    protected int rightExpSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        return rightBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected int leftBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
            i *= 2;
        return rightBinSearch(array, a + i / 2, Math.min(b, a - 1 + i), val);
    }

    protected int rightBoundSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
            i *= 2;
        return leftBinSearch(array, Math.max(a, b - i + 1), b - i / 2, val);
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        boolean change = false;
        while (a > b) {
            Writes.write(array, a, array[--a], 0.5, true, false);
            change = true;
        }
        if (change)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b, double sleep) {
        IndexedRotations.holyGriesMills(array, a, m, b, sleep, true, false);
    }

    protected void insertSort(int[] array, int a, int b) {
        int i = a + 1;
        if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
            Writes.reversal(array, a, i - 1, 1.0, true, false);
        } else
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        Highlights.clearMark(2);
        for (; i < b; i++)
            insertTo(array, i, rightExpSearch(array, a, i, array[i]));
    }

    // Refactored from PDQSorting.java
    protected boolean partialInsert(int[] array, int a, int b) {
        if (a == b)
            return true;
        double sleep = 0.25;
        int c = 0;
        for (int i = a + 1; i < b; i++) {
            if (c > partialInsertLimit)
                return false;
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

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 1, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) > 0) i++;
                Writes.reversal(array, j, i - 1, 1, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 1, true) <= 0) i++;
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, rightBinSearch(array, j, i, array[i]));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        int i = a, j = m, k;
        while (i < j && j < b)
            if (Reads.compareValues(array[i], array[j]) == 1) {
                k = leftBinSearch(array, j + 1, b, array[i]);
                rotate(array, i, j, k, 1.0);
                i += k - j;
                j = k;
            } else
                i++;
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        int i = m - 1, j = b - 1, k;
        while (j > i && i >= a)
            if (Reads.compareValues(array[i], array[j]) > 0) {
                k = rightBinSearch(array, a, i, array[j]);
                rotate(array, k, i + 1, j + 1, 1.0);
                j -= (i + 1) - k;
                i = k - 1;
            } else
                j--;
    }

    public void smartInPlaceMerge(int[] array, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = leftBoundSearch(array, a, m, array[m]);
        b = rightBoundSearch(array, m, b, array[m - 1]);
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
                    cmp = Reads.compareIndexValue(array, j, p, 0.5, true);
                } while (j < b && cmp < 0);
                rotate(array, pa, i, j, 0.25);
                pa += j - i;
                pb += j - i;
                i = j;
            } else if (cmp == 0) {
                do {
                    j++;
                    cmp = Reads.compareIndexValue(array, j, p, 0.5, true);
                } while (j < b && cmp == 0);
                rotate(array, pb, i, j, 0.25);
                pb += j - i;
                i = j;
            } else
                do {
                    i++;
                    cmp = Reads.compareIndexValue(array, i, p, 0.5, true);
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
            if (eqLen == b - a)
                return;
            if ((l == 0 || r == 0) || (l / r >= 16 || r / l >= 16)) {
                piv = medianOf9(array, a, b);
                p = partition(array, a, b, piv);
                pa = p.pa;
                pb = p.pb;
                l = pa - a;
                r = b - pb;
                eqLen = pb - pa;
                if (eqLen == b - a)
                    return;
            }
            if (partialInsert(array, a, pa) && partialInsert(array, pb, b))
                return;
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
        quickSort(array, 0, sortLength, 2 * floorLog(sortLength));

    }

}
