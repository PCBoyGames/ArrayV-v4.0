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
        int i = start;
        int j = end;
        while (i < j) {
            if (Reads.compareIndices(array, i, j, 0.5, true) > 0) {
                Writes.swap(array, i, j, 1, true, false);
                swaps = true;
            }
            i++;
            j--;
        }
        return swaps;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int len = 1;
        int index;
        boolean anyswaps = true;
        boolean swapA = false;
        while (len < currentLength) len *= 2;
        if (len > currentLength) len /= 2;
        int max = len;
        int first = len;
        while (anyswaps) {
            anyswaps = false;
            len = first;
            while (len > 1) {
                index = 0;
                while (index + len - 1 < currentLength) {
                    if (len != 1) {
                        swapA = circle(array, index, index + len - 1);
                        anyswaps = anyswaps || swapA;
                    }
                    index += len;
                }
                len /= 2;
            }
            if (anyswaps) first /= 4;
        }
        Highlights.clearMark(2);
        if (first != max) mergefinal.runSort(array, currentLength, 0);
    }
}