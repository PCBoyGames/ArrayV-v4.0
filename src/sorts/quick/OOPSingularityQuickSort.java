package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.QuadSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OOPSingularityQuickSort extends QuadSorting {

    int depthlimit;
    int insertlimit;
    int replimit;

    public OOPSingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Out of Place Singularity Quick");
        this.setRunAllSortsName("Out of Place Singularity Quick Sort");
        this.setRunSortName("Out of Place Singularity Quicksort");
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
            else if (reverse < start + 3) Writes.swap(array, start, reverse, 0.75, true, false);
            else Writes.reversal(array, start, reverse, 0.75, true, false);
        }
        return reverse;
    }
    
    protected int partition(int[] array, int start, int end, int pivot) {
        int[] low = Writes.createExternalArray(end - start);
        int[] high = Writes.createExternalArray(end - start);
        int itemslow = 0;
        int itemshigh = 0;
        for (int i = start; i < end; i++) {
            Highlights.markArray(1, i);
            int cmp = Reads.compareValues(array[i], pivot);
            if (cmp < 0) {
                Writes.write(low, itemslow, array[i], 0.25, false, true);
                itemslow++;
            } else {
                Writes.write(high, itemshigh, array[i], 0.25, false, true);
                itemshigh++;
            }
        }
        Writes.arraycopy(low, 0, array, start, itemslow, 0.25, true, false);
        Writes.arraycopy(high, 0, array, start + itemslow, itemshigh, 0.25, true, false);
        Writes.deleteExternalArray(low);
        Writes.deleteExternalArray(high);
        return start + itemslow;
    }

    protected void singularityQuick(int[] array, int start, int end, int depth, int rep) {
        Writes.recordDepth(depth);
        Highlights.clearAllMarks();
        if (end - start > insertlimit && depth < depthlimit && rep < 4) {
            int left = start;
            while (Reads.compareIndices(array, left - 1, left, 0.05, true) <= 0 && left < end) left++;
            if (left < end) {
                Highlights.clearAllMarks();
                left = partition(array, start - 1, end, array[left - 1]) + 1;
                Highlights.clearAllMarks();
                boolean lsmall = left - start < end - (left + 1);
                if (lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, left - 1, depth + 1, 0);
                }
                if (end - (left + 1) > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, left + 1, end, depth + 1, rep + 1);
                    else singularityQuick(array, left + 1, end, depth + 1, 0);
                }
                if (!lsmall && (left - 1) - start > 0) {
                    Writes.recursion();
                    if (end - replimit <= left || left <= start + replimit) singularityQuick(array, start, left - 1, depth + 1, rep + 1);
                    else singularityQuick(array, start, left - 1, depth + 1, 0);
                }
            }
        } else quadSort(array, start - 1, end - start + 1);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        depthlimit = (int) Math.min(Math.sqrt(currentLength), 2 * log2(currentLength));
        insertlimit = Math.max((depthlimit / 2) - 1, 15);
        replimit = Math.max((depthlimit / 4), 2);
        if (pd(array, 0, currentLength) + 1 < currentLength) {
            singularityQuick(array, 1, currentLength, 0, 0);
        }
    }
}