package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class StableFallSort extends Sort {
    public StableFallSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Falling");
        this.setRunAllSortsName("Stable Falling Sort");
        this.setRunSortName("Stable Fallsort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int right = 2;
        int highestlow = 0;
        int item = 0;
        int pull = 0;
        int stacked = 0;
        while (left <= currentLength) {
            right = left + 1 + stacked;
            highestlow = 0;
            while (right <= currentLength) {
                Highlights.markArray(1, left - 1);
                Highlights.markArray(2, right - 1);
                Delays.sleep(0.01);
                if (Reads.compareValues(array[left - 1], array[right - 1]) > 0) {
                    if (highestlow == 0) highestlow = right;
                    else {
                        Highlights.markArray(1, highestlow - 1);
                        Highlights.markArray(2, right - 1);
                        Delays.sleep(0.01);
                        if (Reads.compareValues(array[highestlow - 1], array[right - 1]) < 0) highestlow = right;
                    }
                }
                right++;
            }
            Highlights.clearMark(2);
            if (highestlow == 0) {
                left++;
                stacked = 0;
            } else {
                item = array[highestlow - 1];
                pull = highestlow;
                while (pull > left) {
                    Writes.write(array, pull - 1, array[pull - 2], 0.01, true, false);
                    pull--;
                }
                Writes.write(array, left - 1, item, 0.01, true, false);
                stacked++;
            }
        }
    }
}