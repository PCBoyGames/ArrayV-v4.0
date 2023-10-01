package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class FastNaoanSort extends Sort {

    public FastNaoanSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Fast Naoan");
        this.setRunAllSortsName("Fast Naoan Sort");
        this.setRunSortName("Fast Naoan Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void reversal(int[] array, int i, int j) {
        if (i < j) {
            if (j - i >= 3) Writes.reversal(array, i, j, 0.1, true, false);
            else Writes.swap(array, i, j, 0.1, true, false);
        }
    }

    protected void msort(int[] array, int s, int e, int depth) {
        Writes.recordDepth(depth);
        if (e - s < 2) return;
        int m = s + (e - s) / 2;
        Writes.recursion();
        msort(array, s, m, depth + 1);
        Writes.recursion();
        msort(array, m, e, depth + 1);
        if (s >= m || m >= e) return;
        for (int i = s; i < m; i++)
            for (int j = 0; j < m - i; j++)
                if (Reads.compareIndices(array, i + j, m + j, 0.1, true) > 0) reversal(array, i + j, m + j);
    }

    public boolean isSorted(int[] array, int a, int b) {
        for (int i = a + 1; i < b; i++)
            if (Reads.compareIndices(array, i, i - 1, 0.1, true) < 0) return false;
        return true;
    }

    public void sort(int[] array, int a, int b) {
        while (!isSorted(array, a, b)) msort(array, a, b, 0);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
