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

mediocre merge sort

 */

public class RiftSortRecursive extends Sort {
    public RiftSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Rift (Recursive)");
        this.setRunAllSortsName("Recursive Rift Sort");
        this.setRunSortName("Recursive Riftsort");
        this.setCategory("Concurrent Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void compSwap(int[] array, int a, int b, double delay, boolean mark, boolean aux) {
        if (Reads.compareIndices(array, a, b, delay/2, mark) > 0) Writes.swap(array, a, b, delay/2, mark, aux);
    }

    public void rift(int[] array, int a, int b, double delay, boolean mark, boolean aux) {
        int m = (b-a+1)/2;
        for (int i = 0; i < m; i++)
            for (int j = a+i; j < a+m; j++)
                compSwap(array, j, j+m-i, delay, mark, aux);
    }

    public void riftSorter(int[] array, int a, int b, double delay, boolean mark, boolean aux, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int m = (b-a)/2;
        Writes.recursion();
        riftSorter(array, a, a+m, delay, mark, aux, d);
        Writes.recursion();
        riftSorter(array, b-m, b, delay, mark, aux, d);
        rift(array, a, b, delay, mark, aux);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        riftSorter(array, 0, currentLength-1, 1, true, false, 0);
    }
}
