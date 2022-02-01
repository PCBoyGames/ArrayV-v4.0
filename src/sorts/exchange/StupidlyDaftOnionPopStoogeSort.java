package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- FUNGAMER2'S SCRATCH VISUAL -
------------------------------

*/
final public class StupidlyDaftOnionPopStoogeSort extends Sort {

    boolean TOGGLE_stable = false;

    public StupidlyDaftOnionPopStoogeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stupidly Daft Onion Pop Stooge");
        this.setRunAllSortsName("Stupidly Daft Onion Pop Stooge Sort");
        this.setRunSortName("Stupidly Daft Onion Pop Stooge Sort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
        this.setBogoSort(false);
    }

    protected void stooge(int[] array, int left, int right, int dir, int depth) {
        Writes.recordDepth(depth);
        if ((right - left) + 1 > 1) {
            if (right - left == 1 || !TOGGLE_stable) {
                if (Reads.compareIndices(array, left, right, 0.005, true) == dir) Writes.swap(array, left, right, 0.01, true, false);
            }
            if ((right - left) + 1 > 2) {
                Writes.recursion();
                stooge(array, left, right - (int) Math.floor(((right - left) + 1) / 3), dir, depth + 1);
                Writes.recursion();
                stooge(array, right - (int) Math.floor(((right - left) + 1) / 3), right, 0 - dir, depth + 1);
                Writes.recursion();
                stooge(array, left + (int) Math.floor(((right - left) + 1) / 3), right, dir, depth + 1);
                Writes.recursion();
                stooge(array, left, right - (int) Math.floor(((right - left) + 1) / 3), dir, depth + 1);
            }
        }
    }

    protected void popStooge(int[] array, int dir, int length) {
        stooge(array, 0, (int) Math.floor(length / 4), 0 - dir, 0);
        stooge(array, (int) Math.floor(length / 4) + 1, (int) Math.floor(length / 2), dir, 0);
        stooge(array, (int) Math.floor(length / 2) + 1, (int) Math.floor(3 * (length / 4)), 0 - dir, 0);
        stooge(array, (int) Math.floor(3 * (length / 4)), length, dir, 0);
        stooge(array, 0, (int) Math.floor(length / 2), 0 - dir, 0);
        stooge(array, (int) Math.floor(length / 2) + 1, length, dir, 0);
        stooge(array, 0, length, dir, 0);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength; i++) {
            popStooge(array, -1, i);
            popStooge(array, 1, i);
            popStooge(array, -1, i);
            popStooge(array, 1, i);
        }
    }
}