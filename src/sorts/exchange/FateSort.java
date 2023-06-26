package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class FateSort extends Sort {
    public FateSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fate");
        this.setRunAllSortsName("Fate Sort");
        this.setRunSortName("Fatesort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int left = 1;
        int bound = currentLength;
        int firstswap = 0;
        int highestswap = currentLength;
        while (bound > 1) {
            int highestlow = 0;
            for (int right = left + 1; right <= bound; right++) {
                if (Reads.compareIndices(array, left - 1, right - 1, 0.125, true) > 0) {
                    if (highestlow == 0) highestlow = right;
                    else {
                        Highlights.markArray(3, left - 1);
                        if (Reads.compareIndices(array, highestlow - 1, right - 1, 0.125, true) < 0) highestlow = right;
                        Highlights.clearMark(3);
                    }
                }
            }
            if (highestlow > highestswap) highestswap = highestlow;
            if (highestlow != 0) {
                if (firstswap == 0) firstswap = left;
                Writes.swap(array, left - 1, highestlow - 1, 0.125, true, false);
            }
            left++;
            if (left > bound) {
                left = firstswap;
                bound = highestswap - 1;
                firstswap = 0;
                highestswap = 0;
            }
        }
    }
}