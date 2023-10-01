package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class ShellUnstableSingularityQuickSort extends Sort {

    int depthlimit;
    int insertlimit;
    int replimit;

    public ShellUnstableSingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shell Unstable Singularity Quick");
        this.setRunAllSortsName("Shell Unstable Singularity Quick Sort");
        this.setRunSortName("Shell Unstable Singularity Quicksort");
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

    protected void shellPass(int[] array, int start, int end, int gap) {
        for (int h = gap, i = h + start; i < end; i++) {
            int v = array[i];
            int j = i;
            boolean w = false;
            Highlights.markArray(1, j);
            Highlights.markArray(2, j - h);
            Delays.sleep(0.25);
            for (; j >= h && j - h >= start && Reads.compareValues(array[j - h], v) == 1; j -= h) {
                Highlights.markArray(2, j - h);
                Writes.write(array, j, array[j - h], 0.25, w = true, false);
            }
            if (w) Writes.write(array, j, v, 0.25, true, false);
        }
    }

    protected void shell(int[] array, int start, int end) {
        for (int gap = (int) ((end - start) / 2); gap >= 2; gap /= 2) shellPass(array, start, end, gap);
        shellPass(array, start, end, 1);
    }

    protected void singularityQuick(int[] array, int start, int offset, int end, int depth, int rep) {
        Writes.recordDepth(depth);
        if (depth == depthlimit || rep == 4) {
            shell(array, start - 1, end);
            return;
        }
        Highlights.clearAllMarks();
        if (end - start > insertlimit) {
            int left = offset;
            while (Reads.compareIndices(array, left - 1, left, 0.05, true) <= 0 && left < end) left++;
            if (left < end) {
                int pivot = array[left - 1];
                int pivotpos = left - 1;
                int originalpos = left - 1;
                int right = left + 1;
                int item = 1;
                boolean brokeloop = false;
                boolean brokencond = false;
                boolean founditem = false;
                for (; right <= end; right++) {
                    if (Reads.compareValues(pivot, array[right - 1]) > 0) {
                        Highlights.markArray(3, pivotpos);
                        Highlights.clearMark(2);
                        if (right - left == 1) {
                            if (!founditem) item = array[left - 1];
                            Writes.write(array, left - 1, array[left], 0.1, founditem = true, false);
                        } else brokeloop = true;
                        if (brokeloop && !brokencond) Writes.write(array, left - 1, item, 0.5, brokencond = true, false);
                        if (right - left > 1) Writes.swap(array, left - 1, right - 1, 0.5, true, false);
                        if (pivotpos == left - 1) pivotpos = right - 1;
                        left++;
                    }
                }
                if (right > end && !brokeloop) Writes.write(array, left - 1, item, 0.5, true, false);
                Highlights.clearAllMarks();
                if (pivotpos != left - 1) Writes.swap(array, pivotpos, left - 1, 0.5, true, false);
                boolean lsmall = left - start < end - (left + 1);
                if (lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, 0);
                }
                if (end - (left + 1) > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, left + 1, left + 1, end, depth + 1, rep + 1);
                    else singularityQuick(array, left + 1, left + 1, end, depth + 1, 0);
                }
                if (!lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, originalpos - 1 > start ? originalpos - 1 : start, left - 1, depth + 1, 0);
                }
            }
        } else shell(array, start - 1, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        depthlimit = (int) Math.min(Math.sqrt(currentLength), 2 * log2(currentLength));
        insertlimit = Math.max((depthlimit / 2) - 1, 15);
        replimit = Math.max((depthlimit / 4), 2);
        int realstart = unstablepd(array, 0, currentLength);
        if (realstart + 1 < currentLength) singularityQuick(array, 1, realstart + 1, currentLength, 0, 0);
    }
}