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
public class OOPSmartSingularityQuickSort extends Sort {

    public OOPSmartSingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("OOP Smart Singularity Quick");
        this.setRunAllSortsName("Out-of-Place Smart Singularity Quick Sort");
        this.setRunSortName("Out-of-Place Smart Singularity Quicksort");
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

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3)
            Writes.swap(array, start, end, 0.75, true, false);
        else
            Writes.reversal(array, start, end, 0.75, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0)
                i++;
            right = i;
            if (left != right)
                if (right - left < 3)
                    Writes.swap(array, left, right, 0.75, true, false);
                else
                    Writes.reversal(array, left, right, 0.75, true, false);
            i++;
        }
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

    protected int leftExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0)
                i *= 2;
        else
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                i *= 2;
        int a1 = a + i / 2, b1 = Math.min(b, a - 1 + i);
        return binSearch(array, a1, b1, val, left);
    }

    protected int rightExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left)
            while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0)
                i *= 2;
        else
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        return binSearch(array, a1, b1, val, left);
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

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b)
            Writes.write(array, b, temp, 0.5, true, false);
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

    protected void merge(int[] array, int[] buf, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0)
            return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        Highlights.clearMark(2);
        if (m - a > b - m)
            mergeBWExt(array, buf, a, m, b);
        else
            mergeFWExt(array, buf, a, m, b);
    }

    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        if (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                if (i - a < 4)
                    Writes.swap(array, a, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0) i++;
        }
        Highlights.clearMark(2);
        while (i - a < mRun && i < b) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i], false));
            i++;
        }
        return i;
    }

    public void insertSort(int[] array, int a, int b) {
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
                break;
            j = findRun(array, i, b, mRun);
            merge(array, buf, a, i, j);
            Highlights.clearMark(2);
            if (j >= b)
                break;
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
        if (eqSize < len) {
            for (int i = 0; i < eqSize; i++)
                Writes.write(array, p0 + i, buf[p2 + eqSize - 1 - i], 0.5, true, false);
            Writes.arraycopy(buf, 0, array, p0 + eqSize, gtrSize, 0.5, true, false);
        }
        return new int[] { p0, p0 + eqSize };
    }

    void innerSort(int[] array, int[] buf, int a, int b, int depth, int rep, int d) {
        Writes.recordDepth(d);
        while (b - a > insertlimit) {
            if (depth >= depthlimit || rep >= 4) {
                mergeSort(array, buf, a, b);
                return;
            }
            findReverseRun(array, a, b);
            int rIdx = a + 1;
            while (rIdx < b && Reads.compareIndices(array, rIdx - 1, rIdx, 0.125, true) <= 0)
                rIdx++;
            if (rIdx >= b)
                return;
            int pIdx = findPivot(array, a, b, rIdx);
            int[] pr = partition(array, buf, a, b, array[pIdx]);
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
                innerSort(array, buf, pr[1], b, depth, rep, d + 1);
                b = pr[0];
            } else {
                innerSort(array, buf, a, pr[0], depth, rep, d + 1);
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
        int len = b - a;
        depthlimit = (int) Math.min(Math.sqrt(len), 2 * log2(len));
        insertlimit = Math.max(depthlimit / 2, 16);
        if (findReverseRun(array, a, b) + 1 < b) {
            int[] buf = Writes.createExternalArray(len);
            innerSort(array, buf, a, b, 0, 0, 0);
            Writes.deleteExternalArray(buf);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
