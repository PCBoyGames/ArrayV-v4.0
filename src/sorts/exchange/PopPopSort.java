package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- FUNGAMER2'S SCRATCH VISUAL -
------------------------------

*/
public class PopPopSort extends Sort {
    public PopPopSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Pop Pop");
        this.setRunAllSortsName("Pop Pop Sort");
        this.setRunSortName("Pop Pop Sort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected void bubble(int[] array, int start, int end, int dir) {
        int lastswap = end - 1;
        while (lastswap >= start + 1) {
            int j = start;
            for (int i = start; i <= lastswap; i++) if (Reads.compareIndices(array, i - 1, i, 0.1, true) == dir) Writes.swap(array, i - 1, j = i, 0.1, true, false);
            lastswap = j;
        }
    }

    protected void pop(int[] array, int start, int end, int dir) {
        bubble(array, start, start + (int) Math.floor((end - start) / 4), 0 - dir);
        bubble(array, start + (int) Math.floor((end - start) / 4) + 1, (int) Math.floor((start + end) / 2), dir);
        bubble(array, (int) Math.floor((start + end) / 2) + 1, start + (int) Math.floor(((end - start) * 3) / 4), 0 - dir);
        bubble(array, start + (int) Math.floor(((end - start) * 3) / 4) + 1, end, dir);
        bubble(array, start, (int) Math.floor((start + end) / 2), 0 - dir);
        bubble(array, (int) Math.floor((start + end) / 2) + 1, end, dir);
        bubble(array, start, end, dir);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        pop(array, 1, (int) Math.floor((currentLength + 1) / 4), -1);
        pop(array, (int) Math.floor((currentLength + 1) / 4) + 1, (int) Math.floor((currentLength + 1) / 2), 1);
        pop(array, (int) Math.floor((currentLength + 1) / 2) + 1, (int) Math.floor (((currentLength + 1) * 3) / 4), -1);
        pop(array, (int) Math.floor(((currentLength + 1) * 3) / 4) + 1, currentLength, 1);
        pop(array, 1, (int) Math.floor((currentLength + 1) / 2), -1);
        pop(array, (int) Math.floor((currentLength + 1) / 2) + 1, currentLength, 1);
        pop(array, 1, currentLength, 1);
    }
}