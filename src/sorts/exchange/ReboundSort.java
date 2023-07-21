package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ReboundSort extends Sort {
    public ReboundSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Rebound");
        this.setRunAllSortsName("Rebound Sort");
        this.setRunSortName("Rebound Sort");
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
        boolean setup = false;
        boolean sorted = false;
        for (int dir = 1; !sorted; dir *= -1) {
            int i = dir == 1 || !setup ? 0 : currentLength - 2;
            setup = true;
            sorted = true;
            for (; i >= 0 && i < currentLength - 1; i += dir) {
                if (Reads.compareIndices(array, i, i + 1, 0.125, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.125, true, sorted = false);
                    dir *= -1;
                }
            }
        }
    }
}