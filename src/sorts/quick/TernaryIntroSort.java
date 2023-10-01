package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Ayako
 * @author David Musser
 *
 */
public class TernaryIntroSort extends Sort {

    public TernaryIntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Ternary Intro");
        this.setRunAllSortsName("Ternary Introspective Sort");
        this.setRunSortName("Ternary Introsort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int sizeThreshold = 32;

    public static int log2(int n) {
        int log = 0;
        while ((n >>= 1) != 0) ++log;
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
        for (int i = a + 1; i < b; i++)
            insertTo(array, i, expSearch(array, a, i, array[i]));
    }

    private void siftDown(int[] array, int val, int i, int p, int n) {
        while (2 * i + 1 < n) {
            int max = val;
            int next = i, child = 2 * i + 1;
            for (int j = child; j < Math.min(child + 2, n); j++) {
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
        for (int i = (n - 1) / 2; i >= 0; i--)
            this.siftDown(array, array[a + i], i, a, n);
        for (int i = n - 1; i > 0; i--) {
            Highlights.markArray(2, a + i);
            int t = array[a + i];
            Writes.write(array, a + i, array[a], 1, false, false);
            this.siftDown(array, t, 0, a, i);
        }
    }

    int medianof3(int[] arr, int left, int mid, int right) {
        if (Reads.compareValues(arr[right], arr[left]) < 0) {
            Writes.swap(arr, left, right, 1, true, false);
        }
        if (Reads.compareValues(arr[mid], arr[left]) < 0) {
            Writes.swap(arr, mid, left, 1, true, false);
        }
        if (Reads.compareValues(arr[right], arr[mid]) < 0) {
            Writes.swap(arr, right, mid, 1, true, false);
        }
        Highlights.markArray(3, mid);
        return arr[mid];
    }

    // Easy patch to avoid self-swaps.
    public void swap(int[] array, int a, int b, double pause, boolean mark, boolean aux) {
        if (a != b) Writes.swap(array, a, b, pause, mark, aux);
    }

    // partition -> [a][E < piv][i][E == piv][j][E > piv][b]
    // returns -> i and j ^
    public int[] partition(int[] array, int a, int b, int piv) {
        // determines which elements do not need to be moved
        for (; a < b; a++) {
            Highlights.markArray(1, a);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[a], piv) >= 0) break;
        }
        for (; b > a; b--) {
            Highlights.markArray(1, b - 1);
            Delays.sleep(0.25);
            if (Reads.compareValues(array[b - 1], piv) <= 0) break;
        }
        int i1 = a, i = a - 1, j = b, j1 = b;
        while (true) {
            while (++i < j) {
                int cmp = Reads.compareIndexValue(array, i, piv, 0.5, true);
                if (cmp == 0) swap(array, i1++, i, 1, true, false);
                else if (cmp > 0) break;
            }
            Highlights.clearMark(2);
            while (--j > i) {
                int cmp = Reads.compareIndexValue(array, j, piv, 0.5, true);
                if (cmp == 0) swap(array, --j1, j, 1, true, false);
                else if (cmp < 0) break;
            }
            Highlights.clearMark(2);
            if (i >= j) {
                if (i1 == b) return new int[] { a, b };
                if (j < i) j++;
                if (i1 - a > i - i1) {
                    int i2 = i;
                    i = a;
                    while (i1 < i2) swap(array, i++, i1++, 1, true, false);
                } else while (i1 > a) swap(array, --i, --i1, 1, true, false);
                if (b - j1 > j1 - j) {
                    int j2 = j;
                    j = b;
                    while (j1 > j2) swap(array, --j, --j1, 1, true, false);
                } else while (j1 < b) swap(array, j++, j1++, 1, true, false);
                return new int[] { i, j };
            }
            // The patch is not needed here, because it never swaps when i == j.
            Writes.swap(array, i, j, 1, true, false);
            Highlights.clearMark(2);
        }
    }

    void introsortLoop(int[] a, int lo, int hi, int depthLimit) {
        while (hi - lo > sizeThreshold) {
            if (depthLimit == 0) {
                Highlights.clearAllMarks();
                heapSort(a, lo, hi);
                return;
            }
            depthLimit--;
            int[] p = partition(a, lo, hi, medianof3(a, lo, lo + ((hi - lo) / 2), hi - 1));
            if (hi - p[1] < p[0] - lo) {
                introsortLoop(a, p[1], hi, depthLimit);
                hi = p[0];
            } else {
                introsortLoop(a, lo, p[0], depthLimit);
                lo = p[1];
            }
        }
    }

    public void quickSort(int[] array, int a, int b) {
        int z = 0, e = 0;
        for (int i = a; i < b - 1; i++) {
            int cmp = Reads.compareIndices(array, i, i + 1, 0.5, true);
            z += cmp > 0 ? 1 : 0;
            e += cmp == 0 ? 1 : 0;
        }
        if (z == 0) return;
        if (z + e == b - a - 1) {
            if (b - a < 4) Writes.swap(array, a, b - 1, 0.75, true, false);
            else Writes.reversal(array, a, b - 1, 0.75, true, false);
            return;
        }
        introsortLoop(array, a, b, 2 * log2(b - a));
        Highlights.clearAllMarks();
        insertSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
