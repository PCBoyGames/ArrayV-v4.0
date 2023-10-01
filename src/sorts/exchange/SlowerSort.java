package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class SlowerSort extends Sort {
    public SlowerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Slower");
        this.setRunAllSortsName("Slower Sort");
        this.setRunSortName("Slowersort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void slowerSort(int[] array, int a, int b, int d) {
        int n = b - a;
        if (n < 2) return;
        Writes.recordDepth(d++);
        if (n == 2) {
            if (Reads.compareValues(array[a], array[a+1]) > 0) Writes.swap(array, a, a+1, 0, true, false);
            return;
        }
        int m = n / 2;
        Writes.recursion();
        slowerSort(array, a, a+m, d);
        Writes.recursion();
        slowerSort(array, a+m, b, d);
        m++;
        for (int i = a + m; i <= b; i++) {
            Writes.recursion();
            slowerSort(array, i-m, i, d);
        }
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        slowerSort(array, 0, currentLength, 0);
    }
}
