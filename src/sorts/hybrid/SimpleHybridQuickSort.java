package sorts.hybrid;

import main.ArrayVisualizer;
import sorts.insert.InsertionSort;
import sorts.select.MaxHeapSort;
import sorts.templates.Sort;

/**
 * @author Ayako-chan
 *
 */
public final class SimpleHybridQuickSort extends Sort {

    public SimpleHybridQuickSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Simple Hybrid Quick");
        setRunAllSortsName("Simple Hybrid Quick Sort");
        setRunSortName("Simple Hybrid Quicksort");
        setCategory("Hybrid Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    MaxHeapSort heapSorter;
    InsertionSort insertSorter;

    private int medianOfThree(int[] array, int a, int m, int b) {
        if (Reads.compareValues(array[m], array[a]) > 0) {
            if (Reads.compareValues(array[m], array[b]) < 0)
                return m;

            if (Reads.compareValues(array[a], array[b]) > 0)
                return a;

            else
                return b;
        } else {
            if (Reads.compareValues(array[m], array[b]) > 0)
                return m;

            if (Reads.compareValues(array[a], array[b]) < 0)
                return a;

            else
                return b;
        }
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

            if (i <= j)
                Writes.swap(array, i++, j--, 1.0D, true, false);

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
            sort(array, p, b, depthLimit);
            b = p;
        }

        insertSorter.customInsertSort(array, a, b, 0.5D, false);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSorter = new InsertionSort(arrayVisualizer);
        heapSorter = new MaxHeapSort(arrayVisualizer);
        sort(array, 0, sortLength, 2 * (int) (Math.log(sortLength) / Math.log(2.0D)));

    }

}
