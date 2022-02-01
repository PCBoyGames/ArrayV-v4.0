package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PDIterativePopSort extends Sort {
    public PDIterativePopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pattern-Defeating Iterative Pop");
        this.setRunAllSortsName("Pattern-Defeating Iterative Pop Sort");
        this.setRunSortName("Pattern-Defeating Iterative Pop Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean lessunique = false;

    protected void stableSegmentReversal(int[] array, int start, int end) {
        if (end - start < 3) Writes.swap(array, start, end, 0.075, true, false);
        else Writes.reversal(array, start, end, 0.075, true, false);
        int i = start;
        int left;
        int right;
        while (i < end) {
            left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end) i++;
            right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
            i++;
        }
    }

    protected void pd(int[] array, int start, int end, int dir) {
        if (dir == 1) {
            int r = start + ((end - start) / 2) - 1;
            int cmp = 0;
            if (r + 1 < end) cmp = Reads.compareIndices(array, r, r + 1, 1, true);
            while (r + 1 < end) {
                if (cmp == dir || cmp == 0) {
                    r++;
                    if (cmp == 0) lessunique = true;
                    if (r + 1 < end) cmp = Reads.compareIndices(array, r, r + 1, 1, r + 1 < end);
                } else break;
            }
            if (lessunique) stableSegmentReversal(array, start, r);
            else {
                if (r - start > 2) Writes.reversal(array, start, r, 0.2, true, false);
                else if (r - start > 0) Writes.swap(array, start, r, 0.2, true, false);
            }
        } else {
            if (lessunique) stableSegmentReversal(array, start + ((end - start) / 2), end - 1);
            else {
                if (end - start > 4) Writes.reversal(array, start + ((end - start) / 2), end - 1, 0.2, true, false);
                else if (end - start > 2) Writes.swap(array, start + ((end - start) / 2), end - 1, 0.2, true, false);
            }
        }
        int c = 1;
        int s;
        int f = start + ((end - start) / 2);
        boolean a = false;
        for (int j = end - 1; j > start; j -= c) {
            if (f - 1 < start) s = start;
            else s = f - 1;
            a = false;
            c = 1;
            for (int i = s; i < j; i++) {
                int cmp = Reads.compareIndices(array, i, i + 1, 0.025, true);
                if (cmp == dir) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
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
                    int cmp = Reads.compareIndices(array, index, index + 1, 0.25, true);
                    if (cmp == dir) Writes.swap(array, index, index + 1, 0.75, true, false);
                    else if (cmp == 0) lessunique = true;
                } else pd(array, index, index + len, dir);
                index += len;
                dir *= -1;
            }
            if (index != currentLength) pd(array, index, currentLength, dir);
            len *= 2;
        }
        pd(array, 0, currentLength, 1);
    }
}