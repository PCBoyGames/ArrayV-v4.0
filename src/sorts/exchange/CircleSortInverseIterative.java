package sorts.exchange;

import main.ArrayVisualizer;
import sorts.merge.QuadSort;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class CircleSortInverseIterative extends Sort {
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
        for (; a < b; a++, b--) if (Reads.compareIndices(array, a, b, 0.5, true) > 0) Writes.swap(array, a, b, 0.5, swaphere = true, false);
        if (anyswaps) return anyswaps;
        else return swaphere;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int gap = 2;
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            gap = 2;
            while (gap <= currentLength) {
                for (int offset = 0; offset + (gap - 1) < currentLength; offset += gap) anyswaps = circle(array, offset, offset + (gap - 1), anyswaps);
                gap *= 2;
            }
            if (gap / 2 != currentLength && !anyswaps) {
                QuadSort quad = new QuadSort(arrayVisualizer);
                quad.runSort(array, currentLength, 0);
            }
        }
    }
}