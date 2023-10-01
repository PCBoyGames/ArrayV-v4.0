package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class UnoptimizedFateSort extends Sort {
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
        boolean anyswaps = true;
        boolean lastswaps = false;
        while (anyswaps) {
            int highestlow = 0;
            for (int right = left + 1; right <= currentLength; right++) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.125, true) > 0) {
                    if (highestlow == 0) highestlow = right;
                    else if (Reads.compareIndices(array, highestlow - 1, right - 1, 0.125, true) < 0) highestlow = right;
                }
            }
            if (highestlow != 0) Writes.swap(array, left - 1, highestlow - 1, 0.125, lastswaps = true, false);
            left++;
            if (left > currentLength) {
                left = 1;
                anyswaps = lastswaps;
                lastswaps = false;
            }
        }
    }
}