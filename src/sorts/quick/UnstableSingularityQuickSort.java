package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class UnstableSingularityQuickSort extends Sort {
    
    int depthlimit;
    
    public UnstableSingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unstable Singularity Quick");
        this.setRunAllSortsName("Unstable Singularity Quick Sort");
        this.setRunSortName("Unstable Singularity Quicksort");
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
    
    protected int unstablepd(int[] array, int start, int end) {
        int reverse = start;
        boolean different = false;
        int cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        while (cmp >= 0 && reverse + 1 < end) {
            if (cmp == 1) different = true;
            reverse++;
            cmp = Reads.compareIndices(array, reverse, reverse + 1, 0.5, true);
        }
        if (reverse > start && different) {
            if (reverse < start + 4) Writes.swap(array, start, reverse, 0.75, true, false);
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
        if (end - start > 7 && depth < depthlimit && rep < 4) {
            int left = offset;
            while (Reads.compareIndices(array, left - 1, left, 0.05, true) <= 0 && left < end) left++;
            if (left < end) {
                int pivot = array[left - 1];
                int pivotpos = left - 1;
                int right = left + 1;
                int item = 1;
                boolean brokeloop = false;
                boolean brokencond = false;
                boolean founditem = false;
                while (right <= end) {
                    if (Reads.compareValues(pivot, array[right - 1]) > 0) {
                        Highlights.markArray(3, pivotpos);
                        Highlights.clearMark(2);
                        if (right - left == 1) {
                            if (!founditem) item = array[left - 1];
                            founditem = true;
                            Writes.write(array, left - 1, array[left], 0.5, true, false);
                        } else brokeloop = true;
                        if (brokeloop && !brokencond) {
                            Writes.write(array, left - 1, item, 0.5, true, false);
                            brokencond = true;
                        }
                        if (right - left > 1) Writes.swap(array, left - 1, right - 1, 0.5, true, false);
                        if (pivotpos == left - 1) pivotpos = right - 1;
                        left++;
                    }
                    right++;
                }
                if (right > end && !brokeloop) Writes.write(array, left - 1, item, 0.5, true, false);
                Highlights.clearAllMarks();
                if (pivotpos != left - 1) Writes.swap(array, pivotpos, left - 1, 0.5, true, false);
                boolean lsmall = left - start < end - (left + 1);
                if (lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - 4 <= left || left <= start + 4) singularityQuick(array, start, start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, start, left - 1, depth + 1, 0);
                }
                if (end - (left + 1) > 0) {
                    Writes.recursion();
                    if (end - 4 <= left || left <= start + 4) singularityQuick(array, left + 1, left + 1, end, depth + 1, rep + 1);
                    else singularityQuick(array, left + 1, left + 1, end, depth + 1, 0);
                }
                if (!lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - 4 <= left || left <= start + 4) singularityQuick(array, start, start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, start, left - 1, depth + 1, 0);
                }
            }
        } else binsert(array, start, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        depthlimit = (int) Math.min(Math.sqrt(currentLength), 2 * log2(currentLength));
        int realstart = unstablepd(array, 0, currentLength);
        if (realstart + 1 < currentLength) singularityQuick(array, 1, realstart + 1, currentLength, 0, 0);
    }
}