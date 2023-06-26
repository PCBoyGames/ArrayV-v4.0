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
public final class TernarySingularityQuickSort extends Sort {

    public TernarySingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary Singularity Quick");
        this.setRunAllSortsName("Ternary Singularity Quick Sort");
        this.setRunSortName("Ternary Singularity Quicksort");
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
    int replimit;

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b)
            return;
        int p0 = a, p1 = m - 1, p2 = m, p3 = b - 1;
        int tmp;
        while (p0 < p1 && p2 < p3) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p2], 0.5, true, false);
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p0 < p1) {
            tmp = array[p1];
            Writes.write(array, p1--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, tmp, 0.5, true, false);
        }
        while (p2 < p3) {
            tmp = array[p2];
            Writes.write(array, p2++, array[p3], 0.5, true, false);
            Writes.write(array, p3--, array[p0], 0.5, true, false);
            Writes.write(array, p0++, tmp, 0.5, true, false);
        }
        if (p0 < p3) { // don't count reversals that don't do anything
            if (p3 - p0 >= 3)
                Writes.reversal(array, p0, p3, 1, true, false);
            else
                Writes.swap(array, p0, p3, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        for (int i = a; i > b; i--)
            Writes.write(array, i, array[i - 1], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
    }

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.75, true, false);
        else Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    protected int findReverseRun(int[] array, int start, int end) {
        int reverse = start;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (reverse + 1 < end && cmp >= 0) {
            if (cmp == 0)
                lessunique = true;
            else
                different = true;
            reverse++;
            if (reverse + 1 < end)
                cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (lessunique)
                stableSegmentReversal(array, start, reverse);
            else if (reverse < start + 3)
                Writes.swap(array, start, reverse, 0.75, true, false);
            else
                Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
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

    protected int expSearch(int[] array, int a, int b, int val, boolean dir, boolean left) {
        int i = 1;
        int a1, b1;
        if (dir) {
            if (left)
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                    i *= 2;
            else
                while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                    i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        } else {
            if (left)
                while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                    i *= 2;
            else
                while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                    i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        }
        return binSearch(array, a1, b1, val, left);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = expSearch(array, m, b, array[a], true, true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (a >= m)
                break;
            a = expSearch(array, a, m, array[m], true, false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b) {
        while (b > m && m > a) {
            int i = expSearch(array, a, m, array[b - 1], false, false);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a)
                break;
            b = expSearch(array, m, b, array[m - 1], false, true);
        }
    }

    protected void merge(int[] array, int a, int m, int b, int d) {
        Writes.recordDepth(d);
        if (a >= m || m >= b)
            return;
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = expSearch(array, a, m, array[m], true, false);
        b = expSearch(array, m, b, array[m - 1], false, true);
        int lenA = m - a, lenB = b - m;
        if (lenA <= 16 || lenB <= 16) {
            if (m - a > b - m)
                inPlaceMergeBW(array, a, m, b);
            else
                inPlaceMergeFW(array, a, m, b);
            return;
        }
        int c = lenA + (lenB - lenA) / 2;
        if (lenB < lenA) { // partitions c largest elements
            int r1 = 0, r2 = lenB;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[m - (c - ml)], array[b - ml - 1]) > 0)
                    r2 = ml;
                else
                    r1 = ml + 1;
            }
            // [lenA-(c-r1)][c-r1][lenB-r1][r1]
            // [lenA-(c-r1)][lenB-r1][c-r1][r1]
            this.rotate(array, m - (c - r1), m, b - r1);
            int m1 = b - c;
            Writes.recursion();
            this.merge(array, m1, b - r1, b, d + 1);
            Writes.recursion();
            this.merge(array, a, m1 - (lenB - r1), m1, d + 1);
        } else { // partitions c smallest elements
            int r1 = 0, r2 = lenA;
            while (r1 < r2) {
                int ml = r1 + (r2 - r1) / 2;
                if (Reads.compareValues(array[a + ml], array[m + (c - ml) - 1]) > 0)
                    r2 = ml;
                else
                    r1 = ml + 1;
            }
            // [r1][lenA-r1][c-r1][lenB-(c-r1)]
            // [r1][c-r1][lenA-r1][lenB-(c-r1)]
            this.rotate(array, a + r1, m, m + (c - r1));
            int m1 = a + c;
            Writes.recursion();
            this.merge(array, a, a + r1, m1, d + 1);
            Writes.recursion();
            this.merge(array, m1, m1 + (lenA - r1), b, d + 1);
        }
    }

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        boolean dir;
        if (i < b)
            dir = Reads.compareIndices(array, i - 1, i++, 0.5, true) <= 0;
        else
            dir = true;
        if (dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, 1.0, true, false);
            else
                Writes.reversal(array, a, i - 1, 1.0, true, false);
        }
        Highlights.clearMark(2);
        while (i - a < mRun && i < b) {
            insertTo(array, i, expSearch(array, a, i, array[i], false, false));
            i++;
        }
        return i;
    }

    protected void insertSort(int[] array, int a, int b) {
        // technically an insertion sort
        findRun(array, a, b, b - a);
    }

    public void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        int mRun = b - a;
        while (mRun >= 32)
            mRun = (mRun - 1) / 2 + 1;
        while (true) {
            i = findRun(array, a, b, mRun);
            if (i >= b)
                return;
            j = findRun(array, i, b, mRun);
            merge(array, a, i, j, 0);
            if (j >= b)
                return;
            k = j;
            while (true) {
                i = findRun(array, k, b, mRun);
                if (i >= b)
                    break;
                j = findRun(array, i, b, mRun);
                merge(array, k, i, j, 0);
                if (j >= b)
                    break;
                k = j;
            }
        }
    }

    protected int[] partition(int[] array, int a, int b, int piv, int d) {
        Writes.recordDepth(d);
        if (b - a < 2) {
            int[] court = new int[] { a, a };
            int cmp = Reads.compareValues(array[a], piv);
            if (cmp < 0) {
                court[0]++;
                court[1]++;
            } else if (cmp == 0)
                court[1]++;
            return court;
        }
        int m = a + (b - a) / 2;
        Writes.recursion();
        int[] l = partition(array, a, m, piv, d + 1);
        Writes.recursion();
        int[] r = partition(array, m, b, piv, d + 1);
        int l1 = l[0] - a, l2 = l[1] - l[0];
        int r1 = r[0] - m, r2 = r[1] - r[0];
        rotate(array, a + l1, m, m + r1);
        rotate(array, a + l1 + l2 + r1, m + r1, m + r1 + r2);
        return new int[] { a + l1 + r1, a + l1 + r1 + l2 + r2 };
    }

    protected void sortHelper(int[] array, int a, int b, int depth, int rep, int d) {
        Writes.recordDepth(d);
        while (b - a > insertlimit) {
            if (depth >= depthlimit || rep >= 4) {
                mergeSort(array, a, b);
                return;
            }
            int pIdx = a + 1;
            while (pIdx < b && Reads.compareIndices(array, pIdx - 1, pIdx, 0.5, true) <= 0)
                pIdx++;
            if (pIdx >= b)
                return;
            int[] p = partition(array, a, b, array[pIdx - 1], 0);
            if (p[1] - p[0] == b - a)
                return;
            rep = Math.min(b - p[1], p[0] - a) <= replimit ? rep + 1 : 0;
            depth++;
            if (b - p[1] < p[0] - a) {
                Writes.recursion();
                sortHelper(array, p[1], b, depth, rep, d + 1);
                b = p[0];
            } else {
                Writes.recursion();
                sortHelper(array, a, p[0], depth, rep, d + 1);
                a = p[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        depthlimit = (int) Math.min(Math.sqrt(b - a), 2 * log2(b - a));
        insertlimit = Math.max(depthlimit / 2, 16);
        replimit = Math.max(depthlimit / 4, 2);
        if (findReverseRun(array, a, b) + 1 < b)
            sortHelper(array, a, b, 0, 0, 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
