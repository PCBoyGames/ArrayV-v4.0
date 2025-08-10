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

this is different from apollyon sort i promise

 */

public class ChainSort extends Sort {

    int n;

    public ChainSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Chain");
        this.setRunAllSortsName("Chain Sort");
        this.setRunSortName("Chainsort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void compSwap(int[] array, int a, int b) {
        if (b < n && Reads.compareIndices(array, a, b, 1, true) > 0) Writes.swap(array, a, b, 1, true, false);
    }

    private void halver(int[] array, int a, int b) {
        while (a < b) compSwap(array, a++, b--);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int l = 1;
        for (; (l << 1) < currentLength; l <<= 1);
        n = currentLength;
        currentLength = l << 1;
        for (int i = 2; i <= currentLength; i <<= 1) {
            for (int j = i; j >= (i >> 1); j >>= 1)
                for (int k = 0; k < n; k += j)
                    halver(array, k, k+j-1);
            for (int j = i >> 1; j > 2; j >>= 1)
                for (int k = j >> 1; k <= j; k <<= 1)
                    for (int m = 0; m < n; m += k)
                        halver(array, m, m+k-1);
        }
    }
}
