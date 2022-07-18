package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public final class LazyLogSort extends Sort {

    public LazyLogSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Lazy Log");
        this.setRunAllSortsName("Lazy Log Sort");
        this.setRunSortName("Lazy Logsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class PivotPair {
        public int p;
        public boolean allEqual;

        public PivotPair(int p, boolean allEqual) {
            this.p = p;
            this.allEqual = allEqual;
        }
    }

    private int productLog(int n) {
        int r = 1;
        while ((r << r) + r - 1 < n)
            r++;
        return r;
    }

    private int medianOf3(int[] array, int... indices) {
        // 3 element case (only one triggered, other cases removed)
        // only first 3 elements are considered if given an array of 4+ indices
        if (Reads.compareIndices(array, indices[0], indices[1], 0.5, true) <= 0) {
            if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) <= 0)
                return indices[1];
            if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) < 0)
                return indices[2];
            return indices[0];
        }
        if (Reads.compareIndices(array, indices[1], indices[2], 0.5, true) >= 0) {
            return indices[1];
        }
        if (Reads.compareIndices(array, indices[0], indices[2], 0.5, true) <= 0) {
            return indices[0];
        }
        return indices[2];
    }

    private int medianOf9(int[] array, int start, int end) {
        // anti-overflow with good rounding
        int length = end - start;
        int half = length / 2;
        int quarter = half / 2;
        int eighth = quarter / 2;
        int med0 = medianOf3(array, start, start + eighth, start + quarter);
        int med1 = medianOf3(array, start + quarter + eighth, start + half, start + half + eighth);
        int med2 = medianOf3(array, start + half + quarter, start + half + quarter + eighth, end - 1);
        return medianOf3(array, new int[] { med0, med1, med2 });
    }

    private int mOMHelper(int[] array, int start, int length) {
        if (length == 1)
            return start;
        int[] meds = new int[3];
        int third = length / 3;
        meds[0] = mOMHelper(array, start, third);
        meds[1] = mOMHelper(array, start + third, third);
        meds[2] = mOMHelper(array, start + 2 * third, third);

        return medianOf3(array, meds);
    }

    private int medianOfMedians(int[] array, int start, int length) {
        if (length == 1)
            return start;
        int[] meds = new int[3];
        int nearPower = (int) Math.pow(3, Math.round(Math.log(length) / Math.log(3)) - 1);
        if (nearPower == length)
            return mOMHelper(array, start, length);
        // uncommon but can happen with numbers slightly smaller than 2*3^k
        // (e.g., 17 < 18 or 47 < 54)
        if (2 * nearPower >= length)
            nearPower /= 3;
        meds[0] = mOMHelper(array, start, nearPower);
        meds[2] = mOMHelper(array, start + length - nearPower, nearPower);
        meds[1] = medianOfMedians(array, start + nearPower, length - 2 * nearPower);

        return medianOf3(array, meds);
    }

    protected void multiSwap(int[] array, int a, int b, int len, boolean bw) {
        if (a == b)
            return;
        if (bw)
            for (int i = 0; i < len; i++)
                Writes.swap(array, a + i, b + i, 1, true, false);
        else
            for (int i = len - 1; i >= 0; i--)
                Writes.swap(array, a + i, b + i, 1, true, false);
    }

    protected void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], sleep, true, false);
        if (a != b)
            Writes.write(array, b, temp, sleep, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        if (a >= m || m >= b)
            return;
        int l = m - a, r = b - m;
        if (l % r == 0 || r % l == 0) {
            while (l > 1 && r > 1) {
                if (r < l) {
                    this.multiSwap(array, m - r, m, r, true);
                    b -= r;
                    m -= r;
                    l -= r;
                } else {
                    this.multiSwap(array, a, m, l, false);
                    a += l;
                    m += l;
                    r -= l;
                }
            }
            if (r == 1)
                this.insertTo(array, m, a, 0.5);
            else if (l == 1)
                this.insertTo(array, a, b - 1, 0.5);
        } else {
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

    protected int findRun(int[] array, int a, int b) {
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
        return i;
    }

    public void insertSort(int[] array, int a, int b) {
        for (int i = findRun(array, a, b); i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]), 0.25);
    }

    // @param bias - false for < piv, true for <= piv
    boolean pivCmp(int v, int piv, boolean bias) {
        int c = Reads.compareValues(v, piv);
        return c < 0 || (bias && c == 0);
    }

    void pivBufXor(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if (v % 2 == 1)
                Writes.swap(array, pa + wLen, pb + wLen, 1, true, false);
            v /= 2;
        }
    }

    // @param bit - < pivot means this bit
    int pivBufGet(int[] array, int pa, int piv, boolean bias, int wLen, int bit) {
        int r = 0;
        while (wLen-- > 0) {
            r *= 2;
            r |= (this.pivCmp(array[pa++], piv, bias) ? 0 : 1) ^ bit;
        }
        return r;
    }

    protected PivotPair partition(int[] array, int a, int b, int bLen, int piv, boolean pCmp) {
        boolean allEqual = true;
        if (b - a <= bLen) {
            int j = a;
            for (int i = a; i < b; i++) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
                if (cmp != 0)
                    allEqual = false;
                if (cmp < 0 || pCmp && cmp == 0)
                    insertTo(array, i, j++, 0.25);
            }
            return new PivotPair(j, allEqual);
        }
        int p = a;
        int l = 0, r = 0;
        int lb = 0, rb = 0;
        for (int i = a; i < b; i++) {
            int cmp = Reads.compareIndexValue(array, i, piv, 0.25, true);
            if (cmp != 0)
                allEqual = false;
            if (cmp < 0 || pCmp && cmp == 0) {
                insertTo(array, i, p + l++, 0.25);
                if (l == bLen) {
                    l = 0;
                    lb++;
                    p += bLen;
                }
            } else {
                r++;
                if (r == bLen) {
                    rotate(array, p, p + l, p + l + bLen);
                    r = 0;
                    rb++;
                    p += bLen;
                }
            }
        }
        int min = Math.min(lb, rb);
        int m = a + lb * bLen;
        if (min > 0) {
            int bCnt = lb + rb;
            int wLen = 32 - Integer.numberOfLeadingZeros(min - 1); // ceil(log2(min))
            for (int i = 0, j = 0, k = 0; i < min; i++) { // set bit buffers
                while (!this.pivCmp(array[a + j * bLen + wLen], piv, pCmp))
                    j++;
                while (this.pivCmp(array[a + k * bLen + wLen], piv, pCmp))
                    k++;
                this.pivBufXor(array, a + (j++) * bLen, a + (k++) * bLen, i, wLen);
            }
            if (lb < rb) {
                for (int i = bCnt - 1, j = 0; j < rb; i--) // swap right to left
                    if (!this.pivCmp(array[a + i * bLen + wLen], piv, pCmp))
                        this.multiSwap(array, a + i * bLen, a + (bCnt - (++j)) * bLen, bLen, false);
                for (int i = 0; i < lb; i++) { // block cycle
                    int dest = this.pivBufGet(array, a + i * bLen, piv, pCmp, wLen, 0);
                    while (dest != i) {
                        this.multiSwap(array, a + i * bLen, a + dest * bLen, bLen, false);
                        dest = this.pivBufGet(array, a + i * bLen, piv, pCmp, wLen, 0);
                    }
                    this.pivBufXor(array, a + i * bLen, m + i * bLen, i, wLen);
                }
            } else {
                for (int i = 0, j = 0; j < lb; i++) // swap left to right
                    if (this.pivCmp(array[a + i * bLen + wLen], piv, pCmp))
                        this.multiSwap(array, a + i * bLen, a + (j++) * bLen, bLen, false);
                for (int i = 0; i < rb; i++) { // block cycle
                    int dest = this.pivBufGet(array, m + i * bLen, piv, pCmp, wLen, 1);
                    while (dest != i) {
                        this.multiSwap(array, m + i * bLen, m + dest * bLen, bLen, false);
                        dest = this.pivBufGet(array, m + i * bLen, piv, pCmp, wLen, 1);
                    }
                    this.pivBufXor(array, a + i * bLen, m + i * bLen, i, wLen);
                }
            }
        }
        rotate(array, a + lb * bLen, b - r - l, b - r);
        return new PivotPair(a + lb * bLen + l, allEqual);
    }

    public boolean getSortedRuns(int[] array, int a, int b) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;
        for (int i = a; i < b - 1; i++) {
            comp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            if (comp > 0)
                sorted = false;
            else
                reverseSorted = false;
            if ((!reverseSorted) && (!sorted))
                return false;
        }
        if (reverseSorted && !sorted) {
            Writes.reversal(array, a, b - 1, 1, true, false);
            sorted = true;
        }

        return sorted;
    }

    protected void sortHelper(int[] array, int a, int b, int bLen, boolean badPartition) {
        while (b - a > 32) {
            if (getSortedRuns(array, a, b))
                return;
            int p;
            if (badPartition) {
                int n = b - a;
                n -= ~n & 1; // even lengths bad
                p = this.medianOfMedians(array, a, n);
                badPartition = false;
            } else
                p = this.medianOf9(array, a, b);
            PivotPair pr = partition(array, a, b, bLen, array[p], false);
            int m = pr.p;
            boolean allEqual = pr.allEqual;
            if (allEqual)
                return;
            if (m == a) {
                pr = partition(array, a, b, bLen, array[p], true);
                m = pr.p;
                badPartition = (b - m) / 8 > m - a;
                a = m;
                continue;
            }
            int lLen = m - a, rLen = b - m;
            badPartition = rLen / 8 > lLen || lLen / 8 > rLen;
            if (lLen > rLen) {
                sortHelper(array, m, b, bLen, badPartition);
                b = m;
            } else {
                sortHelper(array, a, m, bLen, badPartition);
                a = m;
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        sortHelper(array, a, b, productLog(b - a), false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
