package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- I, GAHAM'S FIRST ALGORITHM -
------------------------------

*/
public class CleanerSort extends Sort {
    public CleanerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Cleaner");
        this.setRunAllSortsName("Cleaner Sort");
        this.setRunSortName("Cleaner Sort");
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
        for (int g = 111; g >= 1; g -= 10) {
            for (int i = 0; i < currentLength; i++) {
                Highlights.markArray(3, i);
                int min = i;
                for (int j = i + g; j < currentLength; j++) if (Reads.compareIndices(array, min, j, 0.1, true) > 0) min = j;
                if (min != i) Writes.swap(array, min, i, 1, true, false);
            }
        }
    }
}