package sorts.exchange;

import main.ArrayVisualizer;
import sorts.templates.Sort;

/*

CODED FOR ARRAYV BY PCBOYGAMES

------------------------------
- SORTING ALGORITHM MADHOUSE -
------------------------------

*/
public class OptimizedPushSort extends Sort {
    public OptimizedPushSort(ArrayVisualizer arrayVisualizer) {
        super(arrayVisualizer);
        this.setSortListName("Optimized Push");
        this.setRunAllSortsName("Optimized Push Sort");
        this.setRunSortName("Optimized Pushsort");
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
        int first = 1;
        while (anyswaps) {
            anyswaps = false;
            int gap = 1;
            for (int i = first > 1 ? first - 1 : 1; i + gap <= currentLength;) {
                if (Reads.compareIndices(array, i - 1, (i - 1) + gap, 0.01, true) > 0) {
                    for (int j = 1; j <= gap; j++) Writes.swap(array, i - 1, (i - 1) + j, 0.01, true, false);
                    if (!anyswaps) first = i;
                    anyswaps = true;
                    gap++;
                } else i++;
            }
        }
    }
}