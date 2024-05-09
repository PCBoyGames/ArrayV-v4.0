package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

public class BlindSort extends Sort {
    public BlindSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Blind");
        this.setRunAllSortsName("Blind Sort");
        this.setRunSortName("Blindsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    public void runSort(int[] array, int length, int bucketCount) {
        int[] aux = Writes.createExternalArray(length);
        Writes.arraycopy(array, 0, aux, 0, length, 0.035, true, true);
        int j = 0;
        while (true) {
            boolean change = false;
            for (int g = 0; g < length; g++) array[g] = j;
            for (int i = 0; i + 1 < length; i++) {
                if (aux[i] > aux[i + 1]) {
                    Writes.swap(aux, i, i + 1, 0.035, true, true);
                    Writes.swap(array, i, i + 1, 0.035, true, false);
                    change = true;
                }
            }
            j++;
            if (!change) break;
        }
        Writes.arraycopy(aux, 0, array, 0, length, 0.035, true, false);
    }
}