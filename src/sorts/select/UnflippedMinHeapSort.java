package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

final public class UnflippedMinHeapSort extends Sort {
    public UnflippedMinHeapSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Unflipped Min Heap");
        this.setRunAllSortsName("thatsOven's Unflipped Min Heap Sort");
        this.setRunSortName("thatsOven's Unflipped Min Heap Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void siftDown(int[] array, int root, int dist, int start, double sleep, boolean isMax) {
        int compareVal = 0;

        if (isMax) compareVal = -1;
        else compareVal = 1;

        while (root <= dist / 2) {
            int leaf = 2 * root;
            if (leaf < dist && Reads.compareValues(array[start + leaf - 1], array[start + leaf]) == compareVal) {
                leaf++;
            }
            Highlights.markArray(1, start + root - 1);
            Highlights.markArray(2, start + leaf - 1);
            Delays.sleep(sleep);
            if (Reads.compareValues(array[start + root - 1], array[start + leaf - 1]) == compareVal) {
                Writes.swap(array, start + root - 1, start + leaf - 1, 0, true, false);
                root = leaf;
            }
            else break;
        }
    }

    protected void heapify(int[] arr, int low, int high, double sleep, boolean isMax) {
        int length = high - low;
        for (int i = length / 2; i >= 1; i--) {
            siftDown(arr, i, length, low, sleep, isMax);
        }
    }

    public void insert(int[] arr, int fromindex, int toindex) {
        for (int i = fromindex; i < toindex; i++) {
            Writes.swap(arr, i, i+1, 1, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.heapify(array, 0, currentLength, 1, false);
        for (int i = currentLength; i > 1; i--) {
            Writes.swap(array, 0, i - 1, 1, true, false);
            this.insert(array, i - 1, currentLength-1);
            this.siftDown(array, 1, i - 1, 0, 1, false);
        }
        this.insert(array, 0, currentLength-1);
    }
}