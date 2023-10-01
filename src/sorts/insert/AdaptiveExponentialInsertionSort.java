package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 *
 */
public class AdaptiveExponentialInsertionSort extends Sort {

    public AdaptiveExponentialInsertionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Exponential Insertion");
        this.setRunAllSortsName("Adaptive Exponential Insertion Sort");
        this.setRunSortName("Adaptive Exponential Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int expSearch(int[] array, int a, int b, int val, double sleep) {
        int i = 1;
        int a1, b1;
        boolean dir = Reads.compareIndexValue(array, a + (b - a) / 2, val, 0, false) <= 0;
        if (dir) {
            while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
                i *= 2;
            a1 = Math.max(a, b - i + 1);
            b1 = b - i / 2;
        } else {
            while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0)
                i *= 2;
            a1 = a + i / 2;
            b1 = Math.min(b, a - 1 + i);
        }
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(1, a1);
            Highlights.markArray(2, m);
            Highlights.markArray(3, b1);
            Delays.sleep(sleep);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        Highlights.clearAllMarks();
        return a1;
    }

    protected void insertTo(int[] array, int a, int b, double sleep) {
        Highlights.clearMark(2);
        int temp = array[a];
        for (int i = a; i > b; i--)
            Writes.write(array, i, array[i - 1], sleep, true, false);
        if (a != b)
            Writes.write(array, b, temp, sleep, true, false);
    }

    protected int findRun(int[] array, int a, int b, double compSleep, double writeSleep) {
        int i = a + 1;
        boolean dir;
        if (i < b)
            dir = Reads.compareIndices(array, i - 1, i++, compSleep, true) <= 0;
        else
            dir = true;
        if (dir)
            while (i < b && Reads.compareIndices(array, i - 1, i, compSleep, true) <= 0)
                i++;
        else {
            while (i < b && Reads.compareIndices(array, i - 1, i, compSleep, true) > 0)
                i++;
            if (i - a < 4)
                Writes.swap(array, a, i - 1, writeSleep, true, false);
            else
                Writes.reversal(array, a, i - 1, writeSleep, true, false);
        }
        Highlights.clearMark(2);
        return i;
    }

    public void insertSort(int[] array, int a, int b, double cSleep, double wSleep) {
        int i = findRun(array, a, b, cSleep, wSleep);
        for (; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i], cSleep), wSleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSort(array, 0, sortLength, 1.0, 0.25);

    }

}
