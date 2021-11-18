package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PeelSort extends Sort {
    public PeelSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Peel");
        this.setRunAllSortsName("Peel Sort");
        this.setRunSortName("Peelsort");
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
        for (int left = 0; left < currentLength; left++) {
            int stacked = 0;
            for (int right = currentLength - 1; right > left; right--) {
                if (Reads.compareIndices(array, left, right + stacked, 0.05, true) > 0) {
                    Highlights.markArray(3, left);
                    int item = array[right + stacked];
                    for (int pull = right + stacked; pull > left; pull--) Writes.write(array, pull, array[pull - 1], 0.05, true, false);
                    Writes.write(array, left, item, 0.05, true, false);
                    stacked++;
                    Highlights.clearMark(3);
                }
            }
        }
    }
}