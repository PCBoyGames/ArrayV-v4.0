package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 *
 */
public class AdaptiveDualPivotQuickSort extends Sort {

    public AdaptiveDualPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Dual-Pivot Quick");
        this.setRunAllSortsName("Adaptive Dual-Pivot Quick Sort");
        this.setRunSortName("Adaptive Dual-Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static int insertThreshold = 32;

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0) b1 = m;
            else a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        double sleep = 0.5;
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            int dest = expSearch(array, a, i, current);
            int pos = i - 1;
            while (pos >= dest) {
                Writes.write(array, pos + 1, array[pos], sleep, true, false);
                pos--;
            }
            if (pos + 1 < i) Writes.write(array, pos + 1, current, sleep, true, false);
        }
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    protected int[] partition(int[] array, int a, int b, int piv1, int piv2) {
        int i = a, j = b;
        for (int k = i; k < j; k++) {
            if (Reads.compareIndexValue(array, k, piv1, 0.5, true) < 0)
                swap(array, k, i++, 0.5, true, false);
            else if (Reads.compareIndexValue(array, k, piv2, 0.5, true) > 0) {
                do {
                    j--;
                    Highlights.markArray(3, j);
                    Delays.sleep(0.5);
                } while (k < j && Reads.compareIndexValue(array, j, piv2, 0.5, false) > 0);
                swap(array, k, j, 0.5, true, false);
                if (Reads.compareIndexValue(array, k, piv1, 0.5, true) < 0)
                    swap(array, k, i++, 0.5, true, false);
            }
        }
        Highlights.clearAllMarks();
        return new int[] { i, j };
    }

    protected int[] partitionInverted(int[] array, int a, int b, int piv1, int piv2) {
        int i = a, j = b;
        for (int k = i; k < j; k++) {
            if (Reads.compareIndexValue(array, k, piv1, 0.5, true) <= 0)
                swap(array, k, i++, 0.5, true, false);
            else if (Reads.compareIndexValue(array, k, piv2, 0.5, true) >= 0) {
                do {
                    j--;
                    Highlights.markArray(3, j);
                    Delays.sleep(0.5);
                } while (k < j && Reads.compareIndexValue(array, j, piv2, 0.5, false) >= 0);
                swap(array, k, j, 0.5, true, false);
                if (Reads.compareIndexValue(array, k, piv1, 0.5, true) <= 0)
                    swap(array, k, i++, 0.5, true, false);
            }
        }
        Highlights.clearAllMarks();
        return new int[] { i, j };
    }

    public boolean getSortedRuns(int[] array, int start, int end) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;

        for (int i = start; i < end-1; i++) {
            comp = Reads.compareIndices(array, i, i+1, 0.5, true);
            if (comp > 0) sorted = false;
            else reverseSorted = false;
            if ((!reverseSorted) && (!sorted)) return false;
        }

        if (reverseSorted && !sorted) {
            Writes.reversal(array, start, end-1, 1, true, false);
            sorted = true;
        }

        return sorted;
    }

    protected void quickSort(int[] array, int a, int b, int d) {
        int n = b - a;
        if (n <= insertThreshold) {
            insertSort(array, a, b);
            return;
        }
        if (getSortedRuns(array, a, b)) return;
        int piv1, piv2, s = n/d;

        if (Reads.compareValues(array[a+s], array[b-1-s]) > 0) {
            piv1 = array[b-1-s];
            piv2 = array[a+s];
        }
        else {
            piv1 = array[a+s];
            piv2 = array[b-1-s];
        }
        int[] pr = partition(array, a, b, piv1, piv2);
        int i = pr[0], j = pr[1];
        if (Math.min(i - a, Math.min(j - i, b - j)) <= insertThreshold)
            d++;
        quickSort(array, a, i, d);
        quickSort(array, j, b, d);
        if (a == i && j == b) {
            pr = partitionInverted(array, a, b, piv1, piv2);
            i = pr[0]; j = pr[1];
        }
        if (Reads.compareValues(piv1, piv2) < 0)
            quickSort(array, i, j, d);
    }

    public void customSort(int[] array, int a, int b) {
        quickSort(array, a, b, 3);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        customSort(array, 0, sortLength);

    }

}
