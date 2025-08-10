package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/**
 * @author Lancewer
 * @author Flanlaina
 *
 */
public class OnionStoogeSort extends Sort {

    public OnionStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Onion Stooge");
        this.setRunAllSortsName("Onion Stooge Sort");
        this.setRunSortName("Onion Stoogesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    private void stoogeSort(int[] A, int i, int j) {
        if (Reads.compareIndices(A, i, j, 0.025, true) == 1) {
            Writes.swap(A, i, j, 0.05, true, false);
        }

        if (j - i + 1 >= 3) {
            int t = (j - i + 1) / 3;

            Highlights.markArray(3, j - t);
            Highlights.markArray(4, i + t);

            this.stoogeSort(A, i, j-t);
            this.stoogeSort(A, i+t, j);
            this.stoogeSort(A, i, j-t);
        }
    }

    public void onionStooge(int[] array, int start, int length) {
        for (int ticker = 1; ticker < length; ticker++) {
            stoogeSort(array, start, start + ticker);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        onionStooge(array, 0, sortLength);

    }

}
