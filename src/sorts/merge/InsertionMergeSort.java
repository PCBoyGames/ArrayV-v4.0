package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Enver
 *
 */
public final class InsertionMergeSort extends Sort {

    public InsertionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Insertion Merge");
        setRunAllSortsName("Insertion Merge Sort");
        setRunSortName("Insertion Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    public void insertSort(int[] array, int start, int mid, int end, double sleep) {
        int pos;
        int cur;
        for (int i = mid; i < end; i++) {
            cur = array[i];
            pos = i - 1;
            while (pos >= start && Reads.compareValues(array[pos], cur) > 0) {
                Writes.write(array, pos + 1, array[pos], sleep, true, false);
                pos--;
            }
            if (pos + 1 != i) Writes.write(array, pos + 1, cur, sleep, true, false);
        }
    }

    public void sort(int[] array, int a, int b, double sleep) {
        if (b - a < 2)
            return;
        int m = a + (b - a) / 2;
        sort(array, a, m, sleep);
        sort(array, m, b, sleep);
        insertSort(array, a, m, b, sleep);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength, 0.125);

    }

}
