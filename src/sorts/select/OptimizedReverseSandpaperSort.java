package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class OptimizedReverseSandpaperSort extends Sort {
    public OptimizedReverseSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Reverse Sandpaper");
        this.setRunAllSortsName("Optimized Reverse Sandpaper Sort");
        this.setRunSortName("Optimized Reverse Sandpapersort");
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
        boolean anyenter = true;
        int start = 1;
        int leftend = currentLength - 1;
        int end = currentLength - 1;
        int lastleft = 0;
        int highestswap;
        while (anyenter) {
            anyenter = false;
            boolean startfound = false;
            highestswap = 0;
            for (int i = start > 1 ? start - 1 : 0; i < leftend; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.5, true) > 0) {
                    if (!startfound) start = i;
                    startfound = true;
                    lastleft = i;
                    anyenter = true;
                    for (int j = end; j > i; j--) {
                        if (Reads.compareIndices(array, i, j, 0.05, true) > 0) {
                            Writes.swap(array, i, j, 0.05, true, false);
                            if (j > highestswap) highestswap = j;
                        }
                    }
                }
            }
            leftend = lastleft;
            end = highestswap - 1;
        }
    }
}