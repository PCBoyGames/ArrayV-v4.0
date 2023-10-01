package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class HyperNaoanSort extends Sort {

    int h;

    public HyperNaoanSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Hyper Naoan");
        this.setRunAllSortsName("Hyper Naoan Sort");
        this.setRunSortName("Hyper Naoan Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(9);
        this.setBogoSort(false);
    }

    protected void hyperReversal(int O, int[] array, int i, int j, int depth) {
        if (O == 0) {
            for (int x = j; x > i; x--) {
                for (int y = i; y < x; y++) {
                    Writes.reversal(array, i, y, 0.01, true, false);
                    Writes.reversal(array, i + 1, y + 1, 0.01, true, false);
                    Writes.reversal(array, i, y + 1, 0.01, true, false);
                    Writes.reversal(array, i, y - 1, 0.01, true, false);
                }
            }
        } else {
            Writes.recordDepth(depth++);
            for (int x = j; x > i; x--) {
                for (int y = i; y < x; y++) {
                    Writes.recursion();
                    hyperReversal(O-1, array, i, y, depth);
                    Writes.recursion();
                    hyperReversal(O-1, array, i + 1, y + 1, depth);
                    Writes.recursion();
                    hyperReversal(O-1, array, i, y + 1, depth);
                    Writes.recursion();
                    hyperReversal(O-1, array, i, y - 1, depth);
                }
            }
        }
    }

    protected void msort(int[] array, int s, int e, int depth) {
        Writes.recordDepth(depth);
        if (e - 1 <= s) return;
        int m = (s + e) / 2;
        Writes.recursion();
        msort(array, s, m, depth + 1);
        Writes.recursion();
        msort(array, m, e, depth + 1);
        if (s >= m || m >= e) return;
        for (int i = s; i < m; i++) for (int j = 0; j < m - i; j++) if (Reads.compareIndices(array, i + j, m + j, 0.1, true) > 0) hyperReversal(h, array, i + j, m + j, 0);
    }

    protected boolean isSorted(int[] array, int length) {
        for (int i = 1; i < length; i++) if (Reads.compareIndices(array, i, i - 1, 0.1, true) < 0) return false;
        return true;
    }

    public void runSort(int[] array, int currentLength, int bucketCount) {
        h = currentLength;
        do msort(array, 0, currentLength, 0); while (!isSorted(array, currentLength));
    }
}