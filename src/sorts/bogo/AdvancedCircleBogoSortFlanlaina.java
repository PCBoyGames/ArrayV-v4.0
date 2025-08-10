package sorts.bogo;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

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
public class AdvancedCircleBogoSortFlanlaina extends BogoSorting {
    public AdvancedCircleBogoSortFlanlaina(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Advanced Circle Bogo (Flanlaina)");
        this.setRunAllSortsName("Flanlaina's Advanced Circle Bogo Sort");
        this.setRunSortName("Flanlaina's Advanced Circle Bogosort");
        this.setCategory("Bogo Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected boolean circlePass(int[] array, int a, int n, int b) {
        boolean anySwaps = false;
        int lg = 31 - Integer.numberOfLeadingZeros(n);
        int g = 1 << (randInt(0, lg));
        for (int s = a; s + g < b; s += 2 * g) {
            int i = s, j = s + 2 * g - 1;
            while (i < j) {
                if (j < b && Reads.compareIndices(array, i, j, delay, true) > 0) {
                    Writes.swap(array, i, j, delay, true, false);
                    anySwaps = true;
                }
                i++;
                j--;
            }
        }
        return anySwaps;
    }

    public void sort(int[] array, int a, int b) {
        int sortLength = b - a;
        if (sortLength < 2) return;
        int n = 1 << (32 - Integer.numberOfLeadingZeros(sortLength - 1));
        while (!isRangeSorted(array, a, b, false, true)) {
            circlePass(array, a, n, b);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);
    }
}
