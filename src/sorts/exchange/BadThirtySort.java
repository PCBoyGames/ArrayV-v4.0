package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.BogoSorting;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class BadThirtySort extends BogoSorting {
    public BadThirtySort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Bad Thirty");
        this.setRunAllSortsName("Bad Thirty Sort");
        this.setRunSortName("Bad Thirty Sort");
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
        boolean swaps = true;
        int dir = 1;
        while (swaps) {
            swaps = false;
            boolean escape = false;
            for (int i = 0; i < currentLength && !escape; i++) {
                for (int j = dir == 1 ? i + 1 : currentLength - 1; ((dir == 1 && j < currentLength) || (dir == -1 && j > i)) && !escape; j += dir) {
                    if (Reads.compareIndices(array, i, j, 0.001, true) > 0) {
                        swaps = true;
                        escape = true;
                        Writes.swap(array, i, j, 0.001, true, false);
                    }
                }
            }
            dir *= -1;
        }
    }
}