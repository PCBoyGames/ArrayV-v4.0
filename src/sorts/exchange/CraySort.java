package sorts.exchange;

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

recursive cocktail shaker

 */

public class CraySort extends Sort {
    public CraySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cray");
        this.setRunAllSortsName("Cray Sort");
        this.setRunSortName("Craysort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int ray(int[] array, int a, int b, int d, int swaps, boolean dir) {
        Writes.recordDepth(d++);
        if (a >= b) return swaps;
        for (int i = dir ? a : b-1; dir ? i < b : i >= a; i += dir ? 1 : -1) {
            if (Reads.compareIndices(array, i, i+1, 1, true) > 0) {
                Writes.swap(array, i, i+1, 1, true, false);
                swaps++;
            }
        }
        int m = (a+b)/2;
        Writes.recursion();
        swaps = ray(array, a, m, d, swaps, !dir);
        Writes.recursion();
        swaps = ray(array, m+1, b, d, swaps, !dir);
        return swaps;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int swaps;
        do {
            swaps = ray(array, 0, currentLength-1, 0, 0, true);
        } while (swaps != 0);
    }
}
