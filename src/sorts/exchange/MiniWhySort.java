package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

Coded for ArrayV by Ayako-chan
in collaboration with PCBoy

+---------------------------+
| Sorting Algorithm Scarlet |
+---------------------------+

 */

/**
 * @author Ayako-chan
 * @author PCBoy
 *
 */
public class MiniWhySort extends Sort {

    public MiniWhySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Mini Why");
        this.setRunAllSortsName("Mini Why Sort");
        this.setRunSortName("Mini Whysort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(256);
        this.setBogoSort(false);
    }

    protected void bubbleSort(int[] array, int start, int end, boolean right) {
        int swap = end, comp = right ? 1 : -1;
        while (swap > start) {
            int lastSwap = start;
            for (int i=start; i<swap-1; i++) {
                if (Reads.compareValues(array[i], array[i+1]) == comp) {
                    Writes.swap(array, i, i+1, 0.025, true, false);
                    lastSwap = i+1;
                }
            }
            swap = lastSwap;
        }
    }

    protected void pop(int[] array, int start, int end, int order, boolean invert, int depth) {
        Writes.recordDepth(depth++);
        Writes.recursion();
        if (order < 1 || end-start < 2) {
            this.bubbleSort(array, start, end, !invert);
            return;
        }
        int quarter = (end - start + 1) / 4, half = (end - start + 1) / 2;
        --order;
        this.pop(array, start, start+quarter, order, !invert, depth);
        this.pop(array, start+quarter, start+half, order, invert, depth);
        this.pop(array, start+half, end-quarter, order, !invert, depth);
        this.pop(array, end-quarter, end, order, invert, depth);
        this.pop(array, start, start+half, order, !invert, depth);
        this.pop(array, start+half, end, order, invert, depth);
        this.pop(array, start, end, order, invert, depth);
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        pop(array, 0, sortLength, 20, false, 0);

    }

}
