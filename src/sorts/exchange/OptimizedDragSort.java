package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES
EXTENDING CODE FROM APHITORITE

------------------------------
- MEME MAN'S IN TEC FOR KIDS -
------------------------------

*/
public class OptimizedDragSort extends Sort {

    boolean[] sorted;
    int[] items;

    public OptimizedDragSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Drag");
        this.setRunAllSortsName("Optimized Drag Sort");
        this.setRunSortName("Optimized Dragsort");
        this.setCategory("Exchange Sorts");
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(1024);
        this.setBogoSort(false);
    }

    protected boolean isSorted(int[] array, int a, int b, int idx) {
        int c = a, ce = c + 1;
        for (int i = a; i < b; i++) {
            if (i == idx) continue;
            int cmp = Reads.compareIndices(array, i, idx, 0.001, true);
            c += cmp < 0 ? 1 : 0;
            ce += cmp <= 0 ? 1 : 0;
        }
        return idx >= c && idx < ce;
    }

    protected boolean handleSorted(int[] array, int a, int b, int i) {
        if (sorted[i]) return true;
        if (isSorted(array, a, b, i)) {
            Writes.write(items, i, array[i], 0.001, true, true);
            sorted[i] = true;
            return true;
        } else return false;
    }

    @Override
    public void runSort(int[] array, int length, int bucketCount) {
        int a = 0, b = length;
        sorted = new boolean[length];
        items = Writes.createExternalArray(length);
        for (int i = 0; i < length; i++) {
            sorted[i] = false;
            Writes.visualClear(items, i);
        }
        while (true) {
            int i = a;
            while (i < b && handleSorted(array, a, b, i)) i++;
            if (i == b) break;
            for (int j = i++; i < b; i++) {
                if (!handleSorted(array, a, b, i)) {
                    Writes.swap(array, j, i, 0.001, true, false);
                    j = i;
                } else if (!sorted[i]) {
                    Writes.write(items, i, array[i], 0.001, true, true);
                    sorted[i] = true;
                }
            }
            while (sorted[a]) a++;
            while (sorted[b - 1]) b--;
        }
    }
}
