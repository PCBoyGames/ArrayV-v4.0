package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ShoveSandpaperSort extends Sort {
    public ShoveSandpaperSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shove Sandpaper");
        this.setRunAllSortsName("Shove Sandpaper Sort");
        this.setRunSortName("Shove Sandpapersort");
        this.setCategory("Impractical Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(true);
        this.setUnreasonableLimit(512);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        int pull;
        for (int i = 0; i < currentLength; i++) {
            for (int check = i + 1; check < currentLength; check++) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) pull = i;
                else pull = i + 1;
                while (pull + 1 < currentLength) {
                    Writes.swap(array, pull, pull + 1, 0.01, true, false);
                    pull++;
                }
            }
        }
    }
}