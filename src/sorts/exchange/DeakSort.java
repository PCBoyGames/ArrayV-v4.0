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

This sort was made completely on accident.

 */

public class DeakSort extends Sort {
    public DeakSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);

        this.setSortListName("Deak");
        this.setRunAllSortsName("Deak Sort");
        this.setRunSortName("Deaksort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    private void deak(int[] array, int a, int b, int depth) {
        if (b - a < 2) return;
        if (Reads.compareIndices(array, a, b - 1, 0.1, true) > 0) Writes.swap(array, a, b - 1, 0.1, true, false);
        int m = a + (b - a) / 2;
        Writes.recordDepth(depth);
        Writes.recursion();
        deak(array, a, m, depth+1);
        Writes.recursion();
        deak(array, m, b, depth+1);
    }

    private int segmentCount(int[] array, int start, int end) {
        int count = 1;
        for (int i = start; i < end - 1; i++) {
            if (Reads.compareIndices(array, i, i + 1, 0.1, true) > 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int tries = 0, segCnt = segmentCount(array, 0, currentLength);
        while (segCnt > 2 && tries < currentLength) {
            for (int i = 0; i <= currentLength; i++) {
                deak(array, 0, i, 0);
            }
            segCnt = segmentCount(array, 0, currentLength);
            tries++;
        }
        if (tries >= currentLength || segCnt > 1) {
            // bubble sort
            boolean change = true;
            while (change) {
                change = false;
                for (int i = 1; i < currentLength; i++) {
                    if (Reads.compareIndices(array, i, i - 1, 0.1, true) < 0) {
                        Writes.swap(array, i, i - 1, 0.1, true, false);
                        change = true;
                    }
                }
            }
        }
    }
}
