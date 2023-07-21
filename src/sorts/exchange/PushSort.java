package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

PORTED TO ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
final public class PushSort extends Sort {
    public PushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Push");
        this.setRunAllSortsName("Push Sort");
        this.setRunSortName("Pushsort");
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
            for (int i = 1; i + gap <= currentLength;) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.01, true) > 0) {
                    for (int j = 1; j <= gap; j++) Writes.swap(array, i - 1, (i - 1) + j, 0.01, anyswaps = true, false);
                    gap++;
                } else i++;
            }
        }
    }
}