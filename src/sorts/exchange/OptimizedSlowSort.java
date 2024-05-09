package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public final class OptimizedSlowSort extends Sort {
    public OptimizedSlowSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Optimized Slow");
        this.setRunAllSortsName("Optimized Slow Sort");
        this.setRunSortName("Optimized Slowsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    private void optslowsort(int[] A, int i, int j) {
        if (j <= 1) return;
        optslowsort(A, i + 1, j - 1);
        if (Reads.compareValues(A[i], A[i+1]) > 0) {
        Delays.sleep(0.025);
        Writes.swap(A, i, i + 1, 1, true, false);
        optslowsort(A, i + 1, j);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        optslowsort(array, 0, currentLength-1);
    }
}
