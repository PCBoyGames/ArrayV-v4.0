package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class NHyperStoogeSort extends Sort {
    public NHyperStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("N-Hyper Stooge");
        this.setRunAllSortsName("N-Hyper Stooge Sort");
        this.setRunSortName("N-Hyper Stoogesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(5);
        this.setBogoSort(false);
    }
    private void nHyperStooge(int[] array, int start, int end, int order, int depth) {
        if(order < 1 || start >= end)
            return;
        Writes.recordDepth(depth++);
        if(Reads.compareIndices(array, start, end, 0.001, true) == 1)
            Writes.swap(array, start, end, 1, true, false);
        if (start < end) {
            Writes.recursion(4);
            nHyperStooge(array, start, end - 1, order, depth);
            nHyperStooge(array, start + 1, end, order, depth);
            nHyperStooge(array, start, end - 1, order, depth);
            nHyperStooge(array, start, end, order - 1, depth);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        nHyperStooge(array, 0, currentLength - 1, currentLength+1, 0);
    }
}