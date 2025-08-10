package sorts.insert;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class DoubleInsertionSort2 extends Sort {
    public DoubleInsertionSort2(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Double Insertion 2");
        this.setRunAllSortsName("Double Insertion Sort");
        this.setRunSortName("Double Insertsort");
        this.setCategory("Insertion Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void insertSort(int[] array, int a, int b, double sleep, boolean aux) {
        int n = b - a;
        int k = a + n / 2, j = k - 1 + n % 2;

        if (n % 2 == 0 && Reads.compareIndices(array, j, k, sleep, true) > 0)
            Writes.swap(array, j, k, sleep, false, false);

        for (j--, k++; k < n; j--, k++) {
            int i;
            int t;

            if (Reads.compareIndices(array, j, k, sleep, true) > 0) {
                t = array[k];
                Writes.write(array, k, array[j], sleep, false, false);

                for (i = j; Reads.compareIndexValue(array, i + 1, t, sleep, true) <= 0; i++)
                    Writes.write(array, i, array[i + 1], sleep, true, false);
                Writes.write(array, i, t, sleep, true, false);

                t = array[k];
                for (i = k; Reads.compareIndexValue(array, i - 1, t, sleep, true) >= 0; i--)
                    Writes.write(array, i, array[i - 1], sleep, true, false);
                Writes.write(array, i, t, sleep, true, false);
            } else {
                t = array[j];
                for (i = j; Reads.compareIndexValue(array, i + 1, t, sleep, true) < 0; i++)
                    Writes.write(array, i, array[i + 1], sleep, true, false);
                Writes.write(array, i, t, sleep, true, false);

                t = array[k];

                for (i = k; Reads.compareIndexValue(array, i - 1, t, sleep, true) > 0; i--)
                    Writes.write(array, i, array[i - 1], sleep, true, false);
                Writes.write(array, i, t, sleep, true, false);
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        insertSort(array, 0, sortLength, 0.015, false);
    }
}
