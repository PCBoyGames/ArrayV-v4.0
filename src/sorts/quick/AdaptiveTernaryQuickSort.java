package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 *
 */
public class AdaptiveTernaryQuickSort extends Sort {

    public AdaptiveTernaryQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Ternary Quick");
        this.setRunAllSortsName("Adaptive Ternary Quick Sort");
        this.setRunSortName("Adaptive Ternary Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static int insertThreshold = 32;
    static int partialInsertLimit = 8;

    //Refactored from PDQSorting.java
    protected boolean partialInsert(int[] array, int a, int b) {
        if (a == b) return true;
        double sleep = 0.25;
        int c = 0;
        for (int i = a + 1; i < b; i++) {
            if (c > partialInsertLimit) return false;
            if (Reads.compareIndices(array, i - 1, i, sleep, true) > 0) {
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

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0) b1 = m;
            else a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        double sleep = 0.5;
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            int dest = expSearch(array, a, i, current);
            int pos = i - 1;
            while (pos >= dest) {
                Writes.write(array, pos + 1, array[pos], sleep, true, false);
                pos--;
            }
            if (pos + 1 < i) Writes.write(array, pos + 1, current, sleep, true, false);
        }
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    public int medP3(int[] array, int a, int b, int d) {
        if (b - a == 3 || (b - a > 3 && d == 0))
            return medOf3(array, a, a + (b - a) / 2, b - 1);
        if (b - a < 3) return a + (b - a) / 2;
        int t = (b - a) / 3;
        int l = medP3(array, a, a + t, --d), c = medP3(array, a + t, b - t, d), r = medP3(array, b - t, b, d);
        // median
        return medOf3(array, l, c, r);
    }

    public int medOfMed(int[] array, int a, int b) {
        int log5 = 0, exp5 = 1, exp5_1 = 0;
        int[] indices = new int[5];
        int n = b - a;
        while (exp5 < n) {
            exp5_1 = exp5;
            log5++;
            exp5 *= 5;
        }
        if (log5 < 1) return a;
        // fill indexes, recursing if required
        if (log5 == 1) for (int i = a, j = 0; i < b; i++, j++) indices[j] = i;
        else {
            n = 0;
            for (int i = a; i < b; i += exp5_1) {
                indices[n] = medOfMed(array, i, Math.min(b, i + exp5_1));
                n++;
            }
        }
        // sort - insertion sort is good enough for 5 elements
        for (int i = 1; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if (Reads.compareIndices(array, indices[j], indices[j - 1], 0.5, true) < 0) {
                    int t = indices[j];
                    indices[j] = indices[j - 1];
                    indices[j - 1] = t;
                } else break;
            }
        }
        // return median
        return indices[(n - 1) / 2];
    }

    // partition -> [a][E < piv][i][E == piv][j][E > piv][b]
    // returns -> i, j ^, and whether the list was already correctly partitioned
    public int[] partition(int[] array, int a, int b, int piv) {
        // determines which elements do not need to be moved
        for (; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[a], piv) >= 0) break;
        }
        for (; b > a; b--) {
            Highlights.markArray(1, b - 1);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[b - 1], piv) <= 0) break;
        }
        int i1 = a, i = a - 1, j = b, j1 = b;
        while (true) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i >= j) {
                if (i1 == b) return new int[] { a, b, 1 };
                if (j < i) j++;
                if (i1 - a > i - i1) {
                    int i2 = i;
                    i = a;
                    while (i1 < i2) swap(array, i++, i1++, 1, true, false);
                } else while (i1 > a) swap(array, --i, --i1, 1, true, false);
                if (b - j1 > j1 - j) {
                    int j2 = j;
                    j = b;
                    while (j1 > j2) swap(array, --j, --j1, 1, true, false);
                } else while (j1 < b) swap(array, j++, j1++, 1, true, false);
                return new int[] { i, j, 0 };
            }
            // The patch is not needed here, because it never swaps when i == j.
            Writes.swap(array, i, j, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    void sortHelper(int[] array, int a, int b, boolean bad) {
        while (b - a > insertThreshold) {
            int pIdx;
            if (bad) {
                pIdx = medOfMed(array, a, b);
                bad = false;
            } else pIdx = medP3(array, a, b, 1);
            int[] pr = partition(array, a, b, array[pIdx]);
            int lSize = pr[0] - a, rSize = b - pr[1], eqSize = pr[1] - pr[0];
            if (eqSize == b - a) return;
            if (rSize == 0) bad = eqSize < lSize / 8;
            else if (lSize == 0) bad = eqSize < rSize / 8;
            else bad = lSize < rSize / 8 || rSize < lSize / 8;
            if (!bad && pr[2] != 0 && partialInsert(array, a, pr[0]) && partialInsert(array, pr[1], b)) {
                return;
            }
            if (rSize < lSize) {
                sortHelper(array, pr[1], b, bad);
                b = pr[0];
            } else {
                sortHelper(array, a, pr[0], bad);
                a = pr[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        sortHelper(array, a, b, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
