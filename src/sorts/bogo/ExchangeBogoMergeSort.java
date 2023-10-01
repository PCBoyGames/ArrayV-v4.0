package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

public class ExchangeBogoMergeSort extends BogoSorting {

    public ExchangeBogoMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exchange Bogo Merge");
        this.setRunAllSortsName("Exchange Bogo Merge Sort");
        this.setRunSortName("Exchange Bogo Mergesort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(true);
    }

    protected void exchangeBogo(int[] array, int a, int b) {
        while (!isRangeSorted(array, a, b, false, true)) {
            int choice = BogoSorting.randInt(0, 4);
            if (choice == 0)
                for (int i = a; i < b; i++) {
                    int j = BogoSorting.randInt(i, b);
                    if (Reads.compareIndices(array, i, j, delay, true) > 0)
                        Writes.swap(array, i, j, delay, true, false);
                }
            if (choice == 1)
                for (int i = b - 1; i > a; i--) {
                    int j = BogoSorting.randInt(a, i);
                    if (Reads.compareIndices(array, i, j, delay, true) < 0)
                        Writes.swap(array, i, j, delay, true, false);
                }
            if (choice == 2)
                for (int i = a + 1; i < b; i++) {
                    int j = BogoSorting.randInt(a, i);
                    if (Reads.compareIndices(array, i, j, delay, true) < 0)
                        Writes.swap(array, i, j, delay, true, false);
                }
            if (choice == 3)
                for (int i = b - 2; i >= a; i--) {
                    int j = BogoSorting.randInt(i + 1, b);
                    if (Reads.compareIndices(array, i, j, delay, true) > 0)
                        Writes.swap(array, i, j, delay, true, false);
                }
        }
    }

    public void mergeSort(int[] array, int a, int b) {
        if (b - a < 2)
            return;
        int m = a + (b - a) / 2;
        mergeSort(array, a, m);
        mergeSort(array, m, b);
        exchangeBogo(array, a, b);
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        mergeSort(array, 0, length);

    }

}
