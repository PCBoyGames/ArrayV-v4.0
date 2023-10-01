package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class InstinctSandpaperSort extends Sort {
    public InstinctSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Instinct Sandpaper");
        this.setRunAllSortsName("Instinct Sandpaper Sort");
        this.setRunSortName("Instinct Sandpapersort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        for (int i = 0; i < currentLength - 1; i++) {
            for (int j = i + 1; j < currentLength; j++) {
                Highlights.markArray(1, i);
                Highlights.markArray(2, j);
                Delays.sleep(0.05);
                Writes.swap(array, i, j, 0.05, true, false);
                if (Reads.compareIndices(array, i, j, 0.05, true) > 0) Writes.swap(array, i, j, 0.05, true, false);
            }
        }
    }
}
