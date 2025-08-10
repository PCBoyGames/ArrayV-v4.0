package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author funganer2
 * 
 */
public final class HyperStoogeSort extends Sort {
    public HyperStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        setSortListName("Hyper Stooge");
        setRunAllSortsName("Hyper Stooge Sort");
        setRunSortName("Hyper Stoogesort");
        setCategory("Impractical Sorts");
        setComparisonBased(true);
        setBucketSort(false);
        setRadixSort(false);
        setUnreasonablySlow(true);
        setUnreasonableLimit(32);
        setBogoSort(false);
    }

    private void hyperStooge(int[] array, int start, int end) {
        if (this.Reads.compareIndices(array, start, end, 0.001D, true) > 0) {
            this.Writes.swap(array, start, end, 0.002D, true, false);
        }
        if (end - start + 1 >= 3) {
            hyperStooge(array, start, end - 1);
            hyperStooge(array, start + 1, end);
            hyperStooge(array, start, end - 1);
        }
    }

    public void runSort(int[] array, int length, int bucketCount) {
        hyperStooge(array, 0, length - 1);
    }
}
