package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class OptimizedSwaplessClamberSort extends Sort {

    public OptimizedSwaplessClamberSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Swapless Clamber");
        this.setRunAllSortsName("Optimized Swapless Clamber Sort");
        this.setRunSortName("Optimized Swapless Clambersort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void clamber(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++) {
            if (Reads.compareIndices(array, i - 1, i, 0.125, true) <= 0) continue;
            int t = array[i];
            for (int j = a; j < i; j++) {
                if (Reads.compareValueIndex(array, t, j, 0.125, true) < 0) {
                    int t2 = array[j];
                    Writes.write(array, j, t, 0.25, true, false);
                    t = t2;
                }
            }
            Writes.write(array, i, t, 0.25, true, false);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        clamber(array, 0, sortLength);

    }

}
