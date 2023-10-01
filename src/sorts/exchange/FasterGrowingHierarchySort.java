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

fs

 */

public class FasterGrowingHierarchySort extends Sort {
    public FasterGrowingHierarchySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Faster-growing Hierarchy");
        this.setRunAllSortsName("Faster-growing Hierarchy Sort");
        this.setRunSortName("Faster-growing Hierarchy sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(3);
        this.setBogoSort(false);
    }

    private long fs(int a, long n, int[] array, int d) {
        Writes.recordDepth(d++);
        if (a == 0) {
            return n+1;
        } else {
            long b = n;
            Writes.recursion();
            for (long i = 0; i < fs(a-1, n, array, d); i++) {
                Writes.recursion();
                b = fs(a-1, b, array, d);
                if (Reads.compareValues(array[(a-2 < 0) ? a : a-2], array[(a-1 < 1) ? a+1 : a-1]) > 0) {
                    Writes.swap(array, (a-2 < 0) ? a : a-2, (a-1 < 1) ? a+1 : a-1, 0.01, true, false);
                }
            }
            return b;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        fs(currentLength, currentLength, array, 0);
    }
}
