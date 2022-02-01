package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.PoplarHeapSort;
import sorts.templates.Sort;

/**
 * @author Yuri-chan
 *
 */
public final class MOMIntroSort extends Sort {

    public MOMIntroSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("MOM Intro");
        this.setRunAllSortsName("Median-of-Medians Introspective Sort");
        this.setRunSortName("Median-of-Medians Introsort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private PoplarHeapSort heapSorter;
    private InsertionSort insSort;
    private int middle;
    private int sizeThreshold = 16;

    private static int floorLogBaseTwo(int a) {
        return (int) (Math.floor(Math.log(a) / Math.log(2)));
    }

    private int medianof3(int[] arr, int left, int mid, int right) {
        if(Reads.compareValues(arr[right], arr[left]) == -1) {
            Writes.swap(arr, left, right, 1, true, false);
        }
        if(Reads.compareValues(arr[mid], arr[left]) == -1) {
            Writes.swap(arr, mid, left, 1, true, false);
        }
        if(Reads.compareValues(arr[right], arr[mid]) == -1) {
            Writes.swap(arr, right, mid, 1, true, false);
        }
        this.middle = mid;
        Highlights.markArray(3, mid);
        return arr[mid];
    }

    // lite version
    private int medianOfMedians(int[] array, int a, int b, int s) {
        int end = b, start = a, i, j;
        boolean ad = true;

        while (end - start > 1) {
            j = start;
            Highlights.markArray(2, j);
            for (i = start; i + 2 * s <= end; i += s) {
                this.insSort.customInsertSort(array, i, i + s, 0.25, false);
                Writes.swap(array, j++, i + s / 2, 1, false, false);
                Highlights.markArray(2, j);
            }
            if (i < end) {
                this.insSort.customInsertSort(array, i, end, 0.25, false);
                Writes.swap(array, j++, i + (end - (ad ? 1 : 0) - i) / 2, 1, false, false);
                Highlights.markArray(2, j);
                if ((end - i) % 2 == 0)
                    ad = !ad;
            }
            end = j;
        }
        this.middle = a;
        Highlights.markArray(3, a);
        return array[a];
    }

    private int partition(int[] a, int lo, int hi, int x) {
        int i = lo, j = hi;
        while (true) {
            while (Reads.compareValues(a[i], x) == -1) {
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
                i++;
            }
            j--;
            while (Reads.compareValues(x, a[j]) == -1) {
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
            if(i == this.middle) {
                Highlights.markArray(3, j);
            }
            if(j == this.middle) {
                Highlights.markArray(3, i);
            }
            Writes.swap(a, i, j, 1, true, false);
            i++;
        }
    }

    private void introsortLoop(int[] a, int lo, int hi, int depthLimit) {
        while(hi - lo > this.sizeThreshold) {
            if(depthLimit == 0) {
                Highlights.clearAllMarks();
                this.heapSorter.heapSort(a, lo, hi);
                return;
            }
            int piv = medianof3(a, lo, lo + ((hi - lo) / 2), hi - 1);
            int p = partition(a, lo, hi, piv);
            int l = p - lo;
            int r = hi - p;
            if((l == 0 || r == 0) || (l / r >= 16 || r / l >= 16)) {
                piv = medianOfMedians(a, lo, hi, 5);
                p = partition(a, lo, hi, piv);
            }
            depthLimit--;
            introsortLoop(a, lo, p, depthLimit);
            lo = p;
        }
    }

    public void customSort(int[] array, int a, int b) {
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        introsortLoop(array, a, b, 2 * floorLogBaseTwo(b - a));
        Highlights.clearAllMarks();
        this.insSort.customInsertSort(array, a, b, 0.5, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        heapSorter = new PoplarHeapSort(arrayVisualizer);
        insSort = new InsertionSort(arrayVisualizer);
        introsortLoop(array, 0, sortLength, 2 * floorLogBaseTwo(sortLength));
        Highlights.clearAllMarks();
        this.insSort.customInsertSort(array, 0, sortLength, 0.5, false);
    }

}
