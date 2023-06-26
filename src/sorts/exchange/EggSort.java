package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SO, FIVE DASHES IS A NAME. -
------------------------------

*/
public final class EggSort extends Sort {
    public EggSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Egg");
        this.setRunAllSortsName("Egg Sort");
        this.setRunSortName("Eggsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    protected int maxsorted(int[] array, int start, int end) {
        int a = end - 1;
        int b = end - 1;
        boolean segment = true;
        while (segment) {
            if (b - 1 < start) return start;
            if (Reads.compareIndices(array, b - 1, b, 0.1, true) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= start; s--) if (Reads.compareIndices(array, sel, s, 0.1, true) < 0) sel = s;
        while (Reads.compareIndices(array, sel, a, 0.1, true) <= 0) {
            a--;
            if (a < start) break;
        }
        return a + 1;
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        currentLength = maxsorted(array, currentLength, currentLength);
        while (currentLength > 1) {
            for (int i = 0; i + 1 < currentLength; i++) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) < 0) Writes.insert(array, i, 0, 0.01, true, false);
                else Writes.insert(array, i + 1, 0, 0.01, true, false);
            }
            currentLength--;
            currentLength = maxsorted(array, 0, currentLength);
        }
    }
}
