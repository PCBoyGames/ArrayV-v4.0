package sorts.quick;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class LRQuickSort extends Sort {
    public LRQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Left/Right Quick");
        this.setRunAllSortsName("Quick Sort, Left/Right Pointers");
        this.setRunSortName("Left/Right Quicksort");
        this.setCategory("Quick Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    // Thanks to Timo Bingmann for providing a good reference for Quick Sort w/ LR pointers.
    public void quickSort(int[] a, int p, int r, int d) {
        Writes.recordDepth(d);
        int pivot = p + (r - p + 1) / 2;
        int x = a[pivot];

        int i = p;
        int j = r;

        Highlights.markArray(3, pivot);

        while (i <= j) {
            while (Reads.compareValues(a[i], x) == -1) {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5);
            }
            while (Reads.compareValues(a[j], x) == 1) {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5);
            }

            if (i <= j) {
                // Follow the pivot and highlight it.
                if (i == pivot) {
                    Highlights.markArray(3, j);
                }
                if (j == pivot) {
                    Highlights.markArray(3, i);
                }

                Writes.swap(a, i, j, 1, true, false);

                i++;
                j--;
            }
        }

        if (p < j) {
            Writes.recursion();
            this.quickSort(a, p, j, d + 1);
        }
        if (i < r) {
            Writes.recursion();
            this.quickSort(a, i, r, d + 1);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        this.quickSort(array, 0, currentLength - 1, 0);
    }
}