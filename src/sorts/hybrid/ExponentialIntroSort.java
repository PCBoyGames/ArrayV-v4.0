package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Ayako-chan
 * @author David Musser
 *
 */
public final class ExponentialIntroSort extends Sort {

    public ExponentialIntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Exponential Intro");
        this.setRunAllSortsName("Exponential Introspective Sort");
        this.setRunSortName("Exponential Introsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    int middle;
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
        if(Reads.compareValues(arr[right], arr[left]) < 0) {
            Writes.swap(arr, left, right, 1, true, false); 
        }
        if(Reads.compareValues(arr[mid], arr[left]) < 0) {
            Writes.swap(arr, mid, left, 1, true, false);
        }
        if(Reads.compareValues(arr[right], arr[mid]) < 0) {
            Writes.swap(arr, right, mid, 1, true, false);
        }
        middle = mid;
        Highlights.markArray(3, mid);
        return arr[mid];
    }
    
    int partition(int[] a, int lo, int hi, int x) {
        int i = lo, j = hi;
        while (true) {
            while (Reads.compareValues(a[i], x) < 0) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                i++;
            }
            j--;
            while (Reads.compareValues(x, a[j]) < 0) {
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
                j--;
            }
            if(!(i < j)) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                return i;
            }
            // Follow the pivot and highlight it.
            if(i == middle) Highlights.markArray(3, j);
            if(j == middle) Highlights.markArray(3, i);

            Writes.swap(a, i, j, 1, true, false);
            i++;
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
            int p = partition(a, lo, hi, medianof3(a, lo, lo + ((hi - lo) / 2), hi - 1));
            if (hi - p < p - lo) {
                introsortLoop(a, p, hi, depthLimit);
                hi = p;
            } else {
                introsortLoop(a, lo, p, depthLimit);
                lo = p;
            }
        }
    }
    
    public void quickSort(int[] array, int a, int b) {
        introsortLoop(array, a, b, 2 * log2(b - a));
        Highlights.clearAllMarks();
        insertSort(array, a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
