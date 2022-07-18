package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with _fluffyy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author _fluffyy
 *
 */
public final class SmartSemiMedianQuickSort extends Sort {

    public SmartSemiMedianQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Smart Semi-Median Quick");
        this.setRunAllSortsName("Smart Semi-Median Quick Sort");
        this.setRunSortName("Smart Semi-Median Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public static int floorLog(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }

    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a == b)
            return;
        int temp = array[a];
        int d = (a > b) ? -1 : 1;
        for (int i = a; i != b; i += d)
            Writes.write(array, i, array[i + d], 0.5, true, false);
        Writes.write(array, b, temp, 0.5, true, false);
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0)
            i *= 2;
        int a1 = Math.max(a, b - i + 1), b1 = b - i / 2;
        while (a1 < b1) {
            int m = a1 + (b1 - a1) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            if (Reads.compareValues(val, array[m]) < 0)
                b1 = m;
            else
                a1 = m + 1;
        }
        return a1;
    }

    protected void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    protected void sift(int[] array, int start, int root, int len, int tmp) {
        int j = root;
        while (2 * j + 1 < len) {
            j = 2 * j + 1;
            if (j + 1 < len && Reads.compareValues(array[start + j], array[start + j + 1]) < 0) {
                j++;
            }
        }
        while (Reads.compareValueIndex(array, tmp, start + j, 0.25, true) > 0) {
            j = (j - 1) / 2;
        }
        for (int t2; j > root; j = (j - 1) / 2) {
            t2 = array[start + j];
            Writes.write(array, start + j, tmp, 0.5, true, false);
            tmp = t2;
        }
        Writes.write(array, start + root, tmp, 0.5, true, false);
    }

    protected void heapSort(int[] array, int start, int end) {
        int p = end - start;
        for (int j = (p - 1) / 2; j >= 0; j--) {
            this.sift(array, start, j, p, array[start + j]);
        }
        for (int j = p - 1; j > 0; j--) {
            int t = array[start + j];
            Writes.write(array, start + j, array[start], 1, true, false);
            this.sift(array, start, 0, j, t);
        }
    }

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

    protected void semiMedian(int[] array, int a, int b) {
        int len = b - a;
        boolean invert = false;
        for (int i = 1; i < len; i *= 2, invert = !invert)
            for (int j = a; j < b - i; j += i * 2) {
                int c = Reads.compareIndices(array, j, j + i, 1, true);
                if (invert ? c < 0 : c > 0)
                    Writes.swap(array, j, j + i, 1, true, false);
            }
    }

    public int partition(int[] array, int a, int b, int p) {
        int i = a, j = b;
        if (a != p)
            Writes.swap(array, a, p, 1, true, false);
        Highlights.markArray(3, a);
        while (true) {
            do {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            } while (i < j && Reads.compareIndices(array, i, a, 0, false) < 0);
            do {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            } while (j >= i && Reads.compareIndices(array, j, a, 0, false) > 0);
            if (i >= j) {
                if (a != j)
                    Writes.swap(array, a, j, 1, true, false);
                Highlights.clearMark(3);
                return j;
            }
            Writes.swap(array, i, j, 1, true, false);
        }
    }

    public boolean getSortedRuns(int[] array, int a, int b) {
        Highlights.clearAllMarks();
        boolean reverseSorted = true;
        boolean sorted = true;
        int comp;
        for (int i = a; i < b - 1; i++) {
            comp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            if (comp > 0)
                sorted = false;
            else
                reverseSorted = false;
            if ((!reverseSorted) && (!sorted))
                return false;
        }
        if (reverseSorted && !sorted) {
            Writes.reversal(array, a, b - 1, 1, true, false);
            sorted = true;
        }
        return sorted;
    }

    protected void sortHelper(int[] array, int a, int b, int badAllowed) {
        while (b - a > 16) {
            if (getSortedRuns(array, a, b))
                return;
            medianOfThree(array, a, b);
            int m = partition(array, a, b, a);
            int lLen = m - a, rLen = b - m - 1;
            if (lLen < (b - a) / 16 || rLen < (b - a) / 16) {
                if (--badAllowed <= 0) {
                    heapSort(array, a, b);
                    return;
                }
                semiMedian(array, a, b);
                m = partition(array, a, b, a);
                lLen = m - a;
                rLen = b - m - 1;
            }
            if (rLen < lLen) {
                sortHelper(array, m + 1, b, badAllowed);
                b = m;
            } else {
                sortHelper(array, a, m, badAllowed);
                a = m + 1;
            }
        }
        insertSort(array, a, b);
    }

    public void quickSort(int[] array, int a, int b) {
        sortHelper(array, a, b, floorLog(b - a));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
