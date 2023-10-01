package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/**
 * @author Kiriko-chan
 *
 */
public class BubbleBogoMergeSort extends BogoSorting {

    public BubbleBogoMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bubble Bogo Merge");
        this.setRunAllSortsName("Bubble Bogo Merge Sort");
        this.setRunSortName("Bubble Bogo Mergesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(true);
    }

    protected void bubbleBogoSort(int[] array, int a, int b) {
        while (!this.isRangeSorted(array, a, b, false, true)) {
            int i = BogoSorting.randInt(a, b - 1);
            if (Reads.compareIndices(array, i, i + 1, this.delay, true) > 0)
                Writes.swap(array, i, i + 1, this.delay, true, false);
        }
    }

    protected void sort(int[] array, int a, int b, int depth) {
        Writes.recordDepth(depth);
        if (b - a < 2)
            return;
        int m = a + (b - a) / 2;
        Writes.recursion();
        sort(array, a, m, depth + 1);
        Writes.recursion();
        sort(array, m, b, depth + 1);
        bubbleBogoSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        delay = 0.05;
        sort(array, 0, sortLength, 0);

    }

}
