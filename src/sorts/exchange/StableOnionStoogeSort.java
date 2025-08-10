package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Lancewer
 * @author Flanlaina
 * @author fungamer2
 *
 */
public class StableOnionStoogeSort extends Sort {

    public StableOnionStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Onion Stooge");
        this.setRunAllSortsName("Stable Onion Stooge Sort");
        this.setRunSortName("Stable Onion Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    protected void stoogeSort(int[] array, int start, int end) {
        if (end - start + 1 == 2) {
            if (Reads.compareIndices(array, start, end, 0.025, true) == 1) {
                Writes.swap(array, start, end, 0.05, true, false);
            }
        } else if (end - start + 1 > 2) {
            int third = (end - start + 1) / 3;
            stoogeSort(array, start, end - third);
            stoogeSort(array, start + third, end);
            stoogeSort(array, start, end - third);
        }
    }

    public void onionStooge(int[] array, int start, int length) {
        for(int ticker = 1; ticker < length; ticker++) {
            stoogeSort(array, start, start + ticker);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        onionStooge(array, 0, sortLength);

    }

}
