package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Flanlaina
in collaboration with aphitorite and PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * An adaptive, in-place and stable block merge sort that uses Logsort's bit
 * buffer technique.
 * <p>
 * To use this algorithm in another, use {@code blockMergeSort()} from a
 * reference instance.
 *
 * @author Flanlaina - implementation of the sort
 * @author aphitorite - key idea / concept of Logsort, and implementation
 *         of Lograil Sort
 * @author PCBoy - Stable Segment Reversal and an equal-optimized
 *         {@code findRun()} method
 *
 */
public class LazyLograilSort extends Sort {

    public LazyLograilSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Lazy Lograil");
        this.setRunAllSortsName("Lazy Lograil Sort");
        this.setRunSortName("Lazy Lograilsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private static int MIN_INSERT = 16;

    public static int productLog(int n) {
        int r = 1;
        while ((r << r) + r - 1 < n) r++;
        return r;
    }

    public static int log2(int n) {
        return 31 - Integer.numberOfLeadingZeros(n);
    }

    private void reversalHelper(int[] array, int a, int b) {
        if (b - a < 3) Writes.swap(array, a, b, 0.75, true, false);
        else Writes.reversal(array, a, b, 0.75, true, false);
    }

    protected void reverseEqualSegments(int[] array, int start, int end) {
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (i < end && Reads.compareIndices(array, i, i + 1, 0.5, true) == 0) i++;
            right = i;
            if (left != right) reversalHelper(array, left, right);
            i++;
        }
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        if (a != b) Writes.write(array, b, temp, 0.5, true, false);
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

    protected int minExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int maxExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    public int findRun(int[] array, int start, int end) {
        int i = start + 1;
        if (i >= end) return i;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
        while (cmp == 0 && i < end) {
            lessunique = true;
            i++;
            if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
        }
        if (cmp > 0) {
            while (cmp >= 0 && i < end) {
                if (cmp == 0) lessunique = true;
                else different = true;
                i++;
                if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
            }
            if (i - start > 1 && different) {
                reversalHelper(array, start, i - 1);
                if (lessunique) reverseEqualSegments(array, start, i - 1);
            }
        } else {
            while (cmp <= 0 && i < end) {
                i++;
                if (i < end) cmp = Reads.compareIndices(array, i - 1, i, 0.5, true);
            }
        }
        return i;
    }

    protected boolean buildRuns(int[] array, int a, int b, int mRun) {
        int i = a + 1, j = a;
        boolean noSort = true;
        while (i < b) {
            i = findRun(array, j, b);
            if (i < b) {
                noSort = false;
                j = i - (i - j - 1) % mRun - 1;
            }
            while (i - j < mRun && i < b) {
                insertTo(array, i, maxExpSearch(array, j, i, array[i], false));
                i++;
            }
            j = i++;
        }
        return noSort;
    }

    // @param pCmp - 0 for < piv, 1 for <= piv
    protected boolean pivCmp(int v, int piv, int pCmp) {
        int c = Reads.compareValues(v, piv);
        return c < 0 || (pCmp == 1 && c == 0);
    }

    protected void blockXor(int[] array, int pa, int pb, int v, int wLen) {
        while (wLen-- > 0) {
            if ((v & 1) == 1) Writes.swap(array, pa + wLen, pb + wLen, 1, true, false);
            v >>= 1;
        }
    }

    // @param bit - < pivot means this bit
    protected int blockRead(int[] array, int pa, int piv, int pCmp, int wLen, int bit) {
        int r = 0;

        while (wLen-- > 0) {
            r <<= 1;
            r |= (this.pivCmp(array[pa++], piv, pCmp) ? 0 : 1) ^ bit;
        }
        return r;
    }

    protected void blockCycle(int[] array, int p, int n, int p1, int bLen, int wLen, int piv, int pCmp, int bit) {
        for (int i = 0; i < n; i++) {
            int dest = this.blockRead(array, p + i * bLen, piv, pCmp, wLen, bit);

            while (dest != i) {
                this.blockSwap(array, p + i * bLen, p + dest * bLen, bLen);
                dest = this.blockRead(array, p + i * bLen, piv, pCmp, wLen, bit);
            }
            this.blockXor(array, p + i * bLen, p1 + i * bLen, i, wLen);
        }
        Highlights.clearMark(2);
    }

    protected void blockSwap(int[] array, int a, int b, int s) {
        while (s-- > 0) Writes.swap(array, a++, b++, 1, true, false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        Highlights.clearAllMarks();
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    protected void inPlaceMergeFW(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            int i = minExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m >= b) break;
            a = minExpSearch(array, a, m, array[m], false);
        }
    }

    protected void inPlaceMergeBW(int[] array, int a, int m, int b, boolean fwEq) {
        while (b > m && m > a) {
            int i = maxExpSearch(array, a, m, array[b - 1], !fwEq);
            rotate(array, i, m, b);
            int t = m - i;
            m = i;
            b -= t + 1;
            if (m <= a) break;
            b = maxExpSearch(array, m, b, array[m - 1], fwEq);
        }
    }

    private void blockMergeHelper(int[] array, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if (m - a <= bLen) {
            inPlaceMergeFW(array, a, m, b);
            return;
        }
        int bCnt = 0, wLen = log2((b - a) / bLen - 1) + 1;

        int i = a, j = m, k = 0, pc = p;

        while (i < m && j + bLen - 1 < b) {
            if (Reads.compareIndices(array, i + bLen - 1, j + bLen - 1, 0.5, true) <= 0) {
                this.blockXor(array, i, pc, k++, wLen);
                i += bLen;
            } else {
                this.blockXor(array, j, pc, (k++ << 1) | 1, wLen + 1);
                j += bLen;
            }
            pc += bLen;
            bCnt++;
        }
        while (i < m) {
            this.blockXor(array, i, pc, k++, wLen);
            i += bLen;
            pc += bLen;
            bCnt++;
        }
        this.blockCycle(array, a, bCnt, p, bLen, wLen, piv, pCmp, bit);
        int f = a;

        boolean left = this.pivCmp(array[a + wLen], piv, pCmp) ^ (bit != 0);
        if (!left) Writes.swap(array, a + wLen, p + wLen, 1, true, false);

        for (i = 1; i < bCnt; i++) {
            int nxt = a + i * bLen; pc = p + i * bLen;

            boolean frag = this.pivCmp(array[nxt + wLen], piv, pCmp) ^ (bit != 0);
            if (!frag) Writes.swap(array, nxt + wLen, pc + wLen, 1, true, false);

            if (left ^ frag) {
                int nxtE = this.binSearch(array, nxt, nxt + bLen, array[nxt - 1], left);
                inPlaceMergeBW(array, f, nxt, nxtE, left);
                f = nxtE;
                left = !left;
            }
        }
        if (left) inPlaceMergeBW(array, f, a + bCnt * bLen, b, true);
    }

    private void blockMergeEasy(int[] array, int a, int m, int b, int p, int bLen, int piv, int pCmp, int bit) {
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        b = maxExpSearch(array, m, b, array[m - 1], true);
        if (b - m <= bLen) {
            this.inPlaceMergeBW(array, a, m, b, true);
            return;
        }
        a = minExpSearch(array, a, m, array[m], false);
        if (m - a <= bLen) {
            this.inPlaceMergeFW(array, a, m, b);
            return;
        }

        int a1 = a + (m - a) % bLen;

        this.blockMergeHelper(array, a1, m, b, p, bLen, piv, pCmp, bit);
        this.inPlaceMergeFW(array, a, a1, b);
    }

    public void blockMerge(int[] array, int a, int m, int b, int bLen) {
        if (a == m || m == b) return;
        if (Reads.compareIndices(array, m - 1, m, 0.5, true) <= 0) return;
        b = maxExpSearch(array, m, b, array[m - 1], true);
        a = minExpSearch(array, a, m, array[m], false);
        int l = m - a, r = b - m;
        int lCnt = (l + r + 1) / 2;

        int med;

        // find lower ceil((A+B)/2) elements and then find max of halves to get median
        // binary search is used for O(log n) performance

        if (r < l) {
            if (r <= bLen) {
                this.inPlaceMergeBW(array, a, m, b, true);
                return;
            }
            int la = 0, lb = r;

            while (la < lb) {
                int lm = (la + lb) >>> 1;

                if (Reads.compareIndices(array, m + lm, a + (lCnt - lm) - 1, 0.25, true) <= 0)
                    la = lm + 1;
                else
                    lb = lm;
            }
            if (la == 0) med = array[a + lCnt - 1];
            else med = Reads.compareIndices(array, m + la - 1, a + (lCnt - la) - 1, 0.25, true) > 0
                    ? array[m + la - 1] : array[a + (lCnt - la) - 1];
        } else {
            if (l <= bLen) {
                this.inPlaceMergeFW(array, a, m, b);
                return;
            }
            int la = 0, lb = l;

            while (la < lb) {
                int lm = (la + lb) >>> 1;

                if (Reads.compareIndices(array, a + lm, m + (lCnt - lm) - 1, 0.25, true) < 0)
                    la = lm + 1;
                else
                    lb = lm;
            }
            if (l == r && la == l) med = array[m - 1];
            else if (la == 0) med = array[m + lCnt - 1];
            else med = Reads.compareIndices(array, a + la - 1, m + (lCnt - la) - 1, 0.25, true) >= 0
                ? array[a + la - 1] : array[m + (lCnt - la) - 1];
        }
        Highlights.clearMark(2);

        // stable ternary partition around median: [ < ][ = ][ > ]

        int m1 = this.binSearch(array, a, m, med, true);
        int m2 = this.binSearch(array, m, b, med, false);

        int ms2 = m - this.binSearch(array, m1, m, med, false);
        int ms1 = this.binSearch(array, m, m2, med, true) - m;

        this.rotate(array, m - ms2, m, m2); // ABCABC -> ABABCC
        this.rotate(array, m1, m - ms2, m + ms1 - ms2); // ABABCC -> AABBCC

        if (m1 > a && ms1 > 0) this.blockMergeEasy(array, a, m1, m1 + ms1, a + lCnt, bLen, med, 0, 0);
        if (m2 < b && ms2 > 0) this.blockMergeEasy(array, m2 - ms2, m2, b, a, bLen, med, 1, 1);
    }

    /**
     * Sorts the range {@code [left, right)} of {@code array} using a block merge
     * sort.
     *
     * @param array the array
     * @param left  the start of the range, inclusive
     * @param right the end of the range, exclusive
     */
    public void blockMergeSort(int[] array, int left, int right) {
        int j = MIN_INSERT, length = right - left;
        int bLen = productLog(length);
        if (buildRuns(array, left, right, j)) return;
        for (; j < length; j *= 2)
            for (int i = left; i + j < right; i += 2 * j)
                this.blockMerge(array, i, i + j, Math.min(right, i + 2 * j), bLen);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        blockMergeSort(array, 0, sortLength);
    }

}
