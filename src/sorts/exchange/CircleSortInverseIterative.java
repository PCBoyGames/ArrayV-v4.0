package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class CircleSortInverseIterative extends Sort {

    int len;

    public CircleSortInverseIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Circle (Inverse Iterative)");
        this.setRunAllSortsName("Inverse Iterative Circle Sort");
        this.setRunSortName("Inverse Iterative Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean circle(int[] array, int a, int b, boolean anyswaps) {
        boolean swaphere = false;
        for (; a < b; a++, b--) if (a < len && b < len) if (Reads.compareIndices(array, a, b, 0.5, true) > 0) Writes.swap(array, a, b, 0.5, swaphere = true, false);
        if (anyswaps) return anyswaps;
        else return swaphere;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        len = currentLength;
        int rLen = 1;
        for (; rLen < currentLength; rLen *= 2);
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            for (int gap = 2; gap <= rLen; gap *= 2) for (int offset = 0; offset + (gap - 1) < rLen; offset += gap) anyswaps = circle(array, offset, offset + (gap - 1), anyswaps);
        }
    }
}