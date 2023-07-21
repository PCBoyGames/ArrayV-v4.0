package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class ReversePushSort extends Sort {
    public ReversePushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Reverse Push");
        this.setRunAllSortsName("Reverse Push Sort");
        this.setRunSortName("Reverse Pushsort");
        this.setCategory("Exchange Sorts");
        this.setComparisonBased(true);
        this.setBucketSort(false);
        this.setRadixSort(false);
        this.setUnreasonablySlow(false);
        this.setUnreasonableLimit(0);
        this.setBogoSort(false);
    }

    @Override
    public void runSort(int[] array, int currentLength, int bucketCount) {
        boolean anyswaps = true;
        while (anyswaps) {
            anyswaps = false;
            int gap = 1;
            for (int i = currentLength; i - gap > 0;) {
                if (Reads.compareIndices(array, (i - 1) - gap, i - 1, 0.01, true) > 0) {
                    for (int j = 1; j <= gap; j++) Writes.swap(array, i - 1, (i - 1) - j, 0.01, anyswaps = true, false);
                    gap++;
                } else i--;
            }
        }
    }
}