package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Haruki
in collaboration with David Musser

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki (Ayako-chan)
 * @author David Musser
 *
 */
public class SwaplessIntroSort extends Sort {
    public SwaplessIntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Intro");
        this.setRunAllSortsName("Swapless Introspective Sort");
        this.setRunSortName("Swapless Introsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    static int log2(int val) {
        return 31 - Integer.numberOfLeadingZeros(val);
    }

    static int INSERT_THRESHOLD = 32;

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (4 * i + 1 < n) {
            int max = val;
            int next = i, child = 4 * i + 1;
            for (int j = child; j < Math.min(child + 4, n); j++) {
                if (Reads.compareValues(array[p + j], max) > 0) {
                    max = array[p + j];
                    next = j;
                }
            }
            if (next == i) break;
            Writes.write(array, p + i, max, 1, true, false);
            i = next;
        }
        Writes.write(array, p + i, val, 1, true, false);
    }

    protected void heapSort(int[] array, int a, int b) {
        int n = b - a;
        for (int i = (n - 1) / 4; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }

    protected int expSearch(int[] array, int a, int b, int val) {
        int i = 1;
        while (b - i >= a && Reads.compareValueIndex(array, val, b - i, 0.25, true) < 0) i *= 2;
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

    public void insertSort(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            int current = array[i];
            int dest = expSearch(array, a, i, current);
            Highlights.markArray(2, i);
            int pos = i;
            while (pos > dest) {
                Writes.write(array, pos, array[pos - 1], 0.5, true, false);
                pos--;
            }
            if (pos < i) Writes.write(array, pos, current, 0.5, true, false);
        }
    }

    protected int medOf3(int[] array, int i0, int i1, int i2) {
        int tmp;
        if (Reads.compareIndices(array, i0, i1, 1, true) > 0) {
            tmp = i1;
            i1 = i0;
        } else tmp = i0;
        if (Reads.compareIndices(array, i1, i2, 1, true) > 0) {
            if (Reads.compareIndices(array, tmp, i2, 1, true) > 0) return tmp;
            return i2;
        }
        return i1;
    }

    public int medP3(int[] array, int a, int b, int d) {
        if (b - a == 3 || (b - a > 3 && d == 0))
            return medOf3(array, a, a + (b - a) / 2, b - 1);
        if (b - a < 3) return a + (b - a) / 2;
        int t = (b - a) / 3;
        int l = medP3(array, a, a + t, --d), c = medP3(array, a + t, b - t, d), r = medP3(array, b - t, b, d);
        // median
        return medOf3(array, l, c, r);
    }

    public int partition(int[] array, int a, int b, int pivIdx) {
        Highlights.clearMark(2);
        int hold = array[pivIdx];
        Writes.write(array, pivIdx, array[a], 1, true, false);
        int i = a, j = b - 1;
        while (i < j) {
            while (i < j && Reads.compareValueIndex(array, hold, j, 1, true) < 0) j--;
            if (i < j) {
                Writes.write(array, i++, array[j], 1, true, false);
                while (i < j && Reads.compareValueIndex(array, hold, i, 1, true) > 0) i++;
                if (i < j) Writes.write(array, j--, array[i], 1, true, false);
            }
        }
        Writes.write(array, j, hold, 1, true, false);
        return j;
    }

    void sortHelper(int[] array, int a, int b, int d) {
        while (b - a > INSERT_THRESHOLD) {
            if (d == 0) {
                Highlights.clearAllMarks();
                heapSort(array, a, b);
                return;
            }
            d--;
            int p = partition(array, a, b, medP3(array, a, b, 1));
            if (b - (p + 1) < p - a) {
                sortHelper(array, p + 1, b, d);
                b = p;
            } else {
                sortHelper(array, a, p, d);
                a = p + 1;
            }
        }
    }

    public void quickSort(int[] array, int a, int b) {
        sortHelper(array, a, b, 2 * log2(b - a));
        Highlights.clearAllMarks();
        insertSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);
    }
}
