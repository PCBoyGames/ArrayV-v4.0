package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class BottleSort extends Sort {
    public BottleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Bottle");
        this.setRunAllSortsName("Bottle Sort");
        this.setRunSortName("Bottlesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(24);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        for (int i = 0; i < length - 1; i++) {
            Writes.reversal(array, 0, i-1, 0.075, true, false);
            if (Reads.compareIndices(array, i, i+1, 0.01, true) > 0) {
                Writes.swap(array, i, i+1, 1, true, false);
                i = -1;
            }
        }
        for (int i = length - 2; i > 0; i--)
            Writes.reversal(array, 0, i-1, 0.075, true, false);
    }
}