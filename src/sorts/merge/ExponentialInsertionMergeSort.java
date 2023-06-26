package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Kiriko-chan
 * @author Enver
 *
 */
public final class ExponentialInsertionMergeSort extends Sort {

    public ExponentialInsertionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Exponential Insertion Merge");
        setRunAllSortsName("Exponential Insertion Merge Sort");
        setRunSortName("Exponential Insertion Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    private int expSearch(int[] array, int a, int b, int val, double sleep) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[m]) < 0) b1 = m;
            else a1 = m + 1;
        }
        return a1;
    }

    private void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], sleep, true, false);
            Writes.write(array, b, temp, sleep, true, false);
        }
    }

    public void insertSort(int[] array, int a, int m, int b, double compSleep, double writeSleep) {
        for (int i = m; i < b; i++) {
            insertTo(array, i, expSearch(array, a, i, array[i], compSleep), writeSleep);
        }
    }

    public void sort(int[] array, int a, int b, double compSleep, double writeSleep) {
        if (b - a < 2) return;
        int m = a + (b - a) / 2;
        sort(array, a, m, compSleep, writeSleep);
        sort(array, m, b, compSleep, writeSleep);
        insertSort(array, a, m, b, compSleep, writeSleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 0.5, 0.125);

    }

}
