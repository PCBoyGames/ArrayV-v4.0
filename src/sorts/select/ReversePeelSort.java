package sorts.select;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ReversePeelSort extends Sort {
    public ReversePeelSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Peel");
        this.setRunAllSortsName("Reverse Peel Sort");
        this.setRunSortName("Reverse Peelsort");
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
        int stacked = 0;
        for (int left = 0; left < currentLength; left++) {
            for (int right = left + stacked + 1; right < currentLength; right++) {
                if (right == left + stacked + 1) stacked = 0;
                if (Reads.compareIndices(array, left, right, 0.05, true) > 0) {
                    Highlights.markArray(3, left);
                    int item = array[right];
                    for (int pull = right; pull > left; pull--) Writes.write(array, pull, array[pull - 1], 0.05, true, false);
                    Writes.write(array, left, item, 0.05, true, false);
                    Highlights.clearMark(3);
                    stacked++;
                }
            }
        }
    }
}