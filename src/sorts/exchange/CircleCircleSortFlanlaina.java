package sorts.exchange;

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
public class CircleCircleSortFlanlaina extends Sort {
    public CircleCircleSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Circle Circle (Flanlaina)");
        this.setRunAllSortsName("Flanlaina's Circle Circle Sort");
        this.setRunSortName("Flanlaina's Circle Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    int n;

    private boolean compSwap(int[] array, int a, int b) { // returns whether a swap was performed or not
        if (b < this.n && Reads.compareIndices(array, a, b, 1, true) > 0) {
            Writes.swap(array, a, b, 1, true, false);
            return true;
        }
        return false;
    }

    public boolean circle(int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return false;
        int l = a, h = b, m = (a + b) >>> 1;
        boolean anySwaps = false;
        while (l < h) anySwaps |= compSwap(array, l++, h--);
        Writes.recursion();
        anySwaps |= circle(array, a, m, d);
        Writes.recursion();
        anySwaps |= circle(array, m+1, b, d);
        return anySwaps;
    }

    public boolean circleCircle(int[] array, int a, int b, int d) {
        Writes.recordDepth(d++);
        if (a >= b) return false;
        int l = a, h = b, m = (a + b) >>> 1;
        boolean anySwaps = false;
        while (l < h) anySwaps |= circle(array, l++, h--, d);
        Writes.recursion();
        anySwaps |= circleCircle(array, a, m, d);
        Writes.recursion();
        anySwaps |= circleCircle(array, m+1, b, d);
        return anySwaps;
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        int l = 1 << (32 - Integer.numberOfLeadingZeros(sortLength - 1)); // ceilPow2(currentLength)
        n = sortLength;
        while (circleCircle(array, 0, l - 1, 0))
            ;
    }
}
