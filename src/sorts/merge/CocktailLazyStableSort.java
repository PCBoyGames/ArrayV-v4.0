package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Haruki

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki
 *
 */
public class CocktailLazyStableSort extends Sort {
    public CocktailLazyStableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Cocktail Lazy Stable");
        this.setRunAllSortsName("Cocktail Lazy Stable Sort");
        this.setRunSortName("Cocktail Lazy Stable Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void rotate(int[] array, int a, int m, int b) {
        IndexedRotations.cycleReverse(array, a, m, b, 1, true, false);
    }

    protected int binSearch(int[] array, int a, int b, int val, boolean left) {
        while (a < b) {
            int m = a + (b - a) / 2;
            Highlights.markArray(2, m);
            Delays.sleep(0.25);
            int c = Reads.compareValues(val, array[m]);
            if (c < 0 || (left && c == 0)) b = m;
            else a = m + 1;
        }
        return a;
    }

    protected int minExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) > 0) i *= 2;
        else while (a - 1 + i < b && Reads.compareValues(val, array[a - 1 + i]) >= 0) i *= 2;
        return binSearch(array, a + i / 2, Math.min(b, a - 1 + i), val, left);
    }

    protected int maxExpSearch(int[] array, int a, int b, int val, boolean left) {
        int i = 1;
        if (left) while (b - i >= a && Reads.compareValues(val, array[b - i]) <= 0) i *= 2;
        else while (b - i >= a && Reads.compareValues(val, array[b - i]) < 0) i *= 2;
        return binSearch(array, Math.max(a, b - i + 1), b - i / 2, val, left);
    }

    public void inPlaceMerge(int[] array, int a, int m, int b) {
        while (a < m && m < b) {
            // forwards in-place merge
            a = minExpSearch(array, a, m, array[m], false);
            if (a == m) break;
            int i = minExpSearch(array, m, b, array[a], true);
            rotate(array, a, m, i);
            int t = i - m;
            m = i;
            a += t + 1;
            if (m == b) break;

            // backwards in-place merge
            b = maxExpSearch(array, m, b, array[m - 1], true);
            if (m == b) break;
            i = maxExpSearch(array, a, m, array[b - 1], false);
            rotate(array, i, m, b);
            t = m - i;
            m = i;
            b -= t + 1;
        }
    }

    public void lazyStableSort(int[] array, int a, int b) {
        for (int j = 1; j < b - a; j *= 2)
            for (int i = a; i + j < b; i += 2 * j)
                inPlaceMerge(array, i, i + j, Math.min(i + 2 * j, b));
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        lazyStableSort(array, 0, sortLength);
    }
}
