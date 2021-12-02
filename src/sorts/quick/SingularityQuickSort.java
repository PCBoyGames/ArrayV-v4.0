package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SingularityQuickSort extends Sort {
    
    int depthlimit;
    
    public SingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Singularity Quick");
        this.setRunAllSortsName("Singularity Quick Sort");
        this.setRunSortName("Singularity Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected int log2(int x) {
        int n = 1;
        while (1 << n < x) n++;
        if (1 << n > x) n--;
        return n;
    }
    
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
    
    protected int pd(int[] array, int start, int end) {
        int reverse = start;
        boolean lessunique = false;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp == 0) lessunique = true;
            else different = true;
            reverse++;
            cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (lessunique) stableSegmentReversal(array, start, reverse);
            else if (reverse < start + 4) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }
    
    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(0.1);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }
    
    protected void binsert(int[] array, int start, int end) {
        for (int i = start; i < end; i++) {
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                int item = array[i];
                int left = binarySearch(array, start - 1, i - 1, item);
                Highlights.clearAllMarks();
                Highlights.markArray(2, left);
                for (int right = i; right > left; right--) Writes.write(array, right, array[right - 1], 0.01, true, false);
                Writes.write(array, left, item, 0.01, true, false);
                Highlights.clearAllMarks();
            } else {
                Highlights.markArray(1, i);
                Delays.sleep(0.05);
            }
        }
    }
    
    protected void singularityQuick(int[] array, int start, int offset, int end, int depth, int rep) {
        Writes.recordDepth(depth);
        Highlights.clearAllMarks();
        if (end - start > (depthlimit / 2) - 1 && depth < depthlimit && rep < 4) {
            int left = offset;
            while (Reads.compareIndices(array, left - 1, left, 0.05, true) <= 0 && left < end) left++;
            if (left < end) {
                int right = left + 1;
                int pull = 1;
                int pivot = array[left - 1];
                int originalpos = left - 1;
                boolean brokeloop = false;
                boolean brokencond = false;
                while (right <= end) {
                    if (Reads.compareValues(pivot, array[right - 1]) > 0) {
                        Highlights.clearMark(2);
                        if (right - left == 1) {
                            Writes.write(array, left - 1, array[left], 0.1, true, false);
                        } else brokeloop = true;
                        if (brokeloop && !brokencond) {
                            Writes.write(array, left - 1, pivot, 0.1, true, false);
                            brokencond = true;
                        }
                        if (right - left > 1) {
                            pull = right - 1;
                            int item = array[pull];
                            Highlights.clearMark(2);
                            while (pull >= left) {
                                Writes.write(array, pull, array[pull - 1], 0.1, true, false);
                                pull--;
                            }
                            Writes.write(array, pull, item, 0.1, true, false);
                        }
                        left++;
                    }
                    right++;
                }
                if (right > end && !brokeloop) Writes.write(array, left - 1, pivot, 0.1, true, false);
                boolean lsmall = left - start < end - (left + 1);
                if (lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - 4 <= left || left <= start + 4) singularityQuick(array, start, originalpos - 1 > start ? originalpos : start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, originalpos - 1 > start ? originalpos : start, left - 1, depth + 1, 0);
                }
                if (end - (left + 1) > 0) {
                    Writes.recursion();
                    if (end - 4 <= left || left <= start + 4) singularityQuick(array, left + 1, left + 1, end, depth + 1, rep + 1);
                    else singularityQuick(array, left + 1, left + 1, end, depth + 1, 0);
                }
                if (!lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - 4 <= left || left <= start + 4) singularityQuick(array, start, originalpos - 1 > start ? originalpos : start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, originalpos - 1 > start ? originalpos : start, left - 1, depth + 1, 0);
                }
            }
        } else binsert(array, start, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        depthlimit = (int) Math.min(Math.sqrt(currentLength), 2 * log2(currentLength));
        int realstart = pd(array, 0, currentLength);
        if (realstart + 1 < currentLength) singularityQuick(array, 1, realstart + 1, currentLength, 0, 0);
    }
}