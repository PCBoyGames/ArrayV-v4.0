package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class LDQuickSort extends Sort {
    public LDQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("LD Quick");
        this.setRunAllSortsName("LD Quick Sort");
        this.setRunSortName("LD Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int partition(int[] array, int lo, int hi) {
        int pivot = array[lo];
        int i = lo;

        for(int j = lo; j <= hi; j++) {
            Highlights.markArray(1, j);
            if(Reads.compareValues(array[j], pivot) < 0) {
                Writes.swap(array, i, j, 1, true, false);
                Writes.swap(array, ++i, j, 1, true, false);
            }
            Delays.sleep(1);
        }
        return i;
    }

    private void quickSort(int[] array, int lo, int hi) {
        if(lo < hi) {
            int p = this.partition(array, lo, hi);
            this.quickSort(array, lo, p - 1);
            this.quickSort(array, p + 1, hi);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1);
    }
}