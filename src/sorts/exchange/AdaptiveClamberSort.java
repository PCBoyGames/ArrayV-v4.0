package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class AdaptiveClamberSort extends Sort {
    public AdaptiveClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Adaptive Clamber");
        this.setRunAllSortsName("Adaptive Clamber Sort");
        this.setRunSortName("Adaptive Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 0;
        int right = 1;
        while (right < currentLength) {
            Highlights.markArray(1, right - 1);
            Highlights.markArray(2, right);
            Delays.sleep(1);
            if (Reads.compareValues(array[right - 1], array[right]) > 0) {
                left = 0;
                while (left < right) {
                    Highlights.markArray(1, left);
                    Highlights.markArray(2, right);
                    Delays.sleep(0.1);
                    if (Reads.compareValues(array[left], array[right]) > 0) {
                        while (left < right) {
                            Writes.swap(array, left, right, 0.2, true, false);
                            left++;
                        }
                    }
                    left++;
                }
            }
            right++;
        }
    }
}