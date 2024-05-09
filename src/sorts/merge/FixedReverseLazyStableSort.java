package sorts.merge;

import main.ArrayVisualizer;
import sorts.templates.Sort;
import utils.IndexedRotations;

/*

Coded for ArrayV by Haruki
extending code by Gaming32

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Haruki
 * @author Gaming32
 *
 */
public class FixedReverseLazyStableSort extends Sort {

    public FixedReverseLazyStableSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        
        this.setSortListName("Fixed Reverse Lazy Stable");
        this.setRunAllSortsName("Fixed Reverse Lazy Stable Sort");
        this.setRunSortName("Fixed Reverse Lazy Stable Sort");
        this.setCategory("Merge Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void rotate(int[] array, int start, int mid, int end) {
        IndexedRotations.holyGriesMills(array, start, mid, end, 0.5, true, false);
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

    public void inPlaceMerge(int[] array, int start, int mid, int end) {
        while (start < mid && mid < end) {
            start = minExpSearch(array, start, mid, array[mid], false);
            if (start == mid) break;
            int i = binSearch(array, mid, end, array[start], true);
            rotate(array, start, mid, i);
            int t = i - mid;
            mid = i;
            start += t + 1;
        }
    }

    /**
     * Sorts the range {@code [a, b)} of {@code array} using Reverse Lazy Stable
     * Sort.
     * 
     * @param array the array
     * @param a     the start of the range, inclusive
     * @param b     the end of the range, exclusive
     */
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
