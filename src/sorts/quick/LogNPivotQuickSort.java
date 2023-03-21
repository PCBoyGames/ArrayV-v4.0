package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with _fluffyy and Meme Man

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author _fluffyy
 * @author Meme Man
 *
 */
public final class LogNPivotQuickSort extends Sort {

    public LogNPivotQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Log(N) Pivot Quick");
        this.setRunAllSortsName("Log(N) Pivot Quick Sort");
        this.setRunSortName("Log(N) Pivot Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static double DELAY = 0.25;
    
    public static int log2(int n) {
        int log = 0;
        while ((n >>= 1) != 0)
            ++log;
        return log;
    }
    
    protected void insertTo(int[] array, int a, int b) {
        Highlights.clearMark(2);
        if (a != b) {
            int temp = array[a];
            int d = (a > b) ? -1 : 1;
            for (int i = a; i != b; i += d)
                Writes.write(array, i, array[i + d], 0.5, true, false);
            Writes.write(array, b, temp, 0.5, true, false);
        }
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
    
    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b)
            Writes.swap(array, a, b, pause, mark, aux);
    }
    
    void sort(int[] arr, int start, int stop, int d) {
        int len = stop - start;
        if (len > 16) {
            if (d == 0) {
                heapSort(arr, start, stop);
                return;
            }
            d--;
            int root = log2(len);
            int newStart = start + root;
            this.sort(arr, start, newStart, d);
            int[] pivots = new int[root];
            Writes.changeAllocAmount(pivots.length);
            for (int i = 0; i < root; i++)
                Writes.write(pivots, i, i + start, 0, false, true);
            for (int i = newStart; i < stop; i++) {
                int left = 0, right = root;
                while (left < right) {
                    int mid = (right - left) / 2 + left;
                    if (Reads.compareIndices(arr, pivots[mid], i, DELAY, true) == 1)
                        right = mid;
                    else
                        left = mid + 1;
                }
                int pos = i;
                for (int j = root - 1; j >= left; j--) {
                    swap(arr, pivots[j] + 1, pos, DELAY, true, false);
                    swap(arr, pos = pivots[j], pivots[j] + 1, DELAY, true, false);
                    Writes.write(pivots, j, pivots[j] + 1, 0, false, true);
                }
            }
            this.sort(arr, start, pivots[0], d);
            for (int i = 1; i < root; i++)
                this.sort(arr, pivots[i - 1] + 1, pivots[i], d);
            this.sort(arr, pivots[root - 1] + 1, stop, d);
            Writes.changeAllocAmount(-pivots.length);
        } else {
            insertSort(arr, start, stop);
        }
    }
    
    public void quickSort(int[] array, int a, int b) {
        sort(array, a, b, 2 * log2(b - a));
    }
    
    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
