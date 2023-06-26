package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class AdaptiveDualPivotQuickSort extends Sort {

    public AdaptiveDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Dual-Pivot Quick");
        this.setRunAllSortsName("Adaptive Dual-Pivot Quick Sort");
        this.setRunSortName("Adaptive Dual-Pivot Quicksort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    final int insertThreshold = 24;
    final int partialInsertLimit = 8;

    protected void insertSort(int[] array, int a, int b) {
        double sleep = 0.5;
        for (int i = a + 1; i < b; i++) {
            if (Reads.compareIndices(array, i - 1, i, sleep , true) > 0) {
                Highlights.clearMark(2);
                int t = array[i];
                int j = i;
                do {
                    Writes.write(array, j, array[j - 1], sleep, true, false);
                    j--;
                } while (j - 1 >= a && Reads.compareValues(array[j - 1], t) > 0);
                Writes.write(array, j, t, sleep, true, false);
            }
        }
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
                Highlights.clearMark(2);
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

    protected void quickSort(int[] array, int a, int b, int d) {
        int n = b - a;
        if (n <= insertThreshold) {
            insertSort(array, a, b);
            return;
        }
        int piv1, piv2, s = n/d;

        if (Reads.compareValues(array[a+s], array[b-1-s]) > 0) {
            piv1 = array[b-1-s];
            piv2 = array[a+s];
        }
        else {
            piv1 = array[a+s];
            piv2 = array[b-1-s];
        }
        int i = a, j = b;
        for (int k = i; k < j; k++) {
            if (Reads.compareIndexValue(array, k, piv1, 0.5, true) < 0)
                Writes.swap(array, k, i++, 0.5, true, false);
            else if (Reads.compareIndexValue(array, k, piv2, 0.5, true) > 0) {
                do {
                    j--;
                    Highlights.markArray(3, j);
                    Delays.sleep(0.5);
                } while (k < j && Reads.compareIndexValue(array, j, piv2, 0.5, false) > 0);
                Writes.swap(array, k, j, 0.5, true, false);
                if (Reads.compareIndexValue(array, k, piv1, 0.5, true) < 0)
                    Writes.swap(array, k, i++, 0.5, true, false);
            }
        }
        boolean lSorted = partialInsert(array, a, i),
                mSorted = partialInsert(array, i, j),
                rSorted = partialInsert(array, j, b);
        if (lSorted && mSorted && rSorted)
            return;
        if (Math.min(i - a, Math.min(j - i, b - j)) <= insertThreshold)
            d++;
        if (Reads.compareValues(piv1, piv2) < 0)
            quickSort(array, i, j, d);
        quickSort(array, a, i, d);
        quickSort(array, j, b, d);
    }

    public void customSort(int[] array, int a, int b) {
        quickSort(array, a, b, 3);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength, 3);

    }

}
