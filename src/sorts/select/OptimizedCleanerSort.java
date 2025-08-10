package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedCleanerSort extends Sort {
    public OptimizedCleanerSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Cleaner");
        this.setRunAllSortsName("PCBoy's Optimized Cleaner Sort");
        this.setRunSortName("Optimized Cleaner Sort");
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
        int s = Math.max(currentLength / 8, 8);
        int g = 0;
        while (g < currentLength) g += s;
        for (; g >= 0; g -= s) {
            if (g == 0) {
                g = 1;
                s--;
            }
            for (int i = 0; i < currentLength; i++) {
                Highlights.markArray(3, i);
                int min = i;
                for (int j = i + g; j < Math.min(currentLength, i + g + s); j++) if (Reads.compareIndices(array, min, j, 0.1, true) > 0) min = j;
                if (min != i) Writes.swap(array, min, i, 1, true, false);
            }
        }
    }
}