package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

+---------------------------+
| SORTING ALGORITHM SCARLET |
+---------------------------+
|    A sorting algorithm    |
|    studio by Flanlaina    |
|    (a.k.a Ayako-chan)     |
+---------------------------+

 */

/**
 * @author Flanlaina
 * @author gooflang
 *
 */
public class SweepSortIterative extends Sort {
    public SweepSortIterative(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sweep (Iterative)");
        this.setRunAllSortsName("Iterative Sweep Sort");
        this.setRunSortName("Iterative Sweepsort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int n;

    private int compSwap(int[] array, int a, int b) {
        if (b < this.n && Reads.compareIndices(array, a, b, 1, true) > 0) {
            Writes.swap(array, a, b, 1, true, false);
            return 1;
        }
        return 0;
    }

    protected int sweepPass(int[] array, int a, int b) {
        int swapCnt = 0;
        for (int g = (b - a) / 2; g > 0; g /= 2) {
            for (int s = a; s + g < b; s += 2 * g) {
                int m = s + g;
                for (int i = s; i < m; i++) swapCnt += compSwap(array, i, i + g);
                if (g > 1) swapCnt += compSwap(array, m - 1, m);
            }
        }
        return swapCnt;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int l = 1 << (32 - Integer.numberOfLeadingZeros(sortLength - 1)); // ceilPow2(currentLength)
        //for (; (l << 1) < currentLength; l <<= 1);
        n = sortLength;
        while (sweepPass(array, 0, l) != 0)
            ;
    }
}
