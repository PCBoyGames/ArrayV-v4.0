package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with aphitorite and _fluffyy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author aphitorite
 * @author _fluffyy
 *
 */
public class NaturalSelectionMergeSort extends Sort {

    public NaturalSelectionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Natural Selection Merge");
        setRunAllSortsName("Natural Selection Merge Sort");
        setRunSortName("Natural Selection Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    protected void merge(int[] arr, int start, int mid, int stop) {
        if (Reads.compareIndices(arr, mid - 1, mid, 0.0, true) <= 0)
            return;
        int last = mid + 1;
        int orig = mid;

        for (int i = start; i < stop - 1; i++) {
            if (orig == i) {
                orig++;
            }
            int minIdx = i;
            for (int j = orig; j < last; j++) {
                if (Reads.compareIndices(arr, j, minIdx, 0.0, false) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i)
                Writes.swap(arr, i, minIdx, 1.0, true, false);
            if (minIdx == last - 1 && last < stop) {
                last++;
            }
        }
    }

    protected int findRun(int[] array, int a, int b) {
        int i = a + 1;
        if (i < b)
            if (Reads.compareIndices(array, i - 1, i++, 0.5, true) > 0) {
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) > 0)
                    i++;
                if (i - a < 4)
                    Writes.swap(array, a, i - 1, 1.0, true, false);
                else
                    Writes.reversal(array, a, i - 1, 1.0, true, false);
            } else
                while (i < b && Reads.compareIndices(array, i - 1, i, 0.5, true) <= 0)
                    i++;
        Highlights.clearMark(2);
        return i;
    }

    public void mergeSort(int[] array, int a, int b) {
        int i, j, k;
        while (true) {
            i = findRun(array, a, b);
            if (i >= b)
                break;
            j = findRun(array, i, b);
            merge(array, a, i, j);
            Highlights.clearMark(2);
            if (j >= b)
                break;
            k = j;
            while (true) {
                i = findRun(array, k, b);
                if (i >= b)
                    break;
                j = findRun(array, i, b);
                merge(array, k, i, j);
                if (j >= b)
                    break;
                k = j;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        mergeSort(array, 0, sortLength);

    }

}
