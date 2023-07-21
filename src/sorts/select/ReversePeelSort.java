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
        for (int left = 0, stacked = 0; left < currentLength; left++) {
            for (int right = left + stacked + 1; right < currentLength; right++) {
                if (right == left + stacked + 1) stacked = 0;
                if (Reads.compareIndices(array, left, right, 0.05, true) > 0) {
                    Highlights.markArray(3, left);
                    Writes.insert(array, right, left, 0.05, true, false);
                    Highlights.clearMark(3);
                    stacked++;
                }
            }
        }
    }
}