package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class YSlowSort extends Sort {
    public YSlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Y-Slow");
        this.setRunAllSortsName("Y-Slow Sort");
        this.setRunSortName("Y-Slowsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(32);
        this.setBogoSort(false);
    }

    private void yStooge(int[] array, int left, int right) {
        if (Reads.compareValues(array[left], array[right]) > 0) Writes.swap(array, left, right, 1, true, false);
        int m = (right - left + 1) / 2;
        if (right - left > 1) {
            for (int run = 1; run <= 2; run++) {
                yStooge(array, left, right - m);
                yStooge(array, left + m, right);
                yStooge(array, left + 1, right - 1);
            }
        }
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        yStooge(array, 0, length-1);
    }
}
