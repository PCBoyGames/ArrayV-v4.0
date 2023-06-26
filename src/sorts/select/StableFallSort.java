package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

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
        int stacked = 0;
        while (left <= currentLength) {
            int highestlow = 0;
            for (int right = left + 1 + stacked; right <= currentLength; right++) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.01, true) > 0) {
                    if (highestlow == 0) highestlow = right;
                    else if (Reads.compareIndices(array, highestlow - 1, right - 1, 0.01, true) < 0) highestlow = right;
                }
            }
            Highlights.clearMark(2);
            if (highestlow == 0) {
                left++;
                stacked = 0;
            } else {
                Writes.insert(array, highestlow - 1, left - 1, 0.01, true, false);
                stacked++;
            }
        }
    }
}