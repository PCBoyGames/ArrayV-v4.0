package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public class SwaplessOptimizedReverseSandpaperSort extends Sort {

    public SwaplessOptimizedReverseSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Swapless Optimized Reverse Sandpaper");
        this.setRunAllSortsName("Swapless Optimized Reverse Sandpaper Sort");
        this.setRunSortName("Swapless Optimized Reverse Sandpapersort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void sort(int[] array, int a, int b) {
        boolean anyenter = true;
        int start = a + 1;
        int leftend = b - 1;
        int end = b - 1;
        int lastleft = a;
        int highestswap;
        while (anyenter) {
            anyenter = false;
            boolean startfound = false;
            highestswap = 0;
            for (int i = Math.max(start - 1, a); i < leftend; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.5, true) > 0) {
                    if (!startfound) start = i;
                    startfound = true;
                    lastleft = i;
                    anyenter = true;
                    int t = array[i];
                    for (int j = end; j > i; j--) {
                        if (Reads.compareValueIndex(array, t, j, 0.05, true) > 0) {
                            int t2 = array[j];
                            Writes.write(array, j, t, 0.05, true, false);
                            t = t2;
                            if (j > highestswap) highestswap = j;
                        }
                    }
                    Writes.write(array, i, t, 0.05, true, false);
                }
            }
            leftend = lastleft;
            end = highestswap - 1;
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
