package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/*

Coded for ArrayV by Kiriko-chan

-----------------------------
- Sorting Algorithm Scarlet -
-----------------------------

 */

/**
 * @author Kiriko-chan
 *
 */
public final class AdaptiveMOMQuickSort extends Sort {

    public AdaptiveMOMQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive MOM Quick");
        this.setRunAllSortsName("Adaptive Median-of-Medians Quick Sort");
        this.setRunSortName("Adaptive Median-of-Medians Quicksort");
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
    int threshold = 16;
    int partialInsertLimit = 8;

    void medianOfThree(int[] array, int a, int b) {
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

    void medianOfMedians(int[] array, int a, int b, int s) {
        int end = b, start = a, i, j;
        boolean ad = true;

        while (end - start > 1) {
            j = start;
            Highlights.markArray(2, j);
            for (i = start; i + 2 * s <= end; i += s) {
                insSort.customInsertSort(array, i, i + s, 0.25, false);
                Writes.swap(array, j++, i + s / 2, 1.0, false, false);
                Highlights.markArray(2, j);
            }
            if (i < end) {
                insSort.customInsertSort(array, i, end, 0.25, false);
                Writes.swap(array, j++, i + (end - (ad ? 1 : 0) - i) / 2, 1.0, false, false);
                Highlights.markArray(2, j);
                if ((end - i) % 2 == 0)
                    ad = !ad;
            }
            end = j;
        }
    }

    //Refactored from PDQSorting.java
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

    //Refactored from PDQSorting.java
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

    protected void quickSort(int[] array, int a, int b, int badAllowed, boolean mom) {
        while (b - a > threshold) {
            if (!mom) {
                medianOfThree(array, a, b);
            }
            boolean momLeft = false, momRight = false;
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
                    medianOfMedians(array, a, p, 5);
                    momLeft = true;
                }
                if (r > threshold) {
                    medianOfMedians(array, p + 1, b, 5);
                    momRight = true;
                }
            } else if (alreadyParted && partialInsert(array, a, p) 
                                     && partialInsert(array, p + 1, b))
                return;
            if (l > r) {
                quickSort(array, p + 1, b, badAllowed, momRight);
                b = p;
                mom = momLeft;
            } else {
                quickSort(array, a, p, badAllowed, momLeft);
                a = p + 1;
                mom = momRight;
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
