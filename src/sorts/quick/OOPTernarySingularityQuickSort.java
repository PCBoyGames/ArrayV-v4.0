package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite, Distray and PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author Distray
 * @author PCBoy
 *
 */
public final class OOPTernarySingularityQuickSort extends Sort {

    public OOPTernarySingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("OOP Ternary Singularity Quick");
        this.setRunAllSortsName("Out-of-Place Ternary Singularity Quick Sort");
        this.setRunSortName("Out-of-Place Ternary Singularity Quicksort");
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

    // Conjoined Gries-Mills rotations (by Distray)
    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        int lenA = m - a, lenB = b - m, pos = a;
        int end = pos + lenA + lenB;
        while (lenA > 0 && lenB > 0)
            if (lenA < lenB) {
                for (int i = 0; i < lenA; i++) {
                    int t = array[pos + i], j = pos + i + lenA;
                    for (; j < end; j += lenA)
                        Writes.write(array, j - lenA, array[j], 1, true, false);
                    Writes.write(array, j - lenA, t, 1, true, false);
                }
                pos += lenB;
                lenB %= lenA;
                lenA -= lenB;
            } else {
                for (int i = 0; i < lenB; i++) {
                    int t = array[pos + i + lenA], j = pos + i + lenA - lenB;
                    for (; j >= pos; j -= lenB)
                        Writes.write(array, j + lenB, array[j], 1, true, false);
                    Writes.write(array, j + lenB, t, 1, true, false);
                }
                end = pos + lenB;
                lenA %= lenB;
                lenB -= lenA;
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
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0))
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
    }

    protected void mergeFWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = m - a;
        Writes.arraycopy(array, a, tmp, 0, s, 1, true, true);
        int i = 0, j = m;
        while (i < s && j < b)
            if (Reads.compareValues(tmp[i], array[j]) <= 0)
                Writes.write(array, a++, tmp[i++], 1, true, false);
            else
                Writes.write(array, a++, array[j++], 1, true, false);
        while (i < s)
            Writes.write(array, a++, tmp[i++], 1, true, false);
    }

    protected void mergeBWExt(int[] array, int[] tmp, int a, int m, int b) {
        int s = b - m;
        Writes.arraycopy(array, m, tmp, 0, s, 1, true, true);
        int i = s - 1, j = m - 1;
        while (i >= 0 && j >= a)
            if (Reads.compareValues(tmp[i], array[j]) >= 0)
                Writes.write(array, --b, tmp[i--], 1, true, false);
            else
                Writes.write(array, --b, array[j--], 1, true, false);
        while (i >= 0)
            Writes.write(array, --b, tmp[i--], 1, true, false);
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

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = expSearch(array, a, m, array[m], true, false);
        b = expSearch(array, m, b, array[m - 1], false, true);
        if (Reads.compareIndices(array, a, b - 1, 0, false) > 0) {
            rotate(array, a, m, b);
            return;
        }
        if (Math.min(m - a, b - m) <= 8) {
            if (m - a > b - m)
                inPlaceMergeBW(array, a, m, b);
            else
                inPlaceMergeFW(array, a, m, b);
        } else if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
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

    public void mergeSort(int[] array, int[] buf, int a, int b) {
        int i, j, k;
        int mRun = b - a;
        while (mRun >= 32)
            mRun = (mRun - 1) / 2 + 1;
        while (true) {
            i = findRun(array, a, b, mRun);
            if (i >= b)
                return;
            j = findRun(array, i, b, mRun);
            merge(array, buf, a, i, j);
            Highlights.clearMark(2);
            if (j >= b)
                return;
            k = j;
            while (true) {
                i = findRun(array, k, b, mRun);
                if (i >= b)
                    break;
                j = findRun(array, i, b, mRun);
                merge(array, buf, k, i, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
    }

    // same as arraycopy, but reverses the elements being copied at the same time
    protected void copyReverse(int[] src, int srcPos, int[] dest, int destPos, int len, boolean aux) {
        for (int i = 0; i < len; i++)
            Writes.write(dest, destPos + i, src[srcPos + len - 1 - i], 0.5, !aux, aux);
    }

    protected int[] partition(int[] array, int[] buf, int a, int b, int piv) {
        Highlights.clearMark(2);
        int len = b - a;
        int p0 = a, p1 = 0, p2 = len;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
            if (cmp < 0)
                Writes.write(array, p0++, array[i], 0.5, true, false);
            else if (cmp == 0)
                Writes.write(buf, --p2, array[i], 0.5, false, true);
            else
                Writes.write(buf, p1++, array[i], 0.5, false, true);
        }
        int eqSize = len - p2, gtrSize = p1;
        copyReverse(buf, p2, array, p0, eqSize, false);
        Writes.arraycopy(buf, 0, array, p0 + eqSize, gtrSize, 0.5, true, false);
        return new int[] {p0, p0 + eqSize};
    }

    protected void sortHelper(int[] array, int[] buf, int a, int b, int depth, int rep, int d) {
        Writes.recordDepth(d);
        while (b - a > insertlimit) {
            if (depth >= depthlimit || rep >= 4) {
                mergeSort(array, buf, a, b);
                return;
            }
            int pIdx = a + 1;
            while (pIdx < b && Reads.compareIndices(array, pIdx - 1, pIdx, 0.125, true) <= 0)
                pIdx++;
            if (pIdx >= b)
                return;
            int[] p = partition(array, buf, a, b, array[pIdx - 1]);
            if (p[1] - p[0] == b - a)
                return;
            rep = Math.min(b - p[1], p[0] - a) <= replimit ? rep + 1 : 0;
            depth++;
            if (b - p[1] < p[0] - a) {
                Writes.recursion();
                sortHelper(array, buf, p[1], b, depth, rep, d + 1);
                b = p[0];
            } else {
                Writes.recursion();
                sortHelper(array, buf, a, p[0], depth, rep, d + 1);
                a = p[1];
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        depthlimit = (int) Math.min(Math.sqrt(b - a), 2 * log2(b - a));
        insertlimit = Math.max(depthlimit / 2, 16);
        replimit = Math.max(depthlimit / 4, 2);
        if (findReverseRun(array, a, b) + 1 < b) {
            int[] buf = Writes.createExternalArray(b - a);
            sortHelper(array, buf, a, b, 0, 0, 0);
            Writes.deleteExternalArray(buf);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
