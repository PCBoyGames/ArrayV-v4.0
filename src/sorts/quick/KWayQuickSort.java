package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class KWayQuickSort extends Sort {

    public KWayQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("K-Way Quick");
        this.setRunAllSortsName("K-Way Quick Sort");
        this.setRunSortName("K-Way Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
        this.setQuestion("Enter the base for this sort:", 4);
    }

    protected int binarySearch(int[] array, int a, int b, int value) {
        while (a < b) {
            int m = a + ((b - a) / 2);
            Highlights.markArray(1, a);
            Highlights.markArray(3, m);
            Highlights.markArray(2, b);
            Delays.sleep(0.25);
            if (Reads.compareValues(value, array[m]) < 0) b = m;
            else a = m + 1;
        }
        Highlights.clearMark(3);
        return a;
    }

    protected void binsert(int[] array, int start, int end) {
        for (int i = start; i < end; i++) {
            if (Reads.compareValues(array[i - 1], array[i]) > 0) {
                int item = array[i];
                int left = binarySearch(array, start - 1, i - 1, item);
                Highlights.clearAllMarks();
                Highlights.markArray(2, left);
                for (int right = i; right > left; right--) Writes.write(array, right, array[right - 1], 0.25, true, false);
                Writes.write(array, left, item, 0.01, true, false);
                Highlights.clearAllMarks();
            } else {
                Highlights.markArray(1, i);
                Delays.sleep(0.25);
            }
        }
    }

    @Override
    public int validateAnswer(int answer) {
        if (answer < 1) return 1;
        return answer;
    }

    public void sort(int[] arr, int start, int stop, int buckets) {
        int len = stop - start;
        while (len <= buckets) buckets--;
        if (len > 16) {
            int root = buckets;
            int newStart = start + root;
            sort(arr, start, newStart, buckets);
            int[] pivots = new int[root];
            Writes.changeAllocAmount(pivots.length);
            for (int i = 0; i < root; i++) Writes.write(pivots, i, i + start, 0, false, true);
            for (int i = newStart; i < stop; i++) {
                int left = 0, right = root;
                while (left < right) {
                    int mid = (right - left) / 2 + left;
                    Highlights.markArray(1, pivots[mid]);
                    Highlights.markArray(2, pivots[left]);
                    if (right < root) Highlights.markArray(3, pivots[right]);
                    Delays.sleep(1);
                    if (Reads.compareIndices(arr, pivots[mid], i, 0, false) == 1) right = mid;
                    else left = mid + 1;
                    Highlights.clearAllMarks();
                }
                int pos = i;
                for (int j = root - 1; j >= left; j--) {
                    if (pivots[j] + 1 != pos) Writes.swap(arr, pivots[j] + 1, pos, 0.25, true, false);
                    Writes.swap(arr, pos = pivots[j], pivots[j] + 1, 0.25, true, false);
                    Writes.write(pivots, j, pivots[j] + 1, 0, false, true);
                }
            }
            sort(arr, start, pivots[0], buckets);
            for (int i = 1; i < root; i++) sort(arr, pivots[i - 1] + 1, pivots[i], buckets);
            sort(arr, pivots[root - 1] + 1, stop, buckets);
            Writes.changeAllocAmount(-pivots.length);
        } else {
            binsert(arr, start + 1, stop);
        }

    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, bucketCount);
    }
}
