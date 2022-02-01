package sorts.misc;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class BinaryQuasiPancakeSort extends Sort
{
    public BinaryQuasiPancakeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Binary Quasi-Pancake");
        this.setRunAllSortsName("Binary Quasi-Pancake Sort (By Nayuki & fungamer2)");
        this.setRunSortName("Binary Quasi-Pancake Sort");
        this.setCategory("Miscellaneous Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int binarySearch(final int[] array, int start, int end, final int value) {
        while (start < end) {
            final int mid = start + end >>> 1;
            this.Highlights.markArray(1, start);
            this.Highlights.markArray(2, mid);
            this.Highlights.markArray(3, end);
            if (this.Reads.compareValues(array[mid], value) <= 0) {
                start = mid + 1;
            }
            else {
                end = mid;
            }
            this.Delays.sleep(1.0);
        }
        this.Highlights.clearMark(1);
        this.Highlights.clearMark(2);
        this.Highlights.markArray(3, start);
        return start;
    }

    public void runSort(final int[] array, final int length, final int bucketCount) {
        for (int i = 1; i < length; ++i) {
            final int j = this.binarySearch(array, 0, i, array[i]);
            if (j != i) {
                this.Writes.reversal(array, j, i - 1, 0.2, true, false);
                this.Writes.reversal(array, j, i, 0.2, true, false);
            }
        }
    }
}
