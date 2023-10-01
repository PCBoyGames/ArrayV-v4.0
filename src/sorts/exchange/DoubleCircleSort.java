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

public class DoubleCircleSort extends Sort {
    public DoubleCircleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Double Circle");
        this.setRunAllSortsName("Double Circle Sort");
        this.setRunSortName("Double Circlesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public int dCirclePass(int[] array, int a, int b, int d, int swaps, boolean dir) {
        if (dir) {
            if (a >= b) return swaps;
            Writes.recordDepth(d++);
            int l = a, r = b;
            while (l < r) {
                if (Reads.compareIndices(array, l, r, 0.5, true) > 0) {
                    Writes.swap(array, l, r, 0.5, true, false);
                    swaps++;
                }
                l++; r--;
            }
            int m = (b - a) / 2;
            Writes.recursion();
            swaps = dCirclePass(array, a, a+m, d, swaps, !dir);
            Writes.recursion();
            swaps = dCirclePass(array, b-m, b, d, swaps, !dir);
            return swaps;
        } else {
            if (a >= b) return swaps;
            int m = (b - a) / 2;
            Writes.recursion();
            swaps = dCirclePass(array, a, a+m, d, swaps, !dir);
            Writes.recursion();
            swaps = dCirclePass(array, b-m, b, d, swaps, !dir);
            int l = a, r = b;
            while (l < r) {
                if (Reads.compareIndices(array, l, r, 0.5, true) > 0) {
                    Writes.swap(array, l, r, 0.5, true, false);
                    swaps++;
                }
                l++; r--;
            }
            return swaps;
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int swaps = 0;
        do {
            swaps = dCirclePass(array, 0, currentLength-1, 0, 0, true);
        } while (swaps != 0);
    }
}
