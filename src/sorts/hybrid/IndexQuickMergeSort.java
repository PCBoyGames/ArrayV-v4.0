package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

A mix between Index Merge sort and Index Quicksort.

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 *
 */
public final class IndexQuickMergeSort extends Sort {

    public IndexQuickMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Index Quick Merge");
        setRunAllSortsName("Index Quick Merge Sort");
        setRunSortName("Index Quick Mergesort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }
    
    public static int log2(int n) {
        int log = 0;
        while ((n >>= 1) != 0) ++log;
        return log;
    }
    
    int threshold = 32;
    
    int equ(int a, int b) {
        return ((a - b) >> 31) + ((b - a) >> 31) + 1;
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

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if(Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if(Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if(Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
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
        if (b - a <= 6) return a + (b - a) / 2;
        int p = 1;
        while (6 * p < b - a) p *= 3;
        int l = medP3(array, a, a + p, -1), c = medOfMed(array, a + p, b - p), r = medP3(array, b - p, b, -1);
        // median
        return medOf3(array, l, c, r);
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
    
    protected void indexSort(int[] array, int[] idx, int a, int b) {
        for (int i = 0; i < b - a; i++) {
            int nxt = idx[i];
            int tmp = array[a + i];
            boolean change = false;
            while (Reads.compareOriginalValues(i, nxt) != 0) {
                // Writes.swap(array, a + i, a + nxt, 0.5, true, false);
                int tmp1 = array[a + nxt];
                // array[a + nxt] = tmp;
                Writes.write(array, a + nxt, tmp, 0.5, true, false);
                tmp = tmp1;
                int tmp2 = idx[nxt];
                Writes.write(idx, nxt, nxt, 0.5, false, true);
                nxt = tmp2;
                change = true;
            }
            if (change) {
                Writes.write(array, a + i, tmp, 0.5, true, false);
                Writes.write(idx, i, nxt, 0.5, false, true);
            }
        }
    }

    protected void merge(int[] array, int[] idx, int a, int m, int b) {
        if (Reads.compareIndices(array, m - 1, m, 0.0, true) <= 0) return;
        a = leftExpSearch(array, a, m, array[m], false);
        b = rightExpSearch(array, m, b, array[m - 1], true);
        int i = a, j = m, c = 0;
        while (i < m || j < b) {
            if (i < m) Highlights.markArray(1, i);
            else Highlights.clearMark(1);
            if (j < b) Highlights.markArray(2, j);
            else Highlights.clearMark(2);
            if (i < m && (j >= b || Reads.compareIndices(array, i, j, 0, false) <= 0))
                Writes.write(idx, i++ - a, c, 0.5, false, true);
            else
                Writes.write(idx, j++ - a, c, 0.5, false, true);
            c++;
        }
        Highlights.clearMark(2);
        indexSort(array, idx, a, b);
    }
    
    protected int findRun(int[] array, int a, int b, int mRun) {
        int i = a + 1;
        if (i < b) {
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0) i++;
                if (i - a < 4) Writes.swap(array, a, i - 1, 1.0, true, false);
                else Writes.reversal(array, a, i - 1, 1.0, true, false);
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
        findRun(array, a, b, b - a);
    }

    public void mergeSort(int[] array, int[] buf, int a, int b) {
        int len = b - a;
        if (len <= threshold) {
            insertSort(array, a, b);
            return;
        }
        int mRun = 16;
        int[] runs = Writes.createExternalArray((len - 1) / mRun + 2);
        int r = a, rf = 0;
        while (r < b) {
            Writes.write(runs, rf++, r, 0.5, false, true);
            r = findRun(array, r, b, mRun);
        }
        while (rf > 1) {
            for (int i = 0; i < rf - 1; i += 2) {
                int eIdx;
                if (i + 2 >= rf) eIdx = b;
                else eIdx = runs[i + 2];
                merge(array, buf, runs[i], runs[i + 1], eIdx);
            }
            for (int i = 1, j = 2; i < rf; i++, j+=2, rf--)
                Writes.write(runs, i, runs[j], 0.5, false, true);
        }
        Writes.deleteExternalArray(runs);
    }
    
    int pivCmp(int v, int piv) {
        int c = Reads.compareValues(v, piv);
        if (c > 0)
            return 2;
        if (c < 0)
            return 0;
        return 1;
    }
    
    protected int[] partition(int[] array, int[] idx, int a, int b, int piv) {
        Highlights.clearMark(2);
        int[] ptrs = new int[4];
        for (int i = a; i < b; i++) {
            Highlights.markArray(1, i);
            Delays.sleep(0.5);
            int c = pivCmp(array[i], piv);
            Writes.write(idx, i - a, c, 0.5, false, true);
            ptrs[c]++;
        }
        for (int i = 1; i < ptrs.length;i++) ptrs[i] += ptrs[i-1];
        for (int i = b - a - 1; i >= 0; i--) {
            //Highlights.markArray(1, a + i);
            Writes.write(idx, i, --ptrs[idx[i]], 0, false, true);
        }
        indexSort(array, idx, a, b);
        for (int i = 0; i < ptrs.length; i++) ptrs[i] += a;
        return new int[] {ptrs[1], ptrs[2]};
    }
    
    void sortHelper(int[] array, int[] buf, int a, int b, int badAllowed, boolean bad, boolean doMerge) {
        if (b - a <= threshold) {
            insertSort(array, a, b);
            return;
        }
        if (doMerge) {
            int m = a + (b - a) / 2;
            sortHelper(array, buf, a, m, badAllowed, bad, false);
            sortHelper(array, buf, m, b, badAllowed, bad, false);
            merge(array, buf, a, m, b);
            return;
        }
        int pIdx;
        if (bad) pIdx = medOfMed(array, a, b);
        else pIdx = medP3(array, a, b, 1);
        int[] pr = partition(array, buf, a, b, array[pIdx]);
        int lLen = pr[0] - a, rLen = b - pr[1], eqLen = pr[1] - pr[0];
        // if all equal, skip this partition
        if (eqLen == b - a) return;
        if (rLen == 0) bad = eqLen < lLen / 8;
        else if (lLen == 0) bad = eqLen < rLen / 8;
        else bad = rLen / 8 > lLen || lLen / 8 > rLen;
        if (bad) {
            badAllowed--;
            if (badAllowed == 0) {
                mergeSort(array, buf, a, b);
                return;
            }
        }
        doMerge = true;
        if (rLen < lLen) {
            sortHelper(array, buf, pr[1], b, badAllowed, bad, true);
            sortHelper(array, buf, a, pr[0], badAllowed, bad, true);
        } else {
            sortHelper(array, buf, a, pr[0], badAllowed, bad, true);
            sortHelper(array, buf, pr[1], b, badAllowed, bad, true);
        }
    }
    
    public void quickMergeSort(int[] array, int a, int b) {
        int len = b - a;
        int balance = 0, eq = 0, streaks = 0, dist, eqdist, loop, cnt = len, pos = a;
        while (cnt > 16) {
            for (eqdist = dist = 0, loop = 0; loop < 16; loop++) {
                int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
                dist += cmp > 0 ? 1 : 0;
                eqdist += cmp == 0 ? 1 : 0;
                pos++;
            }
            streaks += equ(dist, 0) | equ(dist + eqdist, 16);
            balance += dist;
            eq += eqdist;
            cnt -= 16;
        }
        while (--cnt > 0) {
            int cmp = Reads.compareIndices(array, pos, pos + 1, 0.5, true);
            balance += cmp > 0 ? 1 : 0;
            eq += cmp == 0 ? 1 : 0;
            pos++;
        }
        if (balance == 0) return;
        if (balance + eq == len - 1) {
            if (eq > 0) stableSegmentReversal(array, a, b - 1);
            else if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        int[] buf = Writes.createExternalArray(len);
        int sixth = len / 6;
        if (streaks > len / 20 || balance <= sixth || balance + eq >= len - sixth)
            mergeSort(array, buf, a, b);
        else sortHelper(array, buf, a, b, log2(len), false, false);
        Writes.deleteExternalArray(buf);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickMergeSort(array, 0, sortLength);

    }

}
