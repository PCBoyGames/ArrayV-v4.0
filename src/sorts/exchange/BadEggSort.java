package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BadEggSort extends Sort {

    public BadEggSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bad Egg");
        this.setRunAllSortsName("Bad Egg Sort");
        this.setRunSortName("Bad Eggsort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(128);
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
    
    void omegaMultiSwap(int[] array, int a, int b, double delay, boolean mark, boolean aux) {
        if (a > b) {
            for (int i = 0; i < a - b; i++)
                Writes.multiSwap(array, b, a, delay, mark, aux);
        } else
            for (int i = 0; i < b - a; i++)
                Writes.multiSwap(array, b, a, delay, mark, aux);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        currentLength = maxSorted(array, 0, currentLength, 0.1, true);
        while (currentLength > 1) {
            for (int i = 0; i + 1 < currentLength; i++) {
                omegaMultiSwap(array, Reads.compareIndices(array, i, i + 1, 0.01, true) < 0 ? i : i + 1, 0, 0.01, true,
                        false);
            }
            currentLength = maxSorted(array, 0, currentLength - 1, 0.1, true);
        }

    }

}
