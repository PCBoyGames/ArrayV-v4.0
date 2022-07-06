package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class ExponentialInsertionSort extends Sort {

    public ExponentialInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exponential Insertion");
        this.setRunAllSortsName("Exponential Insertion Sort");
        this.setRunSortName("Exponential Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int rightBinSearch(int[] array, int a, int b, int val, double sleep) {
        while (a < b) {
            int m = a+(b-a)/2;
            Highlights.markArray(0, a);
            Highlights.markArray(1, m);
            Highlights.markArray(2, b);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[m]) < 0)
                b = m;
            else
                a = m+1;
        }

        return a;
    }

    private int rightExpSearch(int[] array, int a, int b, int val, double sleep) {
        int i = 1;
        while (b-i >= a && Reads.compareValues(val, array[b-i]) < 0) {
            i *= 2;
        }
        return rightBinSearch(array, Math.max(a, b-i+1), b-i/2, val, sleep);
    }

    private void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        if (a > b) {
            int temp = array[a];
            do
                Writes.write(array, a, array[--a], sleep, true, false);
            while (a > b);
            Writes.write(array, b, temp, sleep, true, false);
        }
    }

    private void insertionSort(int[] array, int a, int b, double compSleep, double writeSleep) {
        for (int i = a + 1; i < b; i++) {
            insertTo(array, i, rightExpSearch(array, a, i, array[i], compSleep), writeSleep);
        }
    }

    public void customSort(int[] array, int a, int b, double sleep) {
        insertionSort(array, a, b, sleep / 2.0, sleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) throws Exception {
        insertionSort(array, 0, sortLength, 4, 0.8);

    }

}
