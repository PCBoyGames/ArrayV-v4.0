package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class UnoptimizedFateSort extends Sort {
    public UnoptimizedFateSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Unoptimized Fate");
        this.setRunAllSortsName("Unoptimized Fate Sort");
        this.setRunSortName("Unoptimized Fatesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int right = 2;
        int highestlow = 0;
        boolean anyswaps = true;
        boolean lastswaps = false;
        while (anyswaps) {
            right = left + 1;
            highestlow = 0;
            while (right <= currentLength) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.125);
                if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                    if (highestlow == 0) highestlow = right;
                    else {
                        Highlights.markArray(1, highestlow - 1);
                        Highlights.markArray(2, right - 1);
                        Highlights.markArray(3, left - 1);
                        Delays.sleep(0.125);
                        if (Reads.compareValues(array[highestlow - 1], array[right - 1]) < 0) highestlow = right;
                        Highlights.clearMark(3);
                    }
                }
                right++;
            }
            if (highestlow != 0) {
                Writes.swap(array, left - 1, highestlow - 1, 0.125, true, false);
                lastswaps = true;
            }
            left++;
            if (left > currentLength) {
                left = 1;
                anyswaps = lastswaps;
                lastswaps = false;
            }
        }
    }
}