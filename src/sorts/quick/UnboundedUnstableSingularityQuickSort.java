package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class UnboundedUnstableSingularityQuickSort extends Sort {    
    public UnboundedUnstableSingularityQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unstable Singularity Quick (Unbounded)");
        this.setRunAllSortsName("Unbounded Unstable Singularity Quick Sort");
        this.setRunSortName("Unbounded Unstable Singularity Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    protected void singularityQuick(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        Highlights.clearAllMarks();
        int left = start;
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
                        Writes.write(array, left - 1, array[left], 0.1, true, false);
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
                singularityQuick(array, start, left - 1, depth + 1);
            }
            if (end - (left + 1) > 0) {
                Writes.recursion();
                singularityQuick(array, left + 1, end, depth + 1);
            }
            if (!lsmall && (left - 1) - start > 0) {
                Writes.recursion();
                singularityQuick(array, start, left - 1, depth + 1);
            }
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        singularityQuick(array, 1, currentLength, 0);
    }
}