package sorts.concurrent;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

/------------------/
|   SORTS GALORE   |
|------------------|
|  courtesy of     |
|  meme man        |
|  (aka gooflang)  |
/------------------/

bad concurrent sort

 */

public class SweepSortRecursive extends Sort {

    int n, swaps;

    public SweepSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Sweep (Recursive)");
        this.setRunAllSortsName("Recursive Sweep Sort");
        this.setRunSortName("Recursive Sweepsort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void compSwap(int[] array, int a, int b) {
        if (b < n && Reads.compareIndices(array, a, b, 1, true) > 0) {
            Writes.swap(array, a, b, 1, true, false);
            swaps++;
        }
    }

    private void sweep(int[] array, int a, int b) {
        int m = (a+b) >> 1;
        for (int i = a; i <= m; i++)
            compSwap(array, i, i-a+m+1);
        if (b-a+1 > 2) compSwap(array, m, m+1);
    }

    public void recSweep(int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int m = (a+b) >> 1;
        sweep(array, a, b);
        recSweep(array, a, m, d);
        recSweep(array, m+1, b, d);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int l = 1;
        for (; (l << 1) < currentLength; l <<= 1);
        n = currentLength;
        currentLength = l << 1;
        do {
            swaps = 0;
            recSweep(array, 0, currentLength-1, 0);
        } while (swaps != 0);
    }
}
