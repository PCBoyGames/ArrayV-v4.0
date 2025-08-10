package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class OddEvenInsertionSort extends Sort {
    public OddEvenInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Odd-Even Insertion");
        this.setRunAllSortsName("Odd-Even Insertion Sort");
        this.setRunSortName("Odd-Even Insertionsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int left, int right) {
        for (int i = left + 1; i < right; i++)
            for (int j = i; j > left; j -= 2)
                if (Reads.compareIndices(array, j - 1, j, 0.05, true) > 0)
                    Writes.swap(array, j - 1, j, 0.05, true, false);
        for (int i = right - 2; i > left; i--)
            for (int j = i; j > left; j -= 2)
                if (Reads.compareIndices(array, j - 1, j, 0.05, true) > 0)
                    Writes.swap(array, j - 1, j, 0.05, true, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}

