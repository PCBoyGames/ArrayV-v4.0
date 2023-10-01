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

NO! NOT AGAIN!!!

 */

public class HyperSlowSort extends Sort {
    public HyperSlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Hyper Slow");
        this.setRunAllSortsName("Hyper Slow Sort");
        this.setRunSortName("Hyper Slowsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(16);
        this.setBogoSort(false);
    }

    public void hyperSlow(int[] array, int a, int b, int d) {
        if (a >= b) return;
        Writes.recordDepth(d++);
        Writes.recursion();
        hyperSlow(array, a, b-1, d);
        Writes.recursion();
        hyperSlow(array, a+1, b, d);
        if (Reads.compareValues(array[a], array[b]) > 0) Writes.swap(array, a, b, 0, true, false);
        Writes.recursion();
        hyperSlow(array, a, b-1, d);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        hyperSlow(array, 0, currentLength-1, 0);
    }
}
