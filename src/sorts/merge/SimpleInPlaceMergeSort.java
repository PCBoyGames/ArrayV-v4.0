package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SimpleInPlaceMergeSort extends Sort {
    public SimpleInPlaceMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Simple In-Place Merge");
        this.setRunAllSortsName("Simple In-Place Merge Sort");
        this.setRunSortName("Simple In-Place Mergesort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void merge(int[] array, int a, int m, int b) {
        int i = a, j = m;
        while (i < j && j < b) {
            Highlights.markArray(3, i);
            Delays.sleep(0.01);
            if (Reads.compareValues(array[i], array[j]) > 0)
                Writes.multiSwap(array, j++, i, 0.01, true, false);
            i++;
        }
        Highlights.clearMark(3);
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        merge(array, a, m, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);
    }
}
