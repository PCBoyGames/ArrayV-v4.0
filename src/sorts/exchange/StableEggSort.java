package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class StableEggSort extends Sort {

    public StableEggSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Stable Egg");
        this.setRunAllSortsName("Stable Egg Sort");
        this.setRunSortName("Stable Eggsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }
    
    public int maxSorted(int[] array, int start, int end, double delay, boolean mark) {
        int a = end - 1;
        int b = end - 1;
        boolean segment = true;
        while (segment) {
            if (b - 1 < start) return start;
            if (Reads.compareIndices(array, b - 1, b, delay, mark) > 0) segment = false;
            else b--;
        }
        int sel = b - 1;
        for (int s = b - 2; s >= start; s--) if (Reads.compareIndices(array, sel, s, delay, mark) < 0) sel = s;
        while (Reads.compareIndices(array, sel, a, delay, mark) <= 0) {
            a--;
            if (a < start) break;
        }
        return a + 1;
    }
    
    void stableMultiSwap(int[] array, int a, int b, double delay, boolean mark, boolean aux) {
        if (a < b) {
            for (int i = a; i < b; i++) {
                if (Reads.compareIndices(array, i, i + 1, delay, mark) != 0) {
                    Writes.swap(array, i, i + 1, delay, mark, aux);
                }
            }
        } else {
            for (int i = a; i > b; i--) {
                if (Reads.compareIndices(array, i, i - 1, delay, mark) != 0) {
                    Writes.swap(array, i, i - 1, delay, mark, aux);
                }
            }
        }
    }
    
    public void sort(int[] array, int a, int b) {
        b = maxSorted(array, a, b, 0.1, true);
        while (b - a > 1) {
            for (int i = a; i + 1 < b; i++) {
                stableMultiSwap(array, Reads.compareIndices(array, i, i + 1, 0.01, true) <= 0 ? i : i + 1, a, 0.01, true,
                        false);
            }
            b = maxSorted(array, a, b - 1, 0.1, true);
        }
    }

    @Override
    public void runSort(int[] array, int sortLength, int bucketCount) {
        sort(array, 0, sortLength);

    }

}
