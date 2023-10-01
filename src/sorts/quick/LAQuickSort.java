package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import sorts.select.MaxHeapSort;

public class LAQuickSort extends Sort {
    public LAQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("LA Quick");
        this.setRunAllSortsName("thatsOven's Logarithmic Average QuickSort");
        this.setRunSortName("thatsOven's Logarithmic Average QuickSort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    private MaxHeapSort heapSorter;

    public void insertionSort(int[] arr, int a, int b) {
        for (int i = a; i < b; i++) {
            int key = arr[i];
            int j = i-1;

            while (j >= a && Reads.compareValues(key, arr[j]) < 0) {
                Writes.write(arr, j+1, arr[j], 0.5, true, false);
                j--;
            }
            Writes.write(arr, j+1, key, 0.5, true, false);
        }
    }

    public int partition(int[] array, int a, int b, int val) {
        int i = a;
        int j = b - 1;
        while (Reads.compareValues(i, j) <= 0) {
            while (Reads.compareValues(array[i], val) < 0) {
                i++;
            }
            while (Reads.compareValues(array[j], val) > 0) {
                j--;
            }
            if (i <= j) {
                Writes.swap(array, i++, j--, 1, true, false);
            }
        }
        Highlights.clearAllMarks();
        return i;
    }

    public int log2(int N) {
        int result = (int)(Math.log(N) / Math.log(2));
        return result;
    }

    public int getpivot(int[] arr, int low, int high) {
        int sum = 0;
        int counter = 0;
        int qta = this.log2(high-low);
        if (2 > qta) {
            qta = 2;
        }
        for (int i = low; i < high; i += ((high-low) / qta)) {
            Highlights.markArray(0, i);
            Delays.sleep(20);
            sum += arr[i];
            counter++;
        }
        Highlights.clearAllMarks();
        sum = sum / counter;
        return sum;
    }

    public void quickSort(int[] arr, int low, int high, int depthLimit, int backPivot) {
        if (high-low > 16) {
            int pivot = this.getpivot(arr, low, high);
            if (depthLimit == 0 || backPivot == pivot) {
                heapSorter.customHeapSort(arr, low, high, 1);
                return;
            }
            int pi = this.partition(arr, low, high, pivot);
            depthLimit--;
            this.quickSort(arr, low, pi, depthLimit, pivot);
            this.quickSort(arr, pi, high, depthLimit, pivot);
        } else {
            this.insertionSort(arr, low, high);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.heapSorter = new MaxHeapSort(this.arrayVisualizer);
        this.quickSort(array, 0, currentLength, 2*log2(currentLength), array[0]);
    }
}