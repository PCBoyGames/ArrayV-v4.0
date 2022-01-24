package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author _fluffyy
 * @author Yuri-chan
 *
 */
public final class SelectionMergeSort extends Sort {

    public SelectionMergeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Selection Merge");
        setRunAllSortsName("Selection Merge Sort");
        setRunSortName("Selection Mergesort");
        setCategory("Merge Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(false);
        setUnreasonableLimit(0);
        setBogoSort(false);
    }

    public void wrapper(int[] arr, int start, int stop) {
        if (stop - start > 2) {
            wrapper(arr, start, (stop - start) / 2 + start);
            wrapper(arr, (stop - start) / 2 + start, stop);
        }
        int last = (stop - start) / 2 + start + 1;
        int orig = last - 1;

        for (int i = start; i < stop - 1; i++) {
            if (orig == i) {
                orig++;
            }
            int min = i;
            for (int j = orig; j < last; j++) {
                if (this.Reads.compareValues(arr[min], arr[j]) == 1) {
                    min = j;
                }
            }
            if (min != i) {
                this.Writes.swap(arr, i, min, 1.0D, true, false);
            }
            if (min == last - 1 && last < stop) {
                last++;
            }
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        wrapper(array, 0, sortLength);

    }

}
