package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public final class MoreOptimizedBubbleSort extends Sort {
    public MoreOptimizedBubbleSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("More Optimized Bubble");
        this.setRunAllSortsName("More Optimized Bubble Sort");
        this.setRunSortName("More Optimized Bubblesort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int c = 1;
        int s;
        int f = 1;
        boolean a = false;
        for (int j = currentLength - 1; j > 0; j -= c) {
            if (f - 1 < 0) s = 0;
            else s = f - 1;
            a = false;
            c = 1;
            for (int i = s; i < j; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.025, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.075, true, false);
                    if (!a) f = i;
                    a = true;
                    c = 1;
                } else c++;
            }
        }
    }
}
