package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class DoubleCircleSortIterative extends Sort {

    int lastSize = 0;
    int len;

    public DoubleCircleSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Double Circle (Iterative)");
        this.setRunAllSortsName("Double Iterative Circle Sort");
        this.setRunSortName("Double Iterative Circlesort");
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

    protected boolean TD(int[] array, int start, int end) {
        boolean here = false;
        for (int i = end - start; i > 1; i /= 2) {
            if (i != lastSize) {
                for (int j = start; j + i <= end; j += i) here = circle(array, j, j + i - 1, here);
                lastSize = i;
            }
        }
        return here;
    }

    protected boolean BU(int[] array, int start, int end) {
        boolean here = false;
        for (int i = 2; i <= end - start; i *= 2) {
            if (i != lastSize) {
                for (int j = start; j + i <= end; j += i) here = circle(array, j, j + i - 1, here);
                lastSize = i;
            }
        }
        return here;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int rLen = 1;
        for (; rLen < currentLength; rLen *= 2);
        boolean swaps = true;
        len = currentLength;
        while (swaps) {
            swaps = TD(array, 0, rLen);
            if (!swaps) break;
            swaps = BU(array, 0, rLen);
        }
    }
}