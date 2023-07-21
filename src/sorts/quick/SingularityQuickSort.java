package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.PDBinaryInsertionSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class SingularityQuickSort extends Sort {

    int depthlimit;
    int insertlimit;
    int replimit;

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
        for (int i = start; i < end; i++) {
            int left = i;
            while (Reads.compareIndices(array, i, i + 1, 0.25, true) == 0 && i < end) i++;
            int right = i;
            if (left != right) {
                if (right - left < 3) Writes.swap(array, left, right, 0.75, true, false);
                else Writes.reversal(array, left, right, 0.75, true, false);
            }
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
            else if (reverse < start + 3) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }

    protected void binsert(int[] array, int start, int end) {
        PDBinaryInsertionSort bin = new PDBinaryInsertionSort(arrayVisualizer);
        bin.pdbinsert(array, start - 1, end, 0.1, false);
    }

    protected void singularityQuick(int[] array, int start, int offset, int end, int depth, int rep) {
        Writes.recordDepth(depth);
        Highlights.clearAllMarks();
        if (end - start > insertlimit && depth < depthlimit && rep < 4) {
            int left = offset;
            while (Reads.compareIndices(array, left - 1, left, 0.05, true) <= 0 && left < end) left++;
            if (left < end) {
                int right = left + 1;
                int pivot = array[left - 1];
                int originalpos = left - 1;
                boolean brokeloop = false;
                for (boolean brokencond = false; right <= end; right++) {
                    if (Reads.compareValues(pivot, array[right - 1]) > 0) {
                        Highlights.clearMark(2);
                        if (right - left == 1) Writes.write(array, left - 1, array[left], 0.1, true, false);
                        else brokeloop = true;
                        if (brokeloop && !brokencond) Writes.write(array, left - 1, pivot, 0.1, brokencond = true, false);
                        if (right - left > 1) {
                            Highlights.clearMark(2);
                            Writes.insert(array, right - 1, left - 1, 0.1, true, false);
                        }
                        left++;
                    }
                }
                if (right > end && !brokeloop) Writes.write(array, left - 1, pivot, 0.1, true, false);
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
        } else binsert(array, start, end);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        depthlimit = (int) Math.min(Math.sqrt(currentLength), 2 * log2(currentLength));
        insertlimit = Math.max((depthlimit / 2) - 1, 15);
        replimit = Math.max((depthlimit / 4), 2);
        int realstart = pd(array, 0, currentLength);
        if (realstart + 1 < currentLength) singularityQuick(array, 1, realstart + 1, currentLength, 0, 0);
    }
}