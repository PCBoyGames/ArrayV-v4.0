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

I'm not calling you stupid, Naoan.

 */

public class StupidNaoanSort extends Sort {
    public StupidNaoanSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Stupid Naoan");
        this.setRunAllSortsName("Stupid Naoan Sort");
        this.setRunSortName("Stupid Naoan Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    protected void w0tReversal(int[] array, int i, int j) {
        for (int x = j; x > i; x--) {
            for (int y = i; y < x; y++) {
                Writes.reversal(array, i, y, 0.01, true, false);
                Writes.reversal(array, i + 1, y + 1, 0.01, true, false);
                Writes.reversal(array, i, y + 1, 0.01, true, false);
                Writes.reversal(array, i, y - 1, 0.01, true, false);
                Writes.reversal(array, i, y - 1, 0.01, true, false);
                Writes.reversal(array, i, y + 1, 0.01, true, false);
                Writes.reversal(array, i + 1, y + 1, 0.01, true, false);
                Writes.reversal(array, i, y, 0.01, true, false);
                Writes.reversal(array, i, y, 0.01, true, false);
                Writes.reversal(array, i + 1, y + 1, 0.01, true, false);
                Writes.reversal(array, i, y + 1, 0.01, true, false);
                Writes.reversal(array, i, y - 1, 0.01, true, false);
            }
        }
    }

    protected void w0tsort(int[] array, int start, int end, int depth) {
        Writes.recordDepth(depth);
        if (end - 1 <= start) return;
        int k = (start + end) - 1; // The answer is always k.
        Writes.recursion();
        w0tsort(array, start, k, depth + 1);
        Writes.recursion();
        w0tsort(array, k, end, depth + 1);
        if (start >= k || k >= end) return;
        for (int i = start; i < k; i++) {
            for (int j = 0; j < k - i; j++) {
                if (Reads.compareIndices(array, i + j, k + j, 0.1, true) > 0) {
                    w0tReversal(array, i + j, k + j);
                }
            }
        }
    }

    protected boolean isSorted(int[] array, int length) {
        for (int i = 1; i < length; i++) {
            if (Reads.compareIndices(array, i, i - 1, 0.1, true) < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        do {
            w0tsort(array, 0, currentLength, 0);
        } while (!isSorted(array, currentLength));
    }
}
