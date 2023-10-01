package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SinkingMergeSortParallel extends Sort {

    public SinkingMergeSortParallel(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Sinking Merge (Parallel)");
        this.setRunAllSortsName("Parallel Sinking Merge Sort");
        this.setRunSortName("Parallel Sinking Mergesort");
        this.setCategory("Hybrid Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private int[] array;

    private class MergeSort extends Thread {
        private int a,b;
        MergeSort(int a, int b) {
            this.a = a;
            this.b = b;
        }
        public void run() {
            SinkingMergeSortParallel.this.sort(a, b);
        }
    }

    public void bubbleSort(int start, int end) {
        int consecSorted = 1;
        for (int i = end - 1; i > start; i -= consecSorted) {
            consecSorted = 1;
            for (int j = start; j < i; j++) {
                if (Reads.compareIndices(array, j, j + 1, 0.125, true) > 0) {
                    Writes.swap(array, j, j + 1, 0.25, true, false);
                    consecSorted = 1;
                } else
                    consecSorted++;
            }
        }
    }

    public void sort(int a, int b) {
        if (b - a <= 16) {
            bubbleSort(a, b);
            return;
        }
        int m = a + (b - a) / 2;
        MergeSort left = new MergeSort(a, m);
        MergeSort right = new MergeSort(m, b);
        left.start();
        right.start();

        try {
            left.join();
            right.join();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        bubbleSort(a, b);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        this.array = array;
        sort(0, sortLength);

    }

}
