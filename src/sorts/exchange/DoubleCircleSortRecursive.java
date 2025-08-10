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



 */

public class DoubleCircleSortRecursive extends Sort {

    int swaps, n;

    public DoubleCircleSortRecursive(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Double Circle (Recursive)");
        this.setRunAllSortsName("Recursive Double Circle Sort");
        this.setRunSortName("Double Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void c(int[] array, int a, int b) {
        if (b < n && Reads.compareIndices(array, a, b, 0.5, true) > 0) {
            Writes.swap(array, a, b, 0.5, true, false);
            swaps++;
        }
    }

    public void dCirclePass(int[] array, int a, int b, int d, boolean mode) {
        Writes.recordDepth(d++);
        if (a >= b) return;
        int l = a, r = b, m = (a+b) >> 1;
        if (mode) while (l < r) c(array, l++, r--);
        Writes.recursion();
        dCirclePass(array, a, m, d, !mode);
        Writes.recursion();
        dCirclePass(array, m+1, b, d, !mode);
        if (!mode) while (l < r) c(array, l++, r--);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int l = 1;
        for (; (l << 1) < currentLength; l <<= 1);
        n = currentLength;
        currentLength = l << 1;
        do {
            swaps = 0;
            dCirclePass(array, 0, currentLength-1, 0, true);
        } while (swaps != 0);
    }
}
