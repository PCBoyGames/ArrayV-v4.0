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
        int dir = 1;
        int i = 0;
        boolean setup = false;
        boolean sorted = false;
        while (!sorted) {
            i = dir == 1 || !setup ? 0 : currentLength - 2;
            setup = true;
            sorted = true;
            while (i >= 0 && i < currentLength - 1) {
                if (Reads.compareIndices(array, i, i + 1, 0.01, true) > 0) {
                    Writes.swap(array, i, i + 1, 0.01, true, false);
                    dir *= -1;
                    sorted = false;
                }
                i += dir;
            }
            dir *= -1;
        }
    }
}