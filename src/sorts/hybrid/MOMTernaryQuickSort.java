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
public final class MOMTernaryQuickSort extends Sort {

    public MOMTernaryQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("MOM Ternary Quick");
        this.setRunAllSortsName("Median-of-Medians Ternary Quick Sort");
        this.setRunSortName("Median-of-Medians Ternary Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    class PivotPair {
        public int l, r;

        public PivotPair(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }

    public static int floorLog(int n) {
        int log = 0;
        while ((n >>= 1) != 0) ++log;
        return log;
    }

    MaxHeapSort heapSorter;
    InsertionSort insSort;

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
                this.insSort.customInsertSort(array, i, i + s, 0.25, false);
                Writes.swap(array, j++, i + s / 2, 1.0, false, false);
                Highlights.markArray(2, j);
            }
            if (i < end) {
                this.insSort.customInsertSort(array, i, end, 0.25, false);
                Writes.swap(array, j++, i + (end - (ad ? 1 : 0) - i) / 2, 1.0, false, false);
                Highlights.markArray(2, j);
                if ((end - i) % 2 == 0)
                    ad = !ad;
            }
            end = j;
        }
    }

    protected PivotPair partition(int[] array, int a, int b) {
        int piv = array[a];
        int i = a, j = b;
        for(int k = i; k < j; k++) {
            if(Reads.compareValues(array[k], piv) < 0) {
                Writes.swap(array, k, i++, 1.0, true, false);
            }
            else if(Reads.compareValues(array[k], piv) > 0) {
                do {
                    j--;
                    Highlights.markArray(3, j);
                    Delays.sleep(1.0);
                }
                while(j > k && Reads.compareValues(array[j], piv) > 0);

                Writes.swap(array, k, j, 1.0, true, false);
                Highlights.clearMark(3);

                if(Reads.compareValues(array[k], piv) < 0) {
                    Writes.swap(array, k, i++, 1.0, true, false);
                }
            }
        }
        return new PivotPair(i, j);
    }

    protected void quickSort(int[] array, int a, int b, int depth) {
        while(b - a > 16) {
            if(depth == 0) {
                heapSorter.customHeapSort(array, a, b, 1.0);
                return;
            }
            medianOfThree(array, a, b);
            PivotPair mid = partition(array, a, b);
            int l = mid.l - a, r = b - mid.r, eqCnt = mid.r - mid.l;
            if(eqCnt == b - a)
                return;
            if((l == 0 || r == 0) || (l / r >= 16 || r / l >= 16)) {
                medianOfMedians(array, a, b, 5);
                mid = partition(array, a, b);
                l = mid.l - a;
                r = b - mid.r;
                eqCnt = mid.r - mid.l;
                if(eqCnt == b - a)
                    return;
            }
            if(l > r) {
                quickSort(array, mid.r, b, --depth);
                b = mid.l;
            } else {
                quickSort(array, a, mid.l, --depth);
                a = mid.r;
            }
        }
        insSort.customInsertSort(array, a, b, 0.5, false);
    }

    public void customSort(int[] array, int a, int b) {
        heapSorter = new MaxHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        quickSort(array, a, b, 2 * floorLog(b - a));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        heapSorter = new MaxHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        quickSort(array, 0, sortLength, 2 * floorLog(sortLength));
    }

}
