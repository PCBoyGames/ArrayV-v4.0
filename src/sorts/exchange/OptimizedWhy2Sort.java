package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedWhy2Sort extends Sort {
    public OptimizedWhy2Sort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Why 2");
        this.setRunAllSortsName("Optimized Why Sort 2");
        this.setRunSortName("Optimized Why Sort 2");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    protected boolean lessunique = false;
    protected int globalpdir = 1;

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.001, true, false);
        else Writes.reversal(array, start, end, 0.001, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.001, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.001, true, false);
                else Writes.reversal(array, left, right, 0.001, true, false);
            }
            i++;
        }
    }

    protected void pd(int[] array, int start, int end, int dir) {
        if (dir * globalpdir == 1) {
            int r = start + ((end - start) / 2) - 1;
            int cmp = 0;
            if (r + 1 < end) cmp = Reads.compareIndices(array, r, r + 1, 0.001, true);
            while (r + 1 < end) {
                if (cmp == dir || cmp == 0) {
                    r++;
                    if (cmp == 0) lessunique = true;
                    if (r + 1 < end) cmp = Reads.compareIndices(array, r, r + 1, 1, r + 1 < end);
                } else break;
            }
            if (lessunique) stableSegmentReversal(array, start, r);
            else {
                if (r - start > 2) Writes.reversal(array, start, r, 0.001, true, false);
                else if (r - start > 0) Writes.swap(array, start, r, 0.001, true, false);
            }
        } else {
            if (lessunique) stableSegmentReversal(array, start + ((end - start) / 2), end - 1);
            else {
                if (end - start > 4) Writes.reversal(array, start + ((end - start) / 2), end - 1, 0.001, true, false);
                else if (end - start > 2) Writes.swap(array, start + ((end - start) / 2), end - 1, 0.001, true, false);
            }
        }
        int c = 1;
        int s;
        int f = start;
        boolean a = false;
        for (int j = end - 1; j > start; j -= c) {
            if (f - 1 < start) s = start;
            else s = f - 1;
            a = false;
            c = 1;
            for (int i = s; i < j; i++) {
                int cmp = Reads.compareIndices(array, i, i + 1, 0.001, true);
                if (cmp == dir) {
                    Writes.swap(array, i, i + 1, 0.001, true, false);
                    if (!a) f = i;
                    a = true;
                    c = 1;
                } else {
                    c++;
                    if (cmp == 0) lessunique = true;
                }
            }
        }
    }

    protected void method(int[] array, int start, int end, int pdir, int itr) {
        Writes.recordDepth(itr - 1);
        globalpdir = pdir;
        int len = 2;
        int index = start;
        int dir = -1;
        while (len < end - start) {
            index = start;
            dir = -1;
            while (index + len <= end) {
                if (len == 2) {
                    int cmp = Reads.compareIndices(array, index, index + 1, 0.001, true);
                    if (cmp == dir * pdir) Writes.swap(array, index, index + 1, 0.001, true, false);
                    else if (cmp == 0) lessunique = true;
                } else if (itr < 20) {
                    Writes.recursion();
                    method(array, index, index + len, pdir, itr + 1);
                }
                else pd(array, index, index + len, dir * pdir);
                index += len;
                dir *= -1;
            }
            if (index != end) {
                if (itr < 20) {
                    Writes.recursion();
                    method(array, index, end, dir * pdir, itr + 1);
                }
                else pd(array, index, end, dir * pdir);
            }
            len *= 2;
        }
        if (itr < 20) {
            Writes.recursion();
            method(array, start, end, pdir, itr + 1);
        }
        else pd(array, start, end, pdir);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 2;
        int index = 0;
        int dir = -1;
        while (len < currentLength) {
            index = 0;
            dir = -1;
            while (index + len <= currentLength) {
                if (len == 2) {
                    int cmp = Reads.compareIndices(array, index, index + 1, 0.001, true);
                    if (cmp == dir) Writes.swap(array, index, index + 1, 0.001, true, false);
                    else if (cmp == 0) lessunique = true;
                } else method(array, index, index + len, dir, 1);
                index += len;
                dir *= -1;
            }
            if (index != currentLength) method(array, index, currentLength, dir, 1);
            len *= 2;
        }
        method(array, 0, currentLength, 1, 1);
    }
}