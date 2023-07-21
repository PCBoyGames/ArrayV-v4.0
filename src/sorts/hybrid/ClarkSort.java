package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.merge.QuadSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ClarkSort extends Sort {
    QuadSort mergefinal = new QuadSort(arrayVisualizer);
    public ClarkSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Clark");
        this.setRunAllSortsName("Clark Sort");
        this.setRunSortName("Clarksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean circle(int[] array, int start, int end) {
        boolean swaps = false;
        for (; start < end; start++, end--) if (Reads.compareIndices(array, start, end, 0.5, true) > 0) Writes.swap(array, start, end, 1, swaps = true, false);
        return swaps;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 1;
        boolean anyswaps = true;
        boolean swapA = false;
        while (len < currentLength) len *= 2;
        if (len > currentLength) len /= 2;
        int max = len;
        int first = len;
        while (anyswaps) {
            anyswaps = false;
            len = first;
            for (; len > 1; len /= 2) {
                for (int index = 0; index + len - 1 < currentLength; index += len) {
                    if (len != 1) {
                        swapA = circle(array, index, index + len - 1);
                        anyswaps = anyswaps || swapA;
                    }
                }
            }
            if (anyswaps) first /= 4;
        }
        Highlights.clearMark(2);
        if (first != max) mergefinal.runSort(array, currentLength, 0);
    }
}