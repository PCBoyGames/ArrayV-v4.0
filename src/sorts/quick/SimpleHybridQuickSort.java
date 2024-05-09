package sorts.quick;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/*

Coded for ArrayV by Harumi

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Harumi
 *
 */
public class SimpleHybridQuickSort extends Sort {

    public SimpleHybridQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Simple Hybrid Quick");
        setRunAllSortsName("Simple Hybrid Quick Sort");
        setRunSortName("Simple Hybrid Quicksort");
        setCategory("Quick Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    MaxHeapSort heapSorter;
    InsertionSort insertSorter;

    private int medianOfThree(int[] array, int i0, int i1, int i2) {
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

    private int ninther(int[] array, int a, int b) {
        int s = (b - a) / 9;

        int a1 = medianOfThree(array, a, a + s, a + 2 * s);
        int m1 = medianOfThree(array, a + 3 * s, a + 4 * s, a + 5 * s);
        int b1 = medianOfThree(array, a + 6 * s, a + 7 * s, a + 8 * s);

        return medianOfThree(array, a1, m1, b1);
    }

    private int medianOfThreeNinthers(int[] array, int a, int b) {
        int s = (b - a + 2) / 3;

        int a1 = ninther(array, a, a + s);
        int m1 = ninther(array, a + s, a + 2 * s);
        int b1 = ninther(array, a + 2 * s, b);

        return medianOfThree(array, a1, m1, b1);
    }

    private int partition(int[] array, int a, int b, int val) {
        int i = a, j = b;
        while (i <= j) {
            while (Reads.compareValues(array[i], val) < 0) {
                i++;
                Highlights.markArray(1, i);
                Delays.sleep(0.5D);
            }
            while (Reads.compareValues(array[j], val) > 0) {
                j--;
                Highlights.markArray(2, j);
                Delays.sleep(0.5D);
            }

            if (i <= j) {
                if (i != j) Writes.swap(array, i, j, 1.0D, true, false);
                i++; j--;
            }

        }
        return i;
    }

    private void sort(int[] array, int a, int b, int depthLimit) {
        while (b - a > 16) {
            if (depthLimit == 0) {
                heapSorter.customHeapSort(array, a, b, 1.0);
                return;
            }
            int piv = medianOfThreeNinthers(array, a, b - 1);
            int p = partition(array, a, b - 1, array[piv]);
            depthLimit--;
            if (b - p < p - a) {
                sort(array, p, b, depthLimit);
                b = p;
            } else {
                sort(array, a, p, depthLimit);
                a = p;
            }
        }

        insertSorter.customInsertSort(array, a, b, 0.5D, false);
    }

    public void quickSort(int[] array, int a, int b) {
        insertSorter = new InsertionSort(arrayVisualizer);
        heapSorter = new MaxHeapSort(arrayVisualizer);
        sort(array, a, b, 2 * (int) (Math.log(b - a) / Math.log(2)));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        quickSort(array, 0, sortLength);

    }

}
