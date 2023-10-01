package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OutOfPlaceStableSelectionSort extends Sort {
    public OutOfPlaceStableSelectionSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Out-of-Place Stable Selection");
        this.setRunAllSortsName("Out-of-Place Stable Selection Sort");
        this.setRunSortName("Out-of-Place Stable Selection Sort");
        this.setCategory("Selection Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < currentLength; i++) if (array[i] < min) min = array[i];
        if (min >= 0) min = -1;
        else min--;
        int[] out = Writes.createExternalArray(currentLength);
        Writes.arraycopy(array, 0, out, 0, currentLength, 0.25, true, true);
        for (int i = 0; i < currentLength; i++) {
            int sel = 0;
            while (out[sel] == min) sel++;
            for (int j = sel + 1; j < currentLength; j++) if (out[j] != min) if (Reads.compareIndices(out, sel, j, 0.25, true) > 0) sel = j;
            Writes.write(array, i, out[sel], 0.25, true, false);
            // Decorative. Also helps to visualize part of the aux.
            if (sel > i) array[sel] = min;
            Writes.write(out, sel, min, 0.25, true, true);
        }
    }
}