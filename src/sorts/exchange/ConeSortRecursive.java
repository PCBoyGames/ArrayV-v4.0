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

pseudo-parallel circlesort

 */

public class ConeSortRecursive extends Sort {
    public ConeSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cone (Recursive)");
        this.setRunAllSortsName("Recursive Cone Sort");
        this.setRunSortName("Conesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int conePass(int[] array, int a, int b, int c, int d, int swaps) {
        if (a >= b || a+c >= b-c) return swaps;
        Writes.recordDepth(d++);
        if (Reads.compareIndices(array, a+c, b-c, 0.5, true) > 0) {
            Writes.swap(array, a+c, b-c, 0.5, true, false);
            swaps++;
        }
        int m = (b - a) / 2;
        Writes.recursion();
        swaps = conePass(array, a, a+m, c, d, swaps);
        Writes.recursion();
        swaps = conePass(array, b-m, b, c, d, swaps);
        return swaps;
    }

    public int cone(int[] array, int a, int b, int swaps) {
        if (a >= b) return swaps;
        for (int i = 0; i < (b - a + 1) / 2; i++) swaps = conePass(array, a, b, i, 0, swaps);
        return swaps;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int swaps;
        do {
            swaps = cone(array, 0, currentLength-1, 0);
        } while (swaps != 0);
    }
}
