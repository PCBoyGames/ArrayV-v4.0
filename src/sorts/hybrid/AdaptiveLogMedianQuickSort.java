package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Ayako-chan
 *
 */
public final class AdaptiveLogMedianQuickSort extends Sort {

    public AdaptiveLogMedianQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Log-Median Quick");
        this.setRunAllSortsName("Adaptive Log-Median Quick Sort");
        this.setRunSortName("Adaptive Log-Median Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class PivotPair {
        public int p;
        public boolean alreadyParted;

        public PivotPair(int p, boolean alreadyParted) {
            this.p = p;
            this.alreadyParted = alreadyParted;
        }
    }

    public static int floorLog(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }

    MaxHeapSort heapSorter;
    InsertionSort insSort;
    int threshold = 24;
    int partialInsertLimit = 8;

    protected void medianOfThree(int[] array, int a, int b) {
        int m = a + (b - 1 - a) / 2;
        if (Reads.compareIndices(array, a, m, 1, true) > 0)
            Writes.swap(array, a, m, 1, true, false);
        if (Reads.compareIndices(array, m, b - 1, 1, true) > 0) {
            Writes.swap(array, m, b - 1, 1, true, false);
            if (Reads.compareIndices(array, a, m, 1, true) > 0)
                return;
        }
        Writes.swap(array, a, m, 1, true, false);
    }

    protected void logMedian(int[] array, int a, int b) {
        int n = b - a, cnt = Math.max(floorLog(n), 5);
        cnt -= (1 - cnt % 2);
        int p = a, s = n / cnt;
        int r = (int) (Math.random() * s);
        for (int i = a + r; cnt > 0; cnt--, p++, i += s)
            Writes.swap(array, i, p, 0.5, true, false);
        insSort.customInsertSort(array, a, p, 0.5, false);
        Writes.swap(array, a, a + (p - a) / 2, 0.5, true, false);
    }

    // Refactored from PDQSorting.java
    protected PivotPair partition(int[] array, int a, int b) {
        int pivot = array[a];
        int first = a;
        int last = b;
        while (Reads.compareValues(array[++first], pivot) < 0) {
            Highlights.markArray(1, first);
            Delays.sleep(0.25);
        }
        if (first - 1 == a)
            while (first < last && Reads.compareValues(array[--last], pivot) > 0) {
                Highlights.markArray(2, last);
                Delays.sleep(0.25);
            }
        else
            while (Reads.compareValues(array[--last], pivot) > 0) {
                Highlights.markArray(2, last);
                Delays.sleep(0.25);
            }
        boolean alreadyParted = first >= last;
        while (first < last) {
            Writes.swap(array, first, last, 1, true, false);
            while (Reads.compareValues(array[++first], pivot) < 0) {
                Highlights.markArray(1, first);
                Delays.sleep(0.5);
            }
            while (Reads.compareValues(array[--last], pivot) > 0) {
                Highlights.markArray(2, last);
                Delays.sleep(0.5);
            }
        }
        Highlights.clearMark(2);
        int pivotPos = first - 1;
        Writes.write(array, a, array[pivotPos], 1, true, false);
        Writes.write(array, pivotPos, pivot, 1, true, false);
        return new PivotPair(pivotPos, alreadyParted);
    }

    // Refactored from PDQSorting.java
    protected boolean partialInsert(int[] array, int a, int b) {
        if (a == b)
            return true;
        double sleep = 0.25;
        int c = 0;
        for (int i = a + 1; i < b; i++) {
            if (c > partialInsertLimit)
                return false;
            if (Reads.compareIndices(array, i - 1, i, sleep, true) > 0) {
                int t = array[i];
                int j = i;
                do {
                    Writes.write(array, j, array[j - 1], sleep, true, false);
                    j--;
                } while (j - 1 >= a && Reads.compareValues(array[j - 1], t) > 0);
                Writes.write(array, j, t, sleep, true, false);
                c += i - j;
            }
        }
        return true;
    }

    protected void quickSort(int[] array, int a, int b, int badAllowed, boolean logMed) {
        while (b - a > threshold) {
            if (!logMed)
                medianOfThree(array, a, b);
            boolean logMedL = false, logMedR = false;
            PivotPair partResult = partition(array, a, b);
            int p = partResult.p;
            boolean alreadyParted = partResult.alreadyParted;
            int l = p - a, r = b - (p + 1);
            if (l < (b - a) / 16 || r < (b - a) / 16) {
                if (--badAllowed == 0) {
                    heapSorter.customHeapSort(array, a, b, 1);
                    return;
                }
                if (l > threshold) {
                    logMedian(array, a, p);
                    logMedL = true;
                }
                if (r > threshold) {
                    logMedian(array, p + 1, b);
                    logMedR = true;
                }
            } else if (alreadyParted && partialInsert(array, a, p)
                                     && partialInsert(array, p + 1, b))
                return;
            if (l > r) {
                quickSort(array, p + 1, b, badAllowed, logMedR);
                b = p;
                logMed = logMedL;
            } else {
                quickSort(array, a, p, badAllowed, logMedL);
                a = p + 1;
                logMed = logMedR;
            }
        }
        insSort.customInsertSort(array, a, b, 0.5, false);
    }

    public void customSort(int[] array, int a, int b) {
        heapSorter = new MaxHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        quickSort(array, a, b, floorLog(b - a), false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        heapSorter = new MaxHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        quickSort(array, 0, sortLength, floorLog(sortLength), false);

    }

}
