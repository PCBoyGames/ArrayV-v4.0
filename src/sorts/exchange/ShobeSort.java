package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ShobeSort extends Sort {
    public ShobeSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Shobe");
        this.setRunAllSortsName("Shobe Sort");
        this.setRunSortName("Shobe Sort");
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
        boolean sorted = false;
        int start = 0;
        while (!sorted) {
            sorted = true;
            boolean startfound = false;
            for (int i = start - 1 > 0 ? start - 1 : 0; i + 1 < currentLength; i++) {
                while (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.multiSwap(array, i, currentLength - 1, 0.01, true, sorted = false);
                    if (!startfound) {
                        start = i;
                        startfound = true;
                    }
                }
            }
        }
    }
}